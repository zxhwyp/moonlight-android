package com.limelight.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.httpUtils.HXSPostBodyJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HXSControllerPositionPreference {
    public static final String KEY_PAD = "digitalpad";
    public static final String KEY_A = "a";
    public static final String KEY_B = "b";
    public static final String KEY_X = "x";
    public static final String KEY_Y = "y";
    public static final String KEY_LT = "lt";
    public static final String KEY_LB = "rt";
    public static final String KEY_RB = "lb";
    public static final String KEY_RT = "rb";
    public static final String KEY_LS = "ls";
    public static final String KEY_RS = "rs";
    public static final String KEY_BACK = "back";
    public static final String KEY_START = "start";
    public static final String KEY_MOUSE_LEFT = "left";
    public static final String KEY_MOUSE_MIDDLE = "middle";
    public static final String KEY_MOUSE_RIGHT = "right";
    public static final String KEY_INVISIBLE_PAD = "invisible_pad";
    public static final String KEY_LSB = "lsb";
    public static final String KEY_RSB = "rsb";
    public HashMap<String, HXSControllerPosition> positionHashMap;
    private static HXSControllerPositionPreference instance;
    public final String[] positionTitle = {KEY_PAD, KEY_A, KEY_B, KEY_X, KEY_Y, KEY_LT, KEY_LB, KEY_RB, KEY_RT, KEY_LS, KEY_RS, KEY_BACK, KEY_START, KEY_MOUSE_LEFT, KEY_MOUSE_MIDDLE, KEY_MOUSE_RIGHT,KEY_INVISIBLE_PAD,KEY_LSB,KEY_RSB};


    private final static String defaultPositionJson = "{\n" +
            "\"" + KEY_PAD + "\":{\"width\":\"0.2\",\"height\":\"0.53\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_A + "\":{\"width\":\"0.87\",\"height\":\"0.48\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_B + "\":{\"width\":\"0.93\",\"height\":\"0.36\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_X + "\":{\"width\":\"0.81\",\"height\":\"0.36\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_Y + "\":{\"width\":\"0.87\",\"height\":\"0.25\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_LT + "\":{\"width\":\"0.02\",\"height\":\"0.05\",\"size\":\"1.2\",\"visible\":\"true\"},\n" +
            "\"" + KEY_RT + "\":{\"width\":\"0.9\",\"height\":\"0.05\",\"size\":\"1.2\",\"visible\":\"true\"},\n" +
            "\"" + KEY_LB + "\":{\"width\":\"0.07\",\"height\":\"0.14\",\"size\":\"1.2\",\"visible\":\"true\"},\n" +
            "\"" + KEY_RB + "\":{\"width\":\"0.86\",\"height\":\"0.14\",\"size\":\"1.2\",\"visible\":\"true\"},\n" +
            "\"" + KEY_LS + "\":{\"width\":\"0.04\",\"height\":\"0.67\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_RS + "\":{\"width\":\"0.75\",\"height\":\"0.67\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_BACK + "\":{\"width\":\"0.4\",\"height\":\"0.03\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_START + "\":{\"width\":\"0.5\",\"height\":\"0.03\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_MOUSE_LEFT + "\":{\"width\":\"0.4\",\"height\":\"0.12\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_MOUSE_MIDDLE + "\":{\"width\":\"0.5\",\"height\":\"0.12\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_MOUSE_RIGHT + "\":{\"width\":\"0.6\",\"height\":\"0.12\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_INVISIBLE_PAD + "\":{\"width\":\"0\",\"height\":\"0\",\"size\":\"1\",\"visible\":\"false\"},\n" +
            "\"" + KEY_LSB + "\":{\"width\":\"0.3\",\"height\":\"0.12\",\"size\":\"1\",\"visible\":\"true\"},\n" +
            "\"" + KEY_RSB + "\":{\"width\":\"0.7\",\"height\":\"0.12\",\"size\":\"1\",\"visible\":\"true\"}\n" +
            "}";

    public static HXSControllerPositionPreference getInstance() {
        if (instance == null) {
            instance = new HXSControllerPositionPreference();
        }
        return instance;
    }

    private HXSControllerPositionPreference() {
        positionHashMap = new HashMap<>();
        try {
            JSONObject positions = new JSONObject(getPositions(HXSConstant.App));
            for (String s : positionTitle) {
                positionHashMap.put(s, new HXSControllerPosition(positions.getJSONObject(s)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resetDefaultPositions() {
        positionHashMap.clear();
        try {
            JSONObject positions = new JSONObject(defaultPositionJson);
            for (String s : positionTitle) {
                positionHashMap.put(s, new HXSControllerPosition(positions.getJSONObject(s)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void savePositions() {
        HXSPostBodyJson json = new HXSPostBodyJson();
        for (String s : positionTitle) {
            json.putBody(s, positionHashMap.get(s).getJson());
        }
        String positionString = json.getPostJson();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HXSConstant.App);
        prefs.edit()
                .putString("controller_position", positionString)
                .apply();
    }

    public String getPositions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String position =  prefs.getString("controller_position", defaultPositionJson);
        if (!position.contains(KEY_INVISIBLE_PAD)){
            return defaultPositionJson;
        }else {
            return position;
        }
    }
}
