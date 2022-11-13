package com.limelight.UserData;


import com.limelight.Infrastructure.common.HXSClientSharedPreference;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.common.HXSUserSharePerference;

public class HXSUserModule {

    private String userName = "";
    private String token = "";
    public String balance;
    public String bonus;


    private static HXSUserModule instance = null;

    public String getUserName() {
        if (userName == null) {
            return "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        loadUserName(userName);
        this.userName = userName;
    }

    public static void clearUserMemory() {
        instance.setUserName("");
        instance.setToken("");
//        instance.saveUserAgreeLeagle(false);
    }

    public void setToken(String token) {
//        loadUserToken(token);
        this.token = token;
    }

    public boolean getIsAgree() {
        return HXSClientSharedPreference.getIsAgreeLeagel(HXSConstant.App);
    }

    public void setIsAgree(boolean isAgree) {
        HXSClientSharedPreference.saveIsAgreeLeagle(isAgree, HXSConstant.App);
    }

    public String getToken() {
        if (token == null) {
            return "";
        }
        return token;
    }

    private HXSUserModule() {

    }

    public static HXSUserModule getInstance() {
        if (instance == null) {
            instance = new HXSUserModule();
        }
        return instance;
    }

    private void saveUserAgreeLeagle(Boolean isAgree) {
        HXSClientSharedPreference.saveIsAgreeLeagle(isAgree, HXSConstant.App);
    }

    private void loadUserToken(String string) {
        HXSUserSharePerference.saveToken(string, HXSConstant.App);
    }

    private String getLocalName() {
        return HXSUserSharePerference.getUserName(HXSConstant.App);
    }

    private String getLocalToken() {
        return HXSUserSharePerference.getToken(HXSConstant.App);
//        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTY5MzUzMDQsImxvZ2lubG9nIjoiNjI5ODEzIiwidGV4dCI6IjE4Mjg3MTAxMDU0IiwidHlwZSI6InVzZXIiLCJ2ZXJzaW9uIjoidjMuMC4xNC0xIn0.YjYRYAADhFM9olFgFIX19vMGShWM8SUbAjm1yl_KpeI";
    }

    /**
     * 保存用户账号
     */
    public void loadUserName(String userName) {
        if (!userName.equals("请输入登录账号")) {
            HXSUserSharePerference.saveUserName(userName, HXSConstant.App);
        }

    }
}
