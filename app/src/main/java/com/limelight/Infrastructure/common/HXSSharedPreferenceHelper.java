package com.limelight.Infrastructure.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 *     project : starvictory-client-android
 *     author  : LH
 *     e-mail  : lh453123885@gmail.com
 *     time    : 2020/01/07
 *     作用    :
 *     version : 1.0
 * </pre>
 */
public class HXSSharedPreferenceHelper {


    public static boolean saveSPString(String tableName, String keyName, String text, Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, text);
        return editor.commit();
    }

    public static boolean saveBoolean(String tableName, String keyName, boolean argBoolean, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyName, argBoolean);
        return editor.commit();
    }

    public static boolean saveFloat(String tableName, String keyName, float argDouble, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(keyName, argDouble);
        return editor.commit();
    }

    public static String getSPString(String tableName, String keyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyName, "");
    }

    public static boolean getBoolean(String tableName, String keyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(keyName, false);
    }

    public static float getFloat(String tableName, String keyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(keyName, 0);
    }

    public static boolean saveInt(String tableName, String keyName, int argInt, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(keyName, argInt);
        return editor.commit();
    }

    public static int getInt(String tableName, String keyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(keyName, 0);
    }

    public static boolean clearMemory(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor.clear().commit();
    }


}
