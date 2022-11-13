package com.limelight.UserData;


import com.limelight.Infrastructure.common.HXSCommon;
import com.limelight.computers.ComputerManagerService;
import com.limelight.nvstream.PingThread;
import com.limelight.nvstream.http.ComputerDetails;
import com.limelight.nvstream.http.NvApp;
import com.hxstream.operation.GameOption;
import com.hxstream.operation.HXSConnection;

import java.util.ArrayList;

public class HXSVmData {

    private static ArrayList<IVmDataObserver> vmDataObservers = new ArrayList<>();
    public static String version = "unknown";
    public static String VM_UUID = "";
    public static String LineSelection = "";
    public static int portOffset = 0;
    public static boolean isVmRunning = false;
    public static String ComboURL = "";
    public static String PairURL = "";
    public static String ip = "";
    private static ComputerDetails computerDetails = null;
    private static NvApp app = null;
    public static boolean isComputerConnecting = false;
    private static String uuid;
    public static ComputerState state = ComputerState.NOT_DISTRIBUTION;
    private static String uniqueId;
    private static String vmMessage;
    public static String createTime;
    public static String spendPoints;
    public static int balance;
    public static boolean isPairRunning = false;
    public static int chargeType = 0;

    public static String certString = "";
    public static String keyString = "";
    public static String session = "";

    public static String userName = "";
    public static String region = "";

    public static GameOption option = null;

    public static HXSConnection.ConnectionStatus connectionStatus= HXSConnection.ConnectionStatus.StateNormal;

    public static ArrayList<IVmMessageListener> observers = new ArrayList<>();

    public static void setConnectionStatus(HXSConnection.ConnectionStatus connectionStatus) {
        HXSVmData.connectionStatus = connectionStatus;
        if (connectionStatus == HXSConnection.ConnectionStatus.StateStreamRunning){
            for (IVmDataObserver observer:vmDataObservers){
                if (observer!=null){
                    observer.vmStateNormal();
                }
            }
        }
        if (connectionStatus == HXSConnection.ConnectionStatus.StateConnectFail){
            clearVmData();
            HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageNetworkError);
            HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateNormal);
            setConnectionStatus(HXSConnection.ConnectionStatus.StateNormal);
        }
    }

    public static String getVmMessage() {
        return vmMessage;
    }

    public static void setVmMessage(String vmMessage) {
        if (!HXSCommon.isStringEmpty(vmMessage)) {
            for (IVmMessageListener listener : observers) {
                if (listener != null) {
                    listener.onReceivedMessage(vmMessage);
                }
            }
        } else if (HXSCommon.isStringEmpty(vmMessage) && !HXSCommon.isStringEmpty(HXSVmData.vmMessage)) {
            for (IVmMessageListener listener : observers) {
                if (listener != null) {
                    listener.onMessageDismiss();
                }
            }
        }
        HXSVmData.vmMessage = vmMessage;
    }

    public static String getUniqueId() {
        return uniqueId;
    }

    public static void setUniqueId(String uniqueId) {
        HXSVmData.uniqueId = uniqueId;
    }

    public static void clearVmData() {
        VM_UUID = "";
        portOffset = 0;
        isVmRunning = false;
        ComboURL = "";
        PairURL = "";
        ip = "";
        computerDetails = null;
        app = null;
        isComputerConnecting = false;
        uuid = "";
        state = ComputerState.NOT_DISTRIBUTION;
        HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateNormal);
        setConnectionStatus(HXSConnection.ConnectionStatus.StateNormal);
        ComputerManagerService.BooleanResume= false;
        option = null;
        PingThread.onVmClear();
    }

    public enum ComputerState {
        NOT_PAIRED, PAIRED, NOT_DISTRIBUTION, CONNECTED
    }

    public static ComputerDetails getComputerDetails() {
        return computerDetails;
    }

    public static void setComputerDetails(ComputerDetails computerDetails) {
        HXSVmData.computerDetails = computerDetails;
        refershState();
    }

    public static NvApp getApp() {
        return app;
    }

    public static void setApp(NvApp app) {
        HXSVmData.app = app;
        refershState();
    }

    public static String getUuid() {
        return uuid;
    }


    public static void setUuid(String uuid) {
        HXSVmData.uuid = uuid;
        refershState();
    }

    private static void refershState() {
        if (computerDetails != null && app == null) {
            state = ComputerState.PAIRED;
        } else if (computerDetails != null) {
            state = ComputerState.CONNECTED;
        } else if (uuid.equals("")) {
            state = ComputerState.NOT_DISTRIBUTION;
        } else {
            state = ComputerState.NOT_PAIRED;
        }
    }
    public static void addVmDataObserver(IVmDataObserver observer){
        vmDataObservers.add(observer);
    }
    public static void removeDataObserver(IVmDataObserver observer){
        vmDataObservers.remove(observer);
    }

    public interface IVmMessageListener {
        void onReceivedMessage(String message);

        void onMessageDismiss();
    }
    public interface IVmDataObserver{
        public void vmStateNormal();
    }

}
