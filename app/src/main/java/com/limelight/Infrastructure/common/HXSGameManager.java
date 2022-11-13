package com.limelight.Infrastructure.common;

import com.limelight.HXSLog;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.module.CmdData;
import com.limelight.utils.DeviceInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhb5145 on 2020/12/28
 */
public class HXSGameManager {
    public static final int GAME_ERR_CODE_SUCCESS = 0;
    public static final int GAME_ERR_CODE_ERROR = 1;
    public static final int GAME_ERR_CODE_UNKNOWN = -1;

    public static int startSteamGameWithSelfAccount(String steamAccount,
                                                    String steamPassword, String sign) {
        int errCode = GAME_ERR_CODE_UNKNOWN;
        HXSHttpRequestCenter.requestSendCmd(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
            @Override
            public void returnAnalysis(String result) {
                HXSLog.info("cmd返回内容" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("".equals(object.getString("code"))) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new CmdData(false, "e:\\\\steam\\\\steam.exe", new ArrayList<String>() {{
            add("-login");
            add(steamAccount);
            add(steamPassword);
            add("-nofriendsui");
            add("-noasync");
            add("-nocache");
        }}, "gbk", sign));

        return errCode;
    }

    public static void autoResolution() {
        int width = DeviceInfo.getDesplay().getWidth() ;
        int height = DeviceInfo.getDesplay().getHeight();
        if (width < height){
            int temp = width;
            width = height;
            height = temp;
        }
        String frameRate = "60";
        HXSHttpRequestCenter.requestSendResolution(width+"", height+"", frameRate);
    }

    public static int startSteamGame(String sign) {
        HXSHttpRequestCenter.autoClick("0.5427", "0.513889", "Steam - 下载已禁用");
        return 1;
    }

    public static int killSteamProcess(String sign) {
        int errCode = GAME_ERR_CODE_UNKNOWN;
        HXSHttpRequestCenter.requestSendCmd(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
            @Override
            public void returnAnalysis(String result) {
                HXSLog.info("cmd返回内容" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("".equals(object.getString("code"))) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new CmdData(true, "taskkill", new ArrayList<String>() {{
            add("/IM");
            add("steam.exe");
            add("/F");
        }}, "gbk", sign));

        return errCode;
    }
}
