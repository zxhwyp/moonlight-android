package com.limelight.computers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.limelight.AppView;
import com.limelight.HXSLog;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.LimeLog;
import com.limelight.UserData.HXSVmData;
import com.limelight.binding.PlatformBinding;
import com.limelight.discovery.DiscoveryService;
import com.limelight.nvstream.NvConnection;
import com.limelight.nvstream.http.ComputerDetails;
import com.limelight.nvstream.http.NvApp;
import com.limelight.nvstream.http.NvHTTP;
import com.limelight.nvstream.http.PairingManager;
import com.limelight.nvstream.mdns.MdnsComputer;
import com.limelight.nvstream.mdns.MdnsDiscoveryListener;
import com.hxstream.operation.HXSConnection;
import com.limelight.utils.CacheHelper;
import com.limelight.utils.NetHelper;
import com.limelight.utils.ServerHelper;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import org.xmlpull.v1.XmlPullParserException;

import static java.lang.Thread.sleep;

public class ComputerManagerService extends Service {
    private static final int SERVERINFO_POLLING_PERIOD_MS = 1500;
    private static final int APPLIST_POLLING_PERIOD_MS = 30000;
    private static final int APPLIST_FAILED_POLLING_RETRY_MS = 2000;
    private static final int MDNS_QUERY_PERIOD_MS = 1000;
    private static final int OFFLINE_POLL_TRIES = 3;
    private static final int INITIAL_POLL_TRIES = 2;
    private static final int EMPTY_LIST_THRESHOLD = 3;
    private static final int POLL_DATA_TTL_MS = 30000;

    private Handler handler = new Handler();

    public static String serverCertString = "";
    public static boolean BooleanResume = false;
    private AppView.AppObject app = null;

    ComputerManagerService.ApplistPoller poller;

    private int retryTimes = 0;

    private final ComputerManagerBinder binder = new ComputerManagerBinder();

    private ComputerDatabaseManager dbManager;
    private final AtomicInteger dbRefCount = new AtomicInteger(0);

    private IdentityManager idManager;
    private final LinkedList<PollingTuple> pollingTuples = new LinkedList<>();
    private ComputerManagerListener listener = null;
    private final AtomicInteger activePolls = new AtomicInteger(0);
    private boolean pollingActive = false;
    private final Lock defaultNetworkLock = new ReentrantLock();

    private DiscoveryService.DiscoveryBinder discoveryBinder;
    private final ServiceConnection discoveryServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            synchronized (discoveryServiceConnection) {
                DiscoveryService.DiscoveryBinder privateBinder = ((DiscoveryService.DiscoveryBinder) binder);

                // Set us as the event listener
                privateBinder.setListener(createDiscoveryListener());

                // Signal a possible waiter that we're all setup
                discoveryBinder = privateBinder;
                discoveryServiceConnection.notifyAll();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            discoveryBinder = null;
        }
    };

    // Returns true if the details object was modified
    private boolean runPoll(ComputerDetails details, boolean newPc, int offlineCount) throws InterruptedException {
        if (!getLocalDatabaseReference()) {
            return false;
        }

        final int pollTriesBeforeOffline = details.state == ComputerDetails.State.UNKNOWN ?
                INITIAL_POLL_TRIES : OFFLINE_POLL_TRIES;

        activePolls.incrementAndGet();

        // Poll the machine
        try {
            if (!pollComputer(details)) {
                if (!newPc && offlineCount < pollTriesBeforeOffline) {
                    // Return without calling the listener
                    releaseLocalDatabaseReference();
                    return false;
                }

                details.state = ComputerDetails.State.OFFLINE;
            }
        } catch (InterruptedException e) {
            releaseLocalDatabaseReference();
            throw e;
        } finally {
            activePolls.decrementAndGet();
        }

        // If it's online, update our persistent state
        if (details.state == ComputerDetails.State.ONLINE) {
            ComputerDetails existingComputer = dbManager.getComputerByUUID(details.uuid);

            // Check if it's in the database because it could have been
            // removed after this was issued
            if (!newPc && existingComputer == null) {
                // It's gone
                releaseLocalDatabaseReference();
                return false;
            }

            // If we already have an entry for this computer in the DB, we must
            // combine the existing data with this new data (which may be partially available
            // due to detecting the PC via mDNS) without the saved external address. If we
            // write to the DB without doing this first, we can overwrite our existing data.
            if (existingComputer != null) {
                existingComputer.update(details);
                dbManager.updateComputer(existingComputer);
            } else {
                try {
                    // If the active address is a site-local address (RFC 1918),
                    // then use STUN to populate the external address field if
                    // it's not set already.
                    if (details.remoteAddress == null) {
                        InetAddress addr = InetAddress.getByName(details.activeAddress);
                        if (addr.isSiteLocalAddress()) {
                            populateExternalAddress(details);
                        }
                    }
                } catch (UnknownHostException ignored) {
                }

                dbManager.updateComputer(details);
            }
        }

        // Don't call the listener if this is a failed lookup of a new PC
        if ((!newPc || details.state == ComputerDetails.State.ONLINE) && listener != null) {
            listener.notifyComputerUpdated(details);
        }

        releaseLocalDatabaseReference();
        return true;
    }

    private Thread createPollingThread(final PollingTuple tuple) {
        Thread t = new Thread() {
            @Override
            public void run() {

                int offlineCount = 0;
                while (!isInterrupted() && pollingActive && tuple.thread == this) {
                    try {
                        // Only allow one request to the machine at a time
                        synchronized (tuple.networkLock) {
                            // Check if this poll has modified the details
                            if (!runPoll(tuple.computer, false, offlineCount)) {
                                LimeLog.warning(tuple.computer.name + " is offline (try " + offlineCount + ")");
                                offlineCount++;
                            } else {
                                tuple.lastSuccessfulPollMs = SystemClock.elapsedRealtime();
                                offlineCount = 0;
                            }
                        }

                        // Wait until the next polling interval
                        Thread.sleep(SERVERINFO_POLLING_PERIOD_MS);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        t.setName("Polling thread for " + tuple.computer.name);
        return t;
    }

    public class ComputerManagerBinder extends Binder {
        public void startPolling(ComputerManagerListener listener) {
            // Polling is active
            pollingActive = true;

            // Set the listener
            ComputerManagerService.this.listener = listener;

            // Start mDNS autodiscovery too
            discoveryBinder.startDiscovery(MDNS_QUERY_PERIOD_MS);

            synchronized (pollingTuples) {
                for (PollingTuple tuple : pollingTuples) {
                    // Enforce the poll data TTL
                    if (SystemClock.elapsedRealtime() - tuple.lastSuccessfulPollMs > POLL_DATA_TTL_MS) {
                        LimeLog.info("Timing out polled state for " + tuple.computer.name);
                        tuple.computer.state = ComputerDetails.State.UNKNOWN;
                    }

                    // Report this computer initially
                    listener.notifyComputerUpdated(tuple.computer);

                    // This polling thread might already be there
                    if (tuple.thread == null) {
                        tuple.thread = createPollingThread(tuple);
                        tuple.thread.start();
                    }
                }
            }
        }

        public void waitForReady() {
            synchronized (discoveryServiceConnection) {
                try {
                    while (discoveryBinder == null) {
                        // Wait for the bind notification
                        discoveryServiceConnection.wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    // InterruptedException clears the thread's interrupt status. Since we can't
                    // handle that here, we will re-interrupt the thread to set the interrupt
                    // status back to true.
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void waitForPollingStopped() {
            while (activePolls.get() != 0) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    // InterruptedException clears the thread's interrupt status. Since we can't
                    // handle that here, we will re-interrupt the thread to set the interrupt
                    // status back to true.
                    Thread.currentThread().interrupt();
                }
            }
        }

        public boolean addComputerBlocking(ComputerDetails fakeDetails) throws InterruptedException {
            return ComputerManagerService.this.addComputerBlocking(fakeDetails);
        }

        public void removeComputer(ComputerDetails computer) {
            ComputerManagerService.this.removeComputer(computer);
        }

        public void stopPolling() {
            // Just call the unbind handler to cleanup
            ComputerManagerService.this.onUnbind(null);
        }

        public ApplistPoller createAppListPoller(ComputerDetails computer) {
            return new ApplistPoller(computer);
        }

        public String getUniqueId() {
            return idManager.getUniqueId();
        }

        private void doPair(final ComputerDetails computer) {
            try {
                if (computer.state == ComputerDetails.State.OFFLINE ||
                        ServerHelper.getCurrentAddressFromComputer(computer) == null) {
                    return;
                }
            } catch (IOException e) {
                return;
            }
            if (computer.runningGameId != 0) {
                HXSLog.info(" 当前海星云游戏正在游戏中，你必须在配对之前先退出游戏 ");
                //            --state--连接失败 dopair
                return;
            }
            HXSLog.info(" 尝试与海星云游戏建立连接…… ");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NvHTTP httpConn;
                    String message;
                    boolean success = false;
                    try {
                        HXSLog.info("配对------");
                        String uniqueId = idManager.getUniqueId();
                        httpConn = new NvHTTP(ServerHelper.getCurrentAddressFromComputer(computer),
                                uniqueId,
                                computer.serverCert,
                                PlatformBinding.getCryptoProvider(ComputerManagerService.this));
                        PairingManager.PairState pairstate = httpConn.getPairState();
                        HXSLog.info(pairstate.toString() + "----------------0");
                        if (pairstate == PairingManager.PairState.PAIRED) {
                            // Don't display any toast, but open the app list
                            message = null;
                            success = true;
                        } else {
                            final String pinStr = PairingManager.generatePinString();
                            requestPair(computer, pinStr);
                            PairingManager pm = httpConn.getPairingManager();
                            PairingManager.PairState pairState = pm.pair(httpConn.getServerInfo(), pinStr);
                            serverCertString = pm.getPairedCertString();
                            HXSLog.info("serverCertstring:" + serverCertString);

                            HXSLog.info(pairState.toString() + "----------2");
                            if (pairState == PairingManager.PairState.PIN_WRONG) {
                                message = " 尝试连接错误，请点击重试或重新连接 ";
                            } else if (pairState == PairingManager.PairState.FAILED) {
                                message = " 尝试连接错误，请点击重试或重新连接 ";
                            } else if (pairState == PairingManager.PairState.ALREADY_IN_PROGRESS) {
                                message = " 已经在建立连接，请稍等 ";
                            } else if (pairState == PairingManager.PairState.PAIRED) {
                                // Just navigate to the app view without displaying a toast
                                message = null;
                                success = true;
                                // Pin this certificate for later HTTPS use
                                try {
                                    getComputer(computer.uuid).serverCert = pm.getPairedCert();
                                } catch (Exception e) {
                                    HXSLog.info(e.toString());
                                }
                                // Invalidate reachability information after pairing to force
                                // a refresh before reading pair state again
                                invalidateStateForComputer(computer.uuid);
                                try {
                                    pollComputer(computer);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Should be no other values

                                message = null;
                            }
                        }
                    } catch (UnknownHostException e) {
                        message = " 解析海星云地址错误 ";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                                HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);

                            }
                        });
                    } catch (FileNotFoundException e) {
                        message = " 海星云走神了，换台机器吧 ";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                                HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);

                            }
                        });
                        //GeForce Experience返回了HTTP 404 错误。确保你的显卡支持GAMESTREAM\n\n
                        //	    使用远程控制软件同样会引起此错误，请尝试重启电脑或者重新安装GeForce Experience
                    } catch (XmlPullParserException | IOException e) {
                        e.printStackTrace();
                        message = e.getMessage();
                    }

                    final String toastMessage = message;
                    final boolean toastSuccess = success;
                    HXSLog.info("配对提示消息：" + toastMessage);
                    if (toastSuccess) {
                        // Open the app list after a successful pairing attempt
                        if (computer.state == ComputerDetails.State.ONLINE) {
                            HXSLog.info("computer 在线");
                        }
                        if (computer.state == ComputerDetails.State.UNKNOWN) {
                            HXSLog.info("computer 未知");
                        }
                        if (computer.state == ComputerDetails.State.OFFLINE) {
                            HXSLog.info("computer 离线");
                        }

                        doAppList(computer);
                    } else {
                        // Start polling again if we're still in the foreground
                        retryTimes++;
                        if (retryTimes > 3) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                                    HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);

                                }
                            });
                            return;
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        doPair(computer);

                    }
                }
            }).start();
        }


        public void doAppList(ComputerDetails computer) {
            if (computer.state == ComputerDetails.State.OFFLINE) {
                //                --state--连接失败--海星云离线
                HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectSuccess);
                    HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectSuccess);
                }
            });
            poller = new ApplistPoller(computer);
            poller.start();
        }

        private void requestPair(ComputerDetails computer, String pinStr) {
            HXSHttpRequestCenter.requestPair(pinStr, new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
                @Override
                public void returnAnalysis(String result) {
                    HXSLog.info("pairResult" + result);
                }
            });
        }

        public ComputerDetails getComputer(String uuid) {
            synchronized (pollingTuples) {
                for (PollingTuple tuple : pollingTuples) {
                    if (uuid.equals(tuple.computer.uuid)) {
                        return tuple.computer;
                    }
                }
            }

            return null;
        }

        public void invalidateStateForComputer(String uuid) {
            synchronized (pollingTuples) {
                for (PollingTuple tuple : pollingTuples) {
                    if (uuid.equals(tuple.computer.uuid)) {
                        // We need the network lock to prevent a concurrent poll
                        // from wiping this change out
                        synchronized (tuple.networkLock) {
                            tuple.computer.state = ComputerDetails.State.UNKNOWN;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (discoveryBinder != null) {
            // Stop mDNS autodiscovery
            discoveryBinder.stopDiscovery();
        }

        // Stop polling
        pollingActive = false;
        synchronized (pollingTuples) {
            for (PollingTuple tuple : pollingTuples) {
                if (tuple.thread != null) {
                    // Interrupt and remove the thread
                    tuple.thread.interrupt();
                    tuple.thread = null;
                }
            }
        }

        // Remove the listener
        listener = null;

        return false;
    }

    private void populateExternalAddress(ComputerDetails details) {
        boolean boundToNetwork = false;
        boolean activeNetworkIsVpn = NetHelper.isActiveNetworkVpn(this);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check if we're currently connected to a VPN which may send our
        // STUN request from an unexpected interface
        if (activeNetworkIsVpn) {
            // Acquire the default network lock since we could be changing global process state
            defaultNetworkLock.lock();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // On Lollipop or later, we can bind our process to the underlying interface
                // to ensure our STUN request goes out on that interface or not at all (which is
                // preferable to getting a VPN endpoint address back).
                Network[] networks = connMgr.getAllNetworks();
                for (Network net : networks) {
                    NetworkCapabilities netCaps = connMgr.getNetworkCapabilities(net);
                    if (netCaps != null) {
                        if (!netCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                                !netCaps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                            // This network looks like an underlying multicast-capable transport,
                            // so let's guess that it's probably where our mDNS response came from.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (connMgr.bindProcessToNetwork(net)) {
                                    boundToNetwork = true;
                                    break;
                                }
                            } else if (ConnectivityManager.setProcessDefaultNetwork(net)) {
                                boundToNetwork = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Perform the STUN request if we're not on a VPN or if we bound to a network
        if (!activeNetworkIsVpn || boundToNetwork) {
            details.remoteAddress = NvConnection.findExternalAddressForMdns("stun.moonlight-stream.org", 3478);
        }

        // Unbind from the network
        if (boundToNetwork) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connMgr.bindProcessToNetwork(null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ConnectivityManager.setProcessDefaultNetwork(null);
            }
        }

        // Unlock the network state
        if (activeNetworkIsVpn) {
            defaultNetworkLock.unlock();
        }
    }

    private MdnsDiscoveryListener createDiscoveryListener() {
        return new MdnsDiscoveryListener() {
            @Override
            public void notifyComputerAdded(MdnsComputer computer) {
                ComputerDetails details = new ComputerDetails();

                // Populate the computer template with mDNS info
                if (computer.getLocalAddress() != null) {
                    details.localAddress = computer.getLocalAddress().getHostAddress();

                    // Since we're on the same network, we can use STUN to find
                    // our WAN address, which is also very likely the WAN address
                    // of the PC. We can use this later to connect remotely.
                    if (computer.getLocalAddress() instanceof Inet4Address) {
                        populateExternalAddress(details);
                    }
                }
                if (computer.getIpv6Address() != null) {
                    details.ipv6Address = computer.getIpv6Address().getHostAddress();
                }

                try {
                    // Kick off a blocking serverinfo poll on this machine
                    if (!addComputerBlocking(details)) {
                        LimeLog.warning("Auto-discovered PC failed to respond: " + details);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    // InterruptedException clears the thread's interrupt status. Since we can't
                    // handle that here, we will re-interrupt the thread to set the interrupt
                    // status back to true.
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public void notifyComputerRemoved(MdnsComputer computer) {
                // Nothing to do here
            }

            @Override
            public void notifyDiscoveryFailure(Exception e) {
                LimeLog.severe("mDNS discovery failed");
                e.printStackTrace();
            }
        };
    }

    private void addTuple(ComputerDetails details) {
        synchronized (pollingTuples) {
            for (PollingTuple tuple : pollingTuples) {
                // Check if this is the same computer
                if (tuple.computer.uuid.equals(details.uuid)) {
                    // Update the saved computer with potentially new details
                    tuple.computer.update(details);

                    // Start a polling thread if polling is active
                    if (pollingActive && tuple.thread == null) {
                        tuple.thread = createPollingThread(tuple);
                        tuple.thread.start();
                    }

                    // Found an entry so we're done
                    return;
                }
            }

            // If we got here, we didn't find an entry
            PollingTuple tuple = new PollingTuple(details, null);
            if (pollingActive) {
                tuple.thread = createPollingThread(tuple);
            }
            pollingTuples.add(tuple);
            if (tuple.thread != null) {
                tuple.thread.start();
            }
        }
    }

    public boolean addComputerBlocking(ComputerDetails fakeDetails) throws InterruptedException {
        // Block while we try to fill the details

        // We cannot use runPoll() here because it will attempt to persist the state of the machine
        // in the database, which would be bad because we don't have our pinned cert loaded yet.
        if (pollComputer(fakeDetails)) {
            // See if we have record of this PC to pull its pinned cert
            synchronized (pollingTuples) {
                for (PollingTuple tuple : pollingTuples) {
                    if (tuple.computer.uuid.equals(fakeDetails.uuid)) {
                        fakeDetails.serverCert = tuple.computer.serverCert;
                        break;
                    }
                }
            }

            // Poll again, possibly with the pinned cert, to get accurate pairing information.
            // This will insert the host into the database too.
            runPoll(fakeDetails, true, 0);
        }

        // If the machine is reachable, it was successful
        if (fakeDetails.state == ComputerDetails.State.ONLINE) {
            LimeLog.info("New PC (" + fakeDetails.name + ") is UUID " + fakeDetails.uuid);

            // Start a polling thread for this machine
            addTuple(fakeDetails);
            return true;
        } else {
            return false;
        }
    }

    public void removeComputer(ComputerDetails computer) {
        if (!getLocalDatabaseReference()) {
            return;
        }

        // Remove it from the database
        dbManager.deleteComputer(computer);

        synchronized (pollingTuples) {
            // Remove the computer from the computer list
            for (PollingTuple tuple : pollingTuples) {
                if (tuple.computer.uuid.equals(computer.uuid)) {
                    if (tuple.thread != null) {
                        // Interrupt the thread on this entry
                        tuple.thread.interrupt();
                        tuple.thread = null;
                    }
                    pollingTuples.remove(tuple);
                    break;
                }
            }
        }

        releaseLocalDatabaseReference();
    }

    private boolean getLocalDatabaseReference() {
        if (dbRefCount.get() == 0) {
            return false;
        }

        dbRefCount.incrementAndGet();
        return true;
    }

    private void releaseLocalDatabaseReference() {
        if (dbRefCount.decrementAndGet() == 0) {
            dbManager.close();
        }
    }

    private ComputerDetails tryPollIp(ComputerDetails details, String address) {
        try {
            NvHTTP http = new NvHTTP(address, idManager.getUniqueId(), details.serverCert,
                    PlatformBinding.getCryptoProvider(ComputerManagerService.this));

            ComputerDetails newDetails = http.getComputerDetails();

            // Check if this is the PC we expected
            if (newDetails.uuid == null) {
                LimeLog.severe("Polling returned no UUID!");
                return null;
            }
            // details.uuid can be null on initial PC add
            else if (details.uuid != null && !details.uuid.equals(newDetails.uuid)) {
                // We got the wrong PC!
                LimeLog.info("Polling returned the wrong PC!");
                return null;
            }

            return newDetails;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private static class ParallelPollTuple {
        public String address;
        public ComputerDetails existingDetails;

        public boolean complete;
        public Thread pollingThread;
        public ComputerDetails returnedDetails;

        public ParallelPollTuple(String address, ComputerDetails existingDetails) {
            this.address = address;
            this.existingDetails = existingDetails;
        }

        public void interrupt() {
            if (pollingThread != null) {
                pollingThread.interrupt();
            }
        }
    }

    private void startParallelPollThread(ParallelPollTuple tuple, HashSet<String> uniqueAddresses) {
        // Don't bother starting a polling thread for an address that doesn't exist
        // or if the address has already been polled with an earlier tuple
        if (tuple.address == null || !uniqueAddresses.add(tuple.address)) {
            tuple.complete = true;
            tuple.returnedDetails = null;
            return;
        }

        tuple.pollingThread = new Thread() {
            @Override
            public void run() {
                ComputerDetails details = tryPollIp(tuple.existingDetails, tuple.address);

                synchronized (tuple) {
                    tuple.complete = true; // Done
                    tuple.returnedDetails = details; // Polling result

                    tuple.notify();
                }
            }
        };
        tuple.pollingThread.setName("Parallel Poll - " + tuple.address + " - " + tuple.existingDetails.name);
        tuple.pollingThread.start();
    }

    private ComputerDetails parallelPollPc(ComputerDetails details) throws InterruptedException {
        ParallelPollTuple localInfo = new ParallelPollTuple(details.localAddress, details);
        ParallelPollTuple manualInfo = new ParallelPollTuple(details.manualAddress, details);
        ParallelPollTuple remoteInfo = new ParallelPollTuple(details.remoteAddress, details);
        ParallelPollTuple ipv6Info = new ParallelPollTuple(details.ipv6Address, details);

        // These must be started in order of precedence for the deduplication algorithm
        // to result in the correct behavior.
        HashSet<String> uniqueAddresses = new HashSet<>();
        startParallelPollThread(localInfo, uniqueAddresses);
        startParallelPollThread(manualInfo, uniqueAddresses);
        startParallelPollThread(remoteInfo, uniqueAddresses);
        startParallelPollThread(ipv6Info, uniqueAddresses);

        try {
            // Check local first
            synchronized (localInfo) {
                while (!localInfo.complete) {
                    localInfo.wait();
                }

                if (localInfo.returnedDetails != null) {
                    localInfo.returnedDetails.activeAddress = localInfo.address;
                    return localInfo.returnedDetails;
                }
            }

            // Now manual
            synchronized (manualInfo) {
                while (!manualInfo.complete) {
                    manualInfo.wait();
                }

                if (manualInfo.returnedDetails != null) {
                    manualInfo.returnedDetails.activeAddress = manualInfo.address;
                    return manualInfo.returnedDetails;
                }
            }

            // Now remote IPv4
            synchronized (remoteInfo) {
                while (!remoteInfo.complete) {
                    remoteInfo.wait();
                }

                if (remoteInfo.returnedDetails != null) {
                    remoteInfo.returnedDetails.activeAddress = remoteInfo.address;
                    return remoteInfo.returnedDetails;
                }
            }

            // Now global IPv6
            synchronized (ipv6Info) {
                while (!ipv6Info.complete) {
                    ipv6Info.wait();
                }

                if (ipv6Info.returnedDetails != null) {
                    ipv6Info.returnedDetails.activeAddress = ipv6Info.address;
                    return ipv6Info.returnedDetails;
                }
            }
        } finally {
            // Stop any further polling if we've found a working address or we've been
            // interrupted by an attempt to stop polling.
            localInfo.interrupt();
            manualInfo.interrupt();
            remoteInfo.interrupt();
            ipv6Info.interrupt();
        }

        return null;
    }

    private boolean pollComputer(ComputerDetails details) throws InterruptedException {
        // Poll all addresses in parallel to speed up the process
        LimeLog.info("Starting parallel poll for " + details.name + " (" + details.localAddress + ", " + details.remoteAddress + ", " + details.manualAddress + ", " + details.ipv6Address + ")");
        ComputerDetails polledDetails = parallelPollPc(details);
        LimeLog.info("Parallel poll for " + details.name + " returned address: " + details.activeAddress);

        if (polledDetails != null) {
            details.update(polledDetails);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate() {
        // Bind to the discovery service
        bindService(new Intent(this, DiscoveryService.class),
                discoveryServiceConnection, Service.BIND_AUTO_CREATE);

        // Lookup or generate this device's UID
        idManager = new IdentityManager(this);

        // Initialize the DB
        dbManager = new ComputerDatabaseManager(this);
        dbRefCount.set(1);

        // Grab known machines into our computer list
        if (!getLocalDatabaseReference()) {
            return;
        }

        for (ComputerDetails computer : dbManager.getAllComputers()) {
            // Add tuples for each computer
            addTuple(computer);
        }

        releaseLocalDatabaseReference();
    }

    @Override
    public void onDestroy() {
        if (discoveryBinder != null) {
            // Unbind from the discovery service
            unbindService(discoveryServiceConnection);
        }

        // FIXME: Should await termination here but we have timeout issues in HttpURLConnection

        // Remove the initial DB reference
        releaseLocalDatabaseReference();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ApplistPoller {
        private Thread thread;
        private final ComputerDetails computer;
        private final Object pollEvent = new Object();
        private boolean receivedAppList = false;

        public ApplistPoller(ComputerDetails computer) {
            this.computer = computer;
        }

        public void pollNow() {
            synchronized (pollEvent) {
                pollEvent.notify();
            }
        }

        private boolean waitPollingDelay() {
            try {
                synchronized (pollEvent) {
                    if (receivedAppList) {
                        // If we've already reported an app list successfully,
                        // wait the full polling period
                        pollEvent.wait(APPLIST_POLLING_PERIOD_MS);
                    } else {
                        // If we've failed to get an app list so far, retry much earlier
                        pollEvent.wait(APPLIST_FAILED_POLLING_RETRY_MS);
                    }
                }
            } catch (InterruptedException e) {
                return false;
            }

            return thread != null && !thread.isInterrupted();
        }

        private PollingTuple getPollingTuple(ComputerDetails details) {
            synchronized (pollingTuples) {
                for (PollingTuple tuple : pollingTuples) {
                    if (details.uuid.equals(tuple.computer.uuid)) {
                        return tuple;
                    }
                }
            }

            return null;
        }

        public void start() {
            thread = new Thread() {
                @Override
                public void run() {
                    int emptyAppListResponses = 0;
                    do {
                        // Can't poll if it's not online or paired
                        if (computer.state != ComputerDetails.State.ONLINE ||
                                computer.pairState != PairingManager.PairState.PAIRED) {
                            if (listener != null) {
                                listener.notifyComputerUpdated(computer);
                            }
                            continue;
                        }

                        // Can't poll if there's no UUID yet
                        if (computer.uuid == null) {
                            continue;
                        }

                        PollingTuple tuple = getPollingTuple(computer);

                        try {
                            NvHTTP http = new NvHTTP(ServerHelper.getCurrentAddressFromComputer(computer), idManager.getUniqueId(),
                                    computer.serverCert, PlatformBinding.getCryptoProvider(ComputerManagerService.this));

                            String appList;
                            if (tuple != null) {
                                // If we're polling this machine too, grab the network lock
                                // while doing the app list request to prevent other requests
                                // from being issued in the meantime.
                                synchronized (tuple.networkLock) {
                                    appList = http.getAppListRaw();
                                }
                            } else {
                                // No polling is happening now, so we just call it directly
                                appList = http.getAppListRaw();
                            }

                            List<NvApp> list = NvHTTP.getAppListByReader(new StringReader(appList));
                            if (list.isEmpty()) {
                                LimeLog.warning("Empty app list received from " + computer.uuid);

                                // The app list might actually be empty, so if we get an empty response a few times
                                // in a row, we'll go ahead and believe it.
                                emptyAppListResponses++;
                            }
                            if (!appList.isEmpty() &&
                                    (!list.isEmpty() || emptyAppListResponses >= EMPTY_LIST_THRESHOLD)) {
                                // Open the cache file
                                OutputStream cacheOut = null;
                                try {
                                    cacheOut = CacheHelper.openCacheFileForOutput(getCacheDir(), "applist", computer.uuid);
                                    CacheHelper.writeStringToOutputStream(cacheOut, appList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (cacheOut != null) {
                                            cacheOut.close();
                                        }
                                    } catch (IOException ignored) {
                                    }
                                }

                                // Reset empty count if it wasn't empty this time
                                if (!list.isEmpty()) {
                                    emptyAppListResponses = 0;
                                }

                                // Update the computer
                                computer.rawAppList = appList;
                                receivedAppList = true;

                                // Notify that the app list has been updated
                                // and ensure that the thread is still active
                                if (listener != null && thread != null) {
                                    listener.notifyComputerUpdated(computer);
                                }
                            } else if (appList.isEmpty()) {
                                LimeLog.warning("Null app list received from " + computer.uuid);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                    while (waitPollingDelay());
                }
            };
            thread.setName("App list polling thread for " + computer.name);
            thread.start();
        }

        public void stop() {
            if (thread != null) {
                thread.interrupt();

                // Don't join here because we might be blocked on network I/O

                thread = null;
            }
        }
    }
}

class PollingTuple {
    public Thread thread;
    public final ComputerDetails computer;
    public final Object networkLock;
    public long lastSuccessfulPollMs;

    public PollingTuple(ComputerDetails computer, Thread thread) {
        this.computer = computer;
        this.thread = thread;
        this.networkLock = new Object();
    }
}

class ReachabilityTuple {
    public final String reachableAddress;
    public final ComputerDetails computer;

    public ReachabilityTuple(ComputerDetails computer, String reachableAddress) {
        this.computer = computer;
        this.reachableAddress = reachableAddress;
    }
}