package com.limelight.preferences;

public class HXStreamViewPreference {
    private static final String TABLE_STREAM = "stream";
    private static final String SEEK_ALPHA = "seek_alpha";
    private static final String SEEK_SPEED = "seek_speed";
    private static final String SWITCH_CONTROLLER_VISIBLE = "visible";
    private static final String MOUSE_VISIBLE = "mouse_visible";
    private static final String TOUCH_TYPE = "touchType";
    private static final String USE_SOURCE_KEYBOARD = "use_source_keyboard";
    private static final String TOUCH_PAD_OPEN = "touch_pad_open";

    public static void setSeekAlpha(int alpha) {
        HXSPreferenceUtil.saveInt(TABLE_STREAM, SEEK_ALPHA, alpha);
    }

    public static int getSeekAlpha() {
        int alpha = HXSPreferenceUtil.getInt(TABLE_STREAM, SEEK_ALPHA);
        if (alpha == -1) {
            return 50;
        }
        if (alpha < 20) {
            return 20;
        }
        if (alpha > 50) {
            return 50;
        }
        return alpha;
    }

    public static void setSeekSpeed(int speed) {
        HXSPreferenceUtil.saveInt(TABLE_STREAM, SEEK_SPEED, speed);
    }

    public static int getSeekSpeed() {
        int speed = HXSPreferenceUtil.getInt(TABLE_STREAM, SEEK_SPEED);
        if (speed < 1) {
            return 10;
        }
        if (speed > 25) {
            return 25;
        }
        return speed;
    }

    public static void setSwitchControllerVisible(boolean visible) {
        HXSPreferenceUtil.saveBoolean(TABLE_STREAM, SWITCH_CONTROLLER_VISIBLE, visible);
    }

    public static boolean getSwitchControllerVisible() {
        return HXSPreferenceUtil.getBooleanTrue(TABLE_STREAM, SWITCH_CONTROLLER_VISIBLE);
    }

    public static void setMouseVisible(boolean visible) {
        HXSPreferenceUtil.saveBoolean(TABLE_STREAM, MOUSE_VISIBLE, visible);
    }

    public static boolean getMouseVisible() {
        return HXSPreferenceUtil.getBooleanTrue(TABLE_STREAM, MOUSE_VISIBLE);
    }

    public static void saveTouchType(int touchType) {
        HXSPreferenceUtil.saveInt(TABLE_STREAM, TOUCH_TYPE, touchType);
    }

    public static int getTouchType() {
        int type = HXSPreferenceUtil.getInt(TABLE_STREAM, TOUCH_TYPE);
        return type >= 0 && type <= 2 ? type : 0;

    }

    public static boolean getUseSourceKeyboard() {
        return HXSPreferenceUtil.getBoolean(TABLE_STREAM, USE_SOURCE_KEYBOARD);
    }

    public static void setUseSourceKeyboard(boolean b) {
        HXSPreferenceUtil.saveBoolean(TABLE_STREAM, USE_SOURCE_KEYBOARD, b);
    }
    public static boolean getTouchPadOpen() {
        return HXSPreferenceUtil.getBoolean(TABLE_STREAM, TOUCH_PAD_OPEN);
    }

    public static void setTouchPadOpen(boolean b) {
        HXSPreferenceUtil.saveBoolean(TABLE_STREAM, TOUCH_PAD_OPEN, b);
    }
}
