package com.limelight.Infrastructure.common;

import android.content.Context;

/**
 * <pre>
 *     project : starvictory-client-android
 *     author  : LH
 *     e-mail  : lh453123885@gmail.com
 *     time    : 2020/01/14
 *     作用    :
 *     version : 1.0
 * </pre>
 */
public class HXSClientSharedPreference {
    private static final String NAME_CLIENT = "star_victory_client";

    private static final String KEY_AGREE_LEAGAL = "client_leagle";
    private static final String KEY_LINE_CHOOSE = "client_line_choose";


    public static void saveIsAgreeLeagle(boolean isAgree, Context context) {
        HXSSharedPreferenceHelper.saveBoolean(NAME_CLIENT, KEY_AGREE_LEAGAL, isAgree, context);
    }

    public static boolean getIsAgreeLeagel(Context context) {
        return HXSSharedPreferenceHelper.getBoolean(NAME_CLIENT, KEY_AGREE_LEAGAL, context);
    }


    public static void saveLineChoose(String valueLineName, Context context) {
        HXSSharedPreferenceHelper.saveSPString(NAME_CLIENT, KEY_LINE_CHOOSE, valueLineName, context);
    }

    public static String getLineChoose(Context context) {
        return HXSSharedPreferenceHelper.getSPString(NAME_CLIENT, KEY_LINE_CHOOSE, context);
    }

    public static void savePingDelay(String host, int delay, Context context) {
        HXSSharedPreferenceHelper.saveInt(NAME_CLIENT, host, delay, context);
    }

    public static int getPingDelay(String host, Context context) {
        return HXSSharedPreferenceHelper.getInt(NAME_CLIENT, host, context);
    }
}
