package com.limelight.Infrastructure.common;

import android.content.Context;

public class HXSUserSharePerference {
    private static final String NAME_USER = "star_victory_user";

    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_NAME = "user_name";


    /**
     * 保存text
     *
     * @param text
     * @return 成功
     */
    private static boolean saveText(String tableName, String text, Context context) {
        return HXSSharedPreferenceHelper.saveSPString(NAME_USER, tableName, text, context);
    }

    private static boolean saveBoolean(String tableName, boolean argBoolean, Context context) {
        return HXSSharedPreferenceHelper.saveBoolean(NAME_USER, tableName, argBoolean, context);
    }

    /**
     * 加载用户名
     *
     * @return String text
     */
    private static String getText(String keyName, Context context) {
        return HXSSharedPreferenceHelper.getSPString(NAME_USER, keyName, context);
    }

    private static boolean getBoolean(String keyName, Context context) {
        return HXSSharedPreferenceHelper.getBoolean(NAME_USER, keyName, context);
    }

    private static boolean clearMemory(String name, Context context) {
        return HXSSharedPreferenceHelper.clearMemory(NAME_USER, context);
    }

    public static boolean clearAllMemory(Context context) {
        if (!clearMemory(NAME_USER, context)) {
            return false;
        }
        return true;
    }

    /**
     * 保存用户名
     *
     * @param userName
     * @return 成功？
     */
    public static boolean saveUserName(String userName, Context context) {
        return saveText(KEY_USER_NAME, userName, context);
    }

    /**
     * 获取保存的用户名
     *
     * @return username
     */
    public static String getUserName(Context context) {
        return getText(KEY_USER_NAME, context);
    }

    /**
     * 保存token
     *
     * @param token
     * @return 成功？
     */
    public static boolean saveToken(String token, Context context) {
        return saveText(KEY_USER_TOKEN, token, context);
    }

    public static String getToken(Context context) {
        return getText(KEY_USER_TOKEN, context);
    }
}
