package com.limelight.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;


import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSCommon;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.UserData.HXSUserModule;
import com.limelight.UserData.HXSVmData;
import com.limelight.computers.ComputerManagerService;
import com.limelight.nvstream.http.ComputerDetails;
import com.limelight.nvstream.http.NvHTTP;
import com.hxstream.operation.HXSConnection;
import com.hxstream.preferences.HXSSettingEntity;
import com.limelight.preferences.PreferenceConfiguration;
import com.limelight.utils.ServerHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HXStreamDelegate {

    private ComputerManagerService.ComputerManagerBinder managerBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, final IBinder binder) {
            managerBinder = ((ComputerManagerService.ComputerManagerBinder) binder);
            managerBinder.doAppList(HXSVmData.getComputerDetails());
        }

        public void onServiceDisconnected(ComponentName className) {
            managerBinder = null;
        }
    };

    private static HXStreamDelegate instance;
    private String VM_UUID;
    private int portOffset;
    private String ip;
    private String ComboURL;
    private String PairURL;
    private Handler handler = new Handler();
    public HXSHttpRequestCenter.IOnRequestCompletionDataReturn pingListener = new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
        @Override
        public void returnAnalysis(String result) {
            pingReturn(result);
        }
    };

    private HXStreamDelegate() {
    }

    public static HXStreamDelegate getInstance() {
        if (instance == null) {
            instance = new HXStreamDelegate();
        }
        return instance;
    }

    public void requestShutdown() {
        if (!"".equals(VM_UUID)) {
            HXSVmData.VM_UUID = VM_UUID;
        }
        HXSHttpRequestCenter.requestShutdownVm(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
            @Override
            public void returnAnalysis(String result) {
                onShutdownReturn(result);
            }
        });
    }
    public void onShutdownReturn(String result){
        JSONObject jsonObject = null;
        HXSLog.info("shutdown返回信息" + result);

        try {
            jsonObject = new JSONObject(result);
            switch (jsonObject.getInt("code")) {
                case 200:
                case 403:
                    HXSVmData.clearVmData();
                    VM_UUID = "";
                    portOffset = 0;
                    ip = "";
                    ComboURL = "";
                    PairURL = "";
                    if (managerBinder!=null){
                        if (HXSConstant.App!=null){
                            HXSConstant.App.unbindService(serviceConnection);
                        }
                    }
                    if (RequestVmService.getInstance()!=null){
                        RequestVmService.getInstance().shutdownServiceConnection();
                        RequestVmService.getInstance().stopSelf();
                    }
                    break;
                case 404:
                case 500:

                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void settingWithEntity(HXSSettingEntity entity) {
        PreferenceConfiguration.saveSettingEntity(entity, HXSConstant.App);
    }

    public HXSSettingEntity getSetting() {
        return PreferenceConfiguration.getSettingEntity();
    }

    public void setChargeType(int type) {
        HXSVmData.chargeType = type;
    }

    public int getChargeType() {
        return HXSVmData.chargeType;
    }

    public void requestSuspend(int minutes) {
        HXSHttpRequestCenter.UpdateVMSuspend(minutes, new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
            @Override
            public void returnAnalysis(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);


                    switch (jsonObject.getInt("code")) {
                        case 200:
                            HXSConnection.getInstance().getCallback().sendMessageToClient(HXSConnection.ClientMessage.MessageSuspendSuccess);
                            break;
                        case 400:
//                            showToast("挂机过程中发生异常，请重试或联系管理员");
                            break;
                        case 404:
                        case 500:
//                            UpdateVMSuspend(SuspendMinutes, this);
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start() {
        if (HXSVmData.isPairRunning) {
            HXSLog.info("stream info:return because request progress running ! -- 建立连接程序正在运行中");
            return;
        }
        if (HXSVmData.getApp() != null) {
            HXSLog.info("stream info:reconnect with existing game ! -- 重新打开内存中的游戏");
            ServerHelper.doStart(HXSConstant.App, HXSVmData.getApp(), HXSVmData.getComputerDetails());
            return;
        }
        if ("".equals(HXSUserModule.getInstance().getToken())) {
            HXSLog.info("stream error:return because token null ! -- 没有token");
            return;
        }
        if("--电信线路--".equals(HXSVmData.LineSelection)){
            Intent intent2 = new Intent(HXSConstant.App, RequestVmService.class);
            intent2.putExtra("region", HXSVmData.LineSelection);
            HXSVmData.setUuid("unknown");
            HXSVmData.ip = "wan212.haixingcloud.com";
            HXSVmData.portOffset = -18000;
            HXSConstant.App.startService(intent2);
            HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateRequesting);
            HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateRequesting);

            return;
        }
        if ("".equals(HXSVmData.LineSelection)) {
            HXSLog.info("stream error:return because region name null ! -- 没有选择线路");
            return;
        }
        Intent intent2 = new Intent(HXSConstant.App, RequestVmService.class);
        intent2.putExtra("region", HXSVmData.LineSelection);
        HXSVmData.setUuid("");
        HXSVmData.ip = "";
        HXSVmData.portOffset = 0;
        HXSConstant.App.startService(intent2);
        HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateRequesting);
        HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateRequesting);

    }

    public void logout() {
        HXSVmData.clearVmData();
    }

    private void pingReturn(String result) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                HXSLog.info("ping返回消息：" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    switch (json.getInt("code")) {
                        case 200:
                            JSONObject resultData = new JSONObject(json.getString("data"));
                            JSONObject balanceData = new JSONObject(resultData.getString("real_time_balance"));
                            HXSVmData.createTime = balanceData.getString("create_at");
                            HXSVmData.spendPoints = balanceData.getString("spend_points");
                            HXSVmData.balance = balanceData.getInt("balance");
                            HXSVmData.setVmMessage(balanceData.getString("message"));
                            HXSConnection.getInstance().getCallback().updateCreateTime(HXSVmData.createTime);
                            break;
                        case 403:
                            //--state--没有权限
                            break;
                        case 404:
                            //--state--没有机器
                            HXSVmData.clearVmData();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void requestVmAlive() {
        if (HXSVmData.connectionStatus.getValue() > 0) {
            //存在正在连接的机器时不检查重连
            return;
        }
        HXSHttpRequestCenter.requestVmSession(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
            @Override
            public void returnAnalysis(String result) {
                returnSession(result);
            }
        });
    }

    private void returnSession(final String result) {

        if (HXSConstant.USER_NOT_AUTHORIZED_STRING.equals(result)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            switch (jsonObject.getInt("code")) {
                case 200:
                    JSONObject resultData = new JSONObject(jsonObject.getString("data"));
                    HXSVmData.session = resultData.getString("session");
                    VM_UUID = resultData.getString("uuid");
                    portOffset = resultData.getInt("port_offset");
                    int comboPort = 50052 + portOffset;
                    ip = resultData.getString("ip");
                    ComboURL = "https://" + ip + ":" + comboPort + "/sendkey";
                    PairURL = "https://" + ip + ":" + comboPort + "/pair";
                    if (!HXSCommon.isStringEmpty(HXSVmData.session)) {
                        //询问用户是否重连
                        if (HXSConnection.getInstance().getCallback() != null) {
                            HXSConnection.getInstance().getCallback().existComputerOnline();
                        }
                    } else if (!HXSCommon.isStringEmpty(resultData.getString("ip"))) {
                        //询问用户是否重连
                        if (HXSConnection.getInstance().getCallback() != null) {
                            HXSConnection.getInstance().getCallback().existComputerOnline();
                        }
                    }
                    break;

                default:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmResume() {
        if (!HXSCommon.isStringEmpty(HXSVmData.session)) {
            HXSVmData.ip = ip;
            resumeWithSession();
        } else if (!HXSCommon.isStringEmpty(ip)) {
            resumeWithIp();
        }
    }

    private void resumeWithIp() {
        Intent intent2 = new Intent(HXSConstant.App, RequestVmService.class);
        intent2.putExtra("region", HXSVmData.LineSelection);
        HXSVmData.setUuid(VM_UUID);
        HXSVmData.ip = ip;
        HXSVmData.portOffset = portOffset;
        HXSConstant.App.startService(intent2);
        HXSConnection.getInstance().getCallback().updateConnectionState(HXSConnection.ConnectionStatus.StateRequesting);
        HXSVmData.setConnectionStatus(HXSConnection.ConnectionStatus.StateRequesting);

        VM_UUID = "";
        portOffset = 0;
        ip = "";
        ComboURL = "";
        PairURL = "";
    }

    private void resumeWithSession() {
        String jsonString = new String(Base64.decode(HXSVmData.session, Base64.NO_WRAP));
        try {
            JSONObject keys = new JSONObject(jsonString);
            String hostname = keys.getString("hostname");
            String computerUUID = keys.getString("uuid");
            String mac = keys.getString("mac");
            String localaddress = keys.getString("localaddress");
            String remoteaddress = keys.getString("remoteaddress");
            String manualaddress = keys.getString("manualaddress");
            String srvcert = keys.getString("srvcert");
            HXSVmData.certString = keys.getString("certString");
            HXSVmData.keyString = keys.getString("keyString");
            String activeAddress = keys.getString("activeAddress");
            HXSVmData.VM_UUID = VM_UUID;
            HXSVmData.portOffset = portOffset;
            HXSVmData.ComboURL = ComboURL;
            HXSVmData.PairURL = PairURL;
            int runningGameId = keys.getInt("runningGameId");
            srvcert = new String(Base64.decode(srvcert, Base64.NO_WRAP));
            X509Certificate serverCert;
            try {
                serverCert = extractPlainCert(srvcert);
                HXSVmData.setComputerDetails(new ComputerDetails(computerUUID, hostname, localaddress, remoteaddress, manualaddress, mac, serverCert, activeAddress, runningGameId));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HXSVmData.isVmRunning = true;
            if (managerBinder != null) {
                managerBinder.doAppList(HXSVmData.getComputerDetails());
            } else {
                HXSConstant.App.bindService(new Intent(HXSConstant.App, ComputerManagerService.class), serviceConnection, Service.BIND_AUTO_CREATE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        VM_UUID = "";
        portOffset = 0;
        ip = "";
        ComboURL = "";
        PairURL = "";

    }

    private static X509Certificate extractPlainCert(String text) throws XmlPullParserException, IOException {
//		这里将String转serverCert
        String certText = NvHTTP.getXmlString(text, "plaincert");
        if (certText != null) {
            byte[] certBytes = hexToBytes(certText);

            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            } catch (CertificateException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void setContext(Context context) {
    }
}
