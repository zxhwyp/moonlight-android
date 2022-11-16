package com.limelight.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.limelight.Infrastructure.common.HXSConstant;

public class HXSPreferenceUtil {
    public static boolean saveSPString(String tableName, String keyName, String text) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, text);
        return editor.commit();
    }

    public static boolean saveBoolean(String tableName, String keyName, boolean argBoolean) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyName, argBoolean);
        return editor.commit();
    }

    public static boolean saveFloat(String tableName, String keyName, float argDouble) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(keyName, argDouble);
        return editor.commit();
    }

    public static String getSPString(String tableName, String keyName) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyName, "");
    }

    public static boolean getBoolean(String tableName, String keyName) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(keyName, false);
    }
    public static boolean getBooleanTrue(String tableName, String keyName) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(keyName, false);
    }

    public static float getFloat(String tableName, String keyName) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(keyName, 0);
    }

    public static boolean saveInt(String tableName, String keyName, int argInt) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(keyName, argInt);
        return editor.commit();
    }

    public static int getInt(String tableName, String keyName) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(tableName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(keyName, -1);
    }

    public static boolean clearMemory(String name) {
        SharedPreferences sharedPreferences = HXSConstant.App
                .getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor.clear().commit();
    }
}
