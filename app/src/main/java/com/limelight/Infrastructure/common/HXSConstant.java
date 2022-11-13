package com.limelight.Infrastructure.common;

import android.app.Application;

public class HXSConstant {
    private static String URL = "https://www.haixingcloud.com:8000";//正式环境服务器
    //    private static String URL = "http://www1.haixingcloud.com:8000";
//        private static String URL = "http://idc-tj-cmcc.haixingcloud.com:8000";//测试环境服务器
    //    private static String URL = "http://60.205.206.26:8000";//测试环境服务器
    public static String LoginURI = URL + "/api/login";
    public static String UpdateToken = URL + "/api/users/token";
    public static String RegisteredURI = URL + "/api/users";
    public static String ResetPasswordURI = URL + "/api/users/password_reset";
    public static String RegionsURL = URL + "/api/regions";
    public static String ApplyVMURL = URL + "/api/vms/applyvm";
    public static String VmsURL = URL + "/api/vms/";
    public static String ClientVersion = URL + "/api/clientversion?type=1";
    //    public static String Captcha = URL + "/app/captcha";
    public static String Captcha = URL + "/app/captcha";
    public static String PostMessage = URL + "/api/clientlog";
    public static String PayURL = URL + "/api/pay";
    public static String Product = URL + "/api/product/product_list";
    public static String TimeCard = URL + "/api/timecard";
    public static String BindWechat = URL + "/api/users/bind_wechat";
    public static String Verification = URL + "/api/users/verification_code";
    public static String WechatLogin = URL + "/api/sns/callback";
    public static String Feedback = URL +"/api/feedback";

    public static Application App = null;
    public static int PING_DELAY = 10000;
    public static final String USER_NOT_AUTHORIZED_STRING = "User not authorized";

}
