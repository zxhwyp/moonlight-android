package com.limelight.nvstream;


import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.httpUtils.HXSOkHttpClient;
import com.limelight.Infrastructure.httpUtils.HXSPostBodyJson;
import com.limelight.UserData.HXSUserModule;
import com.limelight.UserData.HXSVmData;
import com.limelight.controller.HXStreamDelegate;

import java.io.IOException;

public class PingThread extends Thread {
    private static boolean pingRunning = false;
    private static PingThread instance = null;
    public static PingThread getInstance(){
        if (instance == null){
            instance = new PingThread();
        }
        return instance;
    }
    public static void onVmClear(){
        pingRunning = false;
        instance = null;
    }
    private PingThread() {

        super(new Runnable() {
            @Override
            public void run() {
                pingRunning = true;
                while (HXSVmData.isVmRunning) {
                    HXSPostBodyJson json = new HXSPostBodyJson();
                    HXSOkHttpClient httpClient = new HXSOkHttpClient();
                    try {
                        final String result = httpClient.post(HXSConstant.VmsURL + HXSVmData.VM_UUID + "/ping", json.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());

                                HXStreamDelegate.getInstance().pingListener.returnAnalysis(result);

                        sleep(HXSConstant.PING_DELAY);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pingRunning = false;
                instance = null;
            }
        });
    }

    @Override
    public synchronized void start() {
        if (pingRunning)
            return;
        super.start();
    }
}
