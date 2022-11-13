package com.limelight.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;


import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.common.HXSGameManager;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.UserData.HXSVmData;
import com.limelight.computers.ComputerManagerService;
import com.limelight.nvstream.http.ComputerDetails;
import com.hxstream.operation.HXSConnection;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class RequestVmService extends Service implements HXSVmData.IVmDataObserver {


    private static RequestVmService instance = null;
    private int retryTimes;
    private Thread addThread;
    private ComputerManagerService.ComputerManagerBinder managerBinder;
    private final LinkedBlockingQueue<String> computersToAdd = new LinkedBlockingQueue<>();
    private boolean isPending = false;
    public static int networkErrorTimes = 0;
    private int scheduleTimes = 0;
    Handler handler = new Handler(Looper.getMainLooper());

    public RequestVmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HXSVmData.addVmDataObserver(this);
    }

    private void AddComputers() {
        bindService(new Intent(RequestVmService.this, ComputerManagerService.class), serviceConnection, Service.BIND_AUTO_CREATE);
        computersToAdd.add(HXSVmData.ip);
        autoStartGameWithOption();
    }

    public static RequestVmService getInstance() {
        return instance;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, final IBinder binder) {
            managerBinder = ((ComputerManagerService.ComputerManagerBinder) binder);
            startAddThread();
        }

        public void onServiceDisconnected(ComponentName className) {
            joinAddThread();
            managerBinder = null;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //        HXSVmData.portOffset =-38000;
        //        HXSVmData.ip = "wan214.haixingcloud.com";
        retryTimes = 0;
        HXSVmData.VM_UUID = "";
        if (HXSVmData.getUuid().equals("")) {
            HXSHttpRequestCenter.requestIp(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
                @Override
                public void returnAnalysis(String result) {
                    returnIp(result);
                }
            });
        } else {
            resumeWithIp(HXSVmData.getUuid(), HXSVmData.ip, HXSVmData.portOffset);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void resumeWithIp(String uuid, String ip, int offset) {
        HXSVmData.VM_UUID = uuid;


        HXSVmData.ip = ip;

        HXSVmData.portOffset = offset;
        int comboPort = 50052 + HXSVmData.portOffset;
        HXSVmData.ComboURL = "https://" + HXSVmData.ip + ":" + comboPort + "/sendkey";
        HXSVmData.PairURL = "https://" + HXSVmData.ip + ":" + comboPort + "/pair";
        //        --state--排队到号正在连接
        AddComputers();
    }

    private void returnIp(String result) {
        HXSLog.info("returnIp" + result);
        if (HXSConstant.USER_NOT_AUTHORIZED_STRING.equals(result)) {
            //用户token失效
            return;
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            switch (jsonObject.getInt("code")) {
                case 200:
                    JSONObject resultData = new JSONObject(jsonObject.getString("data"));
                    HXSVmData.VM_UUID = resultData.getString("uuid");
                    if (resultData.getString("state").equals("running")) {
                        HXSVmData.ip = resultData.getString("ip");
                        HXSVmData.portOffset = resultData.getInt("port_offset");
                        int comboPort = 50052 + HXSVmData.portOffset;
                        HXSVmData.ComboURL = "https://" + HXSVmData.ip + ":" + comboPort + "/sendkey";
                        HXSVmData.PairURL = "https://" + HXSVmData.ip + ":" + comboPort + "/pair";
                        //--state--排队到号正在连接
                        HXSLog.info("排队到号正在连接");
                        AddComputers();
                        HXSVmData.isVmRunning = true;
                        HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnecting);
                        HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnecting);

                    } else if (resultData.getString("state").equals("ping_fail")) {
                        //--state--连接失败
                        HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageServerError);
                        HXSVmData.clearVmData();
                        HXSLog.info("连接失败");
                        isPending = false;
                    } else if (resultData.getString("state").equals("init")) {
                        int order = resultData.getInt("order");
                        //--state--正在排队
                        if (!isPending) {
                            isPending = true;
                            HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StatePending);
                            HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StatePending);
                        }
                        HXSConnection.getInstance().getCallback().updateOrder(order);
                        HXSLog.info("正在排队" + order);
                        DelayOneGo();
                    } else if (resultData.getString("state").equals("schedule")) {
                        scheduleTimes++;
                        if (scheduleTimes == 10) {
                            HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                            HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);
                            //                        --state--连接失败
                            DelayOneEnd();
                            HXSVmData.clearVmData();
                            return;
                        }
                        String order = resultData.getString("order");
                        HXSLog.info("正在排队" + order);
                        if (!isPending) {
                            isPending = true;
                            HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StatePending);
                            HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StatePending);

                        }
                        //--state--正在排队
                        DelayOneGo();
                    }
                    break;
                case 403:
                    if (jsonObject.getString("message").trim().equals("failed,run out of credit".trim())) {
                        //                        --state--用户余额不足
                        HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageLackBalance);
                        DelayOneEnd();
                        HXSVmData.clearVmData();
                    } else if (jsonObject.getString("message").trim().equals("failed,users are restricted to login".trim())) {
                        //                        --state--用户被禁止登陆
                        HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageUserBanned);
                        DelayOneEnd();
                        HXSVmData.clearVmData();
                    } else if (jsonObject.getString("message").trim().equals("failed,run out of quota".trim())) {
                        //                        --state--重复启动
                        HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageConnectionRunning);
                        DelayOneEnd();
                        HXSVmData.clearVmData();
                    } else {
                        HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                        HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);
                        //                        --state--连接失败
                        DelayOneEnd();
                        HXSVmData.clearVmData();
                    }
                    break;
                default:
                    HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                    HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);
                    //                    --state--连接失败
                    DelayOneEnd();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (networkErrorTimes >= 3) {
                networkErrorTimes = 0;
                HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateConnectFail);
                HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateConnectFail);
                //                    --state--连接失败
                DelayOneEnd();
            } else {
                networkErrorTimes++;
                DelayOneGo();
            }

        }
    }

    private void autoStartGameWithOption() {
        if (HXSVmData.option == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (HXSVmData.option.gameType) {
                    case 1:
                        HXSGameManager.killSteamProcess("hello");
                        HXSGameManager.startSteamGameWithSelfAccount(HXSVmData.option.steamAccount, HXSVmData.option.steamPassword, "hello");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HXSGameManager.startSteamGameWithSelfAccount(HXSVmData.option.steamAccount, HXSVmData.option.steamPassword, "hello");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //                                        HXSGameManager.autoResolution();
                                        //                                        HXSHttpRequestCenter.requestStartCache(DeviceInfo.netIp,HXSVmData.region,HXSVmData.userName);
                                    }
                                }, 1000);
                            }
                        }, 2000);
                        break;
                    case 2:
                        HXSGameManager.startSteamGame("hello");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HXSGameManager.startSteamGame("hello");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //                                        HXSGameManager.autoResolution();
                                        try {
                                            sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //                                        HXSHttpRequestCenter.requestStartCache(DeviceInfo.netIp,HXSVmData.region,HXSVmData.userName);
                                    }
                                }, 1000);
                            }
                        }, 2000);

                        break;
                    case 3:
                        //                        HXSGameManager.autoResolution();
                        //                        HXSHttpRequestCenter.requestStartCache(DeviceInfo.netIp,HXSVmData.region,HXSVmData.userName);
                        break;
                }

            }
        }).start();
    }

    private void DelayOneEnd() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }.start();
    }

    private void DelayOneGo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HXSHttpRequestCenter.requestIp(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
                    @Override
                    public void returnAnalysis(String result) {
                        returnIp(result);
                    }
                });
            }
        }.start();
    }

    private void startAddThread() {
        addThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    String computer;
                    try {
                        computer = computersToAdd.take();
                        computersToAdd.clear();
                    } catch (InterruptedException e) {
                        return;
                    }
                    doAddPc(computer);
                }
            }
        };
        addThread.setName("界面 - 配对电脑");
        addThread.start();
    }

    private void doAddPc(final String host) {
        boolean wrongSiteLocal = false;
        boolean success;
        try {
            ComputerDetails details = new ComputerDetails();
            details.manualAddress = host;
            success = managerBinder.addComputerBlocking(details);
        } catch (InterruptedException | IllegalArgumentException e) {
            // This can be thrown from OkHttp if the host fails to canonicalize to a valid name.
            // https://github.com/square/okhttp/blob/okhttp_27/okhttp/src/main/java/com/squareup/okhttp/HttpUrl.java#L705
            e.printStackTrace();
            success = false;
        }
        if (!success) {
            wrongSiteLocal = isWrongSubnetSiteLocalAddress(host);
        }
        if (wrongSiteLocal) {
            HXSLog.info("连接错误--wrongSiteLocal");
            //            连接错误
            //            Dialog.displayDialog(this, getResources().getString(R.string.conn_error_title), getResources().getString(R.string.addpc_wrong_sitelocal), false);
        } else if (!success) {
            //改善网络不稳定影响，
            if (retryTimes < 10) {
                HXSLog.info("重试");
                retryTimes++;
                Thread retryRunnable = new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(5000);
                            doAddPc(host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                retryRunnable.start();
            } else {
                HXSLog.info("重试结束--连接失败");
                //Unable to connect to the specified computer. Make sure the required ports are allowed through the firewall.
                //                无法连接到指定的电脑。请确保指定的端口没有被防火墙阻止
                //                setRequestVmState("网络错误，请检查本地网络和线路选择"); 连接错误
                //Dialog.displayDialog(this, getResources().getString(R.string.conn_error_title), getResources().getString(R.string.addpc_fail), false);
            }
            //            Dialog.displayDialog(this, getResources().getString(R.string.conn_error_title), getResources().getString(R.string.addpc_fail), false);
        }

    }

    private boolean isWrongSubnetSiteLocalAddress(String address) {
        try {
            InetAddress targetAddress = InetAddress.getByName(address);
            if (!(targetAddress instanceof Inet4Address) || !targetAddress.isSiteLocalAddress()) {
                return false;
            }

            // We have a site-local address. Look for a matching local interface.
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
                    if (!(addr.getAddress() instanceof Inet4Address) || !addr.getAddress().isSiteLocalAddress()) {
                        // Skip non-site-local or non-IPv4 addresses
                        continue;
                    }

                    byte[] targetAddrBytes = targetAddress.getAddress();
                    byte[] ifaceAddrBytes = addr.getAddress().getAddress();

                    // Compare prefix to ensure it's the same
                    boolean addressMatches = true;
                    for (int i = 0; i < addr.getNetworkPrefixLength(); i++) {
                        if ((ifaceAddrBytes[i / 8] & (1 << (i % 8))) != (targetAddrBytes[i / 8] & (1 << (i % 8)))) {
                            addressMatches = false;
                            break;
                        }
                    }

                    if (addressMatches) {
                        return false;
                    }
                }
            }

            // Couldn't find a matching interface
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private void joinAddThread() {
        if (addThread != null) {
            addThread.interrupt();

            try {
                addThread.join();
            } catch (InterruptedException ignored) {
            }

            addThread = null;
        }
    }

    public void shutdownServiceConnection() {
        //        if (serviceConnection != null) {
        //            unbindService(serviceConnection);
        //            serviceConnection = null;
        //        }
    }

    @Override
    public void onDestroy() {
        instance = null;
        HXSVmData.removeDataObserver(this);
        super.onDestroy();
    }

    @Override
    public void vmStateNormal() {
        stopSelf();
    }
}
