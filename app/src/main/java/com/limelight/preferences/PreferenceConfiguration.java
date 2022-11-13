package com.limelight.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.Display;

import com.hxstream.preferences.HXSSettingEntity;
import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.R;
import com.limelight.nvstream.jni.MoonBridge;
import com.limelight.utils.DeviceInfo;

public class PreferenceConfiguration {
    public static final String LEGACY_RES_FPS_PREF_STRING = "list_resolution_fps";
    private static final String LEGACY_ENABLE_51_SURROUND_PREF_STRING = "checkbox_51_surround";

    static final String RESOLUTION_PREF_STRING = "list_resolution";
    static final String FPS_PREF_STRING = "list_fps";
    static final String BITRATE_PREF_STRING = "seekbar_bitrate_kbps";
    private static final String BITRATE_PREF_OLD_STRING = "seekbar_bitrate";
    private static final String STRETCH_PREF_STRING = "checkbox_stretch_video";
    private static final String SOPS_PREF_STRING = "checkbox_enable_sops";
    private static final String DISABLE_TOASTS_PREF_STRING = "checkbox_disable_warnings";
    private static final String HOST_AUDIO_PREF_STRING = "checkbox_host_audio";
    private static final String DEADZONE_PREF_STRING = "seekbar_deadzone";
    private static final String OSC_OPACITY_PREF_STRING = "seekbar_osc_opacity";
    private static final String LANGUAGE_PREF_STRING = "list_languages";
    private static final String SMALL_ICONS_PREF_STRING = "checkbox_small_icon_mode";
    private static final String MULTI_CONTROLLER_PREF_STRING = "checkbox_multi_controller";
    static final String AUDIO_CONFIG_PREF_STRING = "list_audio_config";
    private static final String USB_DRIVER_PREF_SRING = "checkbox_usb_driver";
    private static final String VIDEO_FORMAT_PREF_STRING = "video_format";
    private static final String ONSCREEN_CONTROLLER_PREF_STRING = "checkbox_show_onscreen_controls";
    private static final String ONLY_L3_R3_PREF_STRING = "checkbox_only_show_L3R3";
    private static final String DISABLE_FRAME_DROP_PREF_STRING = "checkbox_disable_frame_drop";
    private static final String ENABLE_HDR_PREF_STRING = "checkbox_enable_hdr";
    private static final String ENABLE_PIP_PREF_STRING = "checkbox_enable_pip";
    private static final String ENABLE_PERF_OVERLAY_STRING = "checkbox_enable_perf_overlay";
    private static final String BIND_ALL_USB_STRING = "checkbox_usb_bind_all";
    private static final String MOUSE_EMULATION_STRING = "checkbox_mouse_emulation";
    private static final String MOUSE_NAV_BUTTONS_STRING = "checkbox_mouse_nav_buttons";
    static final String UNLOCK_FPS_STRING = "checkbox_unlock_fps";
    private static final String VIBRATE_OSC_PREF_STRING = "checkbox_vibrate_osc";
    private static final String VIBRATE_FALLBACK_PREF_STRING = "checkbox_vibrate_fallback";
    private static final String FLIP_FACE_BUTTONS_PREF_STRING = "checkbox_flip_face_buttons";
    private static final String TOUCHSCREEN_TRACKPAD_PREF_STRING = "checkbox_touchscreen_trackpad";
    private static final String LATENCY_TOAST_PREF_STRING = "checkbox_enable_post_stream_toast";
    private static final String RESOLUTION_WIDTH_STRING = "resolution_width";
    private static final String RESOLUTION_HEIGHT_STRING = "resolution_height";
    private static final String FRAME_PER_SECOND_STRING = "fps_string";
    private static final String VIDEO_FORMATE_STRING = "video_formate_string";
    private static final String BASIS_SETTING_STRING = "basis_setting";

    private static final String FRAME_PACING_PREF_STRING = "frame_pacing";
    private static final String ABSOLUTE_MOUSE_MODE_PREF_STRING = "checkbox_absolute_mouse_mode";
    private static final String ENABLE_AUDIO_FX_PREF_STRING = "checkbox_enable_audiofx";
    private static final String REDUCE_REFRESH_RATE_PREF_STRING = "checkbox_reduce_refresh_rate";

    static final String DEFAULT_RESOLUTION = "1280x720";
    static final String DEFAULT_FPS = "60";
    private static final boolean DEFAULT_STRETCH = false;
    private static final boolean DEFAULT_SOPS = true;
    private static final boolean DEFAULT_DISABLE_TOASTS = false;
    private static final boolean DEFAULT_HOST_AUDIO = false;
    private static final int DEFAULT_DEADZONE = 15;
    private static final int DEFAULT_OPACITY = 90;
    public static final String DEFAULT_LANGUAGE = "default";
    private static final boolean DEFAULT_MULTI_CONTROLLER = true;
    private static final boolean DEFAULT_USB_DRIVER = true;
    private static final String DEFAULT_VIDEO_FORMAT = "auto";
    private static final boolean ONSCREEN_CONTROLLER_DEFAULT = false;
    private static final boolean ONLY_L3_R3_DEFAULT = false;
    private static final boolean DEFAULT_DISABLE_FRAME_DROP = false;
    private static final boolean DEFAULT_ENABLE_HDR = false;
    private static final boolean DEFAULT_ENABLE_PIP = false;
    private static final boolean DEFAULT_ENABLE_PERF_OVERLAY = false;
    private static final boolean DEFAULT_BIND_ALL_USB = false;
    private static final boolean DEFAULT_MOUSE_EMULATION = true;
    private static final boolean DEFAULT_MOUSE_NAV_BUTTONS = false;
    private static final boolean DEFAULT_UNLOCK_FPS = false;
    private static final boolean DEFAULT_VIBRATE_OSC = true;
    private static final boolean DEFAULT_VIBRATE_FALLBACK = false;
    private static final boolean DEFAULT_FLIP_FACE_BUTTONS = false;
    private static final boolean DEFAULT_TOUCHSCREEN_TRACKPAD = true;
    private static final String DEFAULT_AUDIO_CONFIG = "2"; // Stereo
    private static final boolean DEFAULT_LATENCY_TOAST = false;
    private static final String DEFAULT_FRAME_PACING = "latency";
    private static final boolean DEFAULT_ABSOLUTE_MOUSE_MODE = false;
    private static final boolean DEFAULT_ENABLE_AUDIO_FX = false;
    private static final boolean DEFAULT_REDUCE_REFRESH_RATE = false;

    public static final int FORCE_H265_ON = -1;
    public static final int AUTOSELECT_H265 = 0;
    public static final int FORCE_H265_OFF = 1;

    public static final int FRAME_PACING_MIN_LATENCY = 0;
    public static final int FRAME_PACING_BALANCED = 1;
    public static final int FRAME_PACING_CAP_FPS = 2;
    public static final int FRAME_PACING_MAX_SMOOTHNESS = 3;

    public static final String RES_360P = "640x360";
    public static final String RES_480P = "854x480";
    public static final String RES_720P = "1280x720";
    public static final String RES_1080P = "1920x1080";
    public static final String RES_1440P = "2560x1440";
    public static final String RES_4K = "3840x2160";

    public int width, height, fps;
    public int bitrate;
    public int videoFormat;
    public int deadzonePercentage;
    public int oscOpacity;
    public boolean stretchVideo, enableSops, playHostAudio, disableWarnings;
    public String language;
    public boolean smallIconMode, multiController, usbDriver, flipFaceButtons;
    public boolean onscreenController;
    public boolean onlyL3R3;
    public boolean disableFrameDrop;
    public boolean enableHdr;
    public boolean enablePip;
    public boolean enablePerfOverlay;
    public boolean enableLatencyToast;
    public boolean bindAllUsb;
    public boolean mouseEmulation;
    public boolean mouseNavButtons;
    public boolean unlockFps;
    public boolean vibrateOsc;
    public boolean vibrateFallbackToDevice;
    public boolean touchscreenTrackpad;
    public MoonBridge.AudioConfiguration audioConfiguration;
    public int framePacing;
    public boolean absoluteMouseMode;
    public boolean enableAudioFx;
    public boolean reduceRefreshRate;

    public static boolean isNativeResolution(int width, int height) {
        // It's not a native resolution if it matches an existing resolution option
        if (width == 640 && height == 360) {
            return false;
        }
        else if (width == 854 && height == 480) {
            return false;
        }
        else if (width == 1280 && height == 720) {
            return false;
        }
        else if (width == 1920 && height == 1080) {
            return false;
        }
        else if (width == 2560 && height == 1440) {
            return false;
        }
        else if (width == 3840 && height == 2160) {
            return false;
        }

        return true;
    }

    // If we have a screen that has semi-square dimensions, we may want to change our behavior
    // to allow any orientation and vertical+horizontal resolutions.
    public static boolean isSquarishScreen(int width, int height) {
        float longDim = Math.max(width, height);
        float shortDim = Math.min(width, height);

        // We just put the arbitrary cutoff for a square-ish screen at 1.3
        return longDim / shortDim < 1.3f;
    }

    public static boolean isSquarishScreen(Display display) {
        int width, height;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            width = display.getMode().getPhysicalWidth();
            height = display.getMode().getPhysicalHeight();
        }
        else {
            width = display.getWidth();
            height = display.getHeight();
        }

        return isSquarishScreen(width, height);
    }

    private static String convertFromLegacyResolutionString(String resString) {
        if (resString.equalsIgnoreCase("360p")) {
            return RES_360P;
        } else if (resString.equalsIgnoreCase("480p")) {
            return RES_480P;
        } else if (resString.equalsIgnoreCase("720p")) {
            return RES_720P;
        } else if (resString.equalsIgnoreCase("1080p")) {
            return RES_1080P;
        } else if (resString.equalsIgnoreCase("1440p")) {
            return RES_1440P;
        } else if (resString.equalsIgnoreCase("4K")) {
            return RES_4K;
        } else {
            // Should be unreachable
            return RES_720P;
        }
    }

    private static int getWidthFromResolutionString(String resString) {
        return Integer.parseInt(resString.split("x")[0]);
    }

    private static int getHeightFromResolutionString(String resString) {
        return Integer.parseInt(resString.split("x")[1]);
    }

    private static String getResolutionString(int width, int height) {
        switch (height) {
            case 360:
                return RES_360P;
            case 480:
                return RES_480P;
            default:
            case 720:
                return RES_720P;
            case 1080:
                return RES_1080P;
            case 1440:
                return RES_1440P;
            case 2160:
                return RES_4K;
        }
    }

    public static int getDefaultBitrate(String resString, String fpsString) {
        int width = getWidthFromResolutionString(resString);
        int height = getHeightFromResolutionString(resString);
        int fps = Integer.parseInt(fpsString);

        // This table prefers 16:10 resolutions because they are
        // only slightly more pixels than the 16:9 equivalents, so
        // we don't want to bump those 16:10 resolutions up to the
        // next 16:9 slot.
        //
        // This logic is shamelessly stolen from Moonlight Qt:
        // https://github.com/moonlight-stream/moonlight-qt/blob/master/app/settings/streamingpreferences.cpp

        if (width * height <= 640 * 360) {
            return (int) (1000 * (fps / 30.0));
        } else if (width * height <= 854 * 480) {
            return (int) (1500 * (fps / 30.0));
        }
        // This covers 1280x720 and 1280x800 too
        else if (width * height <= 1366 * 768) {
            return (int) (5000 * (fps / 30.0));
        } else if (width * height <= 1920 * 1200) {
            return (int) (10000 * (fps / 30.0));
        } else if (width * height <= 2560 * 1600) {
            return (int) (20000 * (fps / 30.0));
        } else /* if (width * height <= 3840 * 2160) */ {
            return (int) (40000 * (fps / 30.0));
        }
    }

    public static boolean getDefaultSmallMode(Context context) {
        PackageManager manager = context.getPackageManager();
        if (manager != null) {
            // TVs shouldn't use small mode by default
            if (manager.hasSystemFeature(PackageManager.FEATURE_TELEVISION)) {
                return false;
            }

            // API 21 uses LEANBACK instead of TELEVISION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (manager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)) {
                    return false;
                }
            }
        }

        // Use small mode on anything smaller than a 7" tablet
        return context.getResources().getConfiguration().smallestScreenWidthDp < 500;
    }

    public static int getDefaultBitrate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return getDefaultBitrate(
                prefs.getString(RESOLUTION_PREF_STRING, DEFAULT_RESOLUTION),
                prefs.getString(FPS_PREF_STRING, DEFAULT_FPS));
    }

    private static int getVideoFormatValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String str = prefs.getString(VIDEO_FORMAT_PREF_STRING, DEFAULT_VIDEO_FORMAT);
        if (str.equals("auto")) {
            return AUTOSELECT_H265;
        } else if (str.equals("forceh265")) {
            return FORCE_H265_ON;
        } else if (str.equals("neverh265")) {
            return FORCE_H265_OFF;
        } else {
            // Should never get here
            return AUTOSELECT_H265;
        }
    }

    public static void resetStreamingSettings(Context context) {
        // We consider resolution, FPS, bitrate, HDR, and video format as "streaming settings" here
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit()
                .remove(BITRATE_PREF_STRING)
                .remove(BITRATE_PREF_OLD_STRING)
                .remove(LEGACY_RES_FPS_PREF_STRING)
                .remove(RESOLUTION_PREF_STRING)
                .remove(FPS_PREF_STRING)
                .remove(VIDEO_FORMAT_PREF_STRING)
                .remove(ENABLE_HDR_PREF_STRING)
                .remove(UNLOCK_FPS_STRING)
                .apply();
    }

    public static void saveSettingEntity(HXSSettingEntity entity, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int bitRate, frameRate, width, height, hevc,basisSetting;
        boolean hdr,stretchVideo;

        switch (entity.basisSettings) {
            case BasisSetting_Smooth:
                bitRate = 2500;
                frameRate = 30;
                width = 1280;
                height = 720;
                basisSetting = 1;
                break;
            case BasisSetting_Standard:
                bitRate = 6000;
                frameRate = 60;
                width = 1280;
                height = 720;
                basisSetting = 2;
                break;
            case BasisSetting_HD:
                bitRate = 11000;
                frameRate = 60;
                width = 1920;
                height = 1080;
                basisSetting = 3;
                break;
            case BasisSetting_SHD:
                bitRate = 20000;
                frameRate = 60;
                width = 2560;
                height = 1440;
                basisSetting = 4;
                break;

            case BasisSetting_Advanced:
                bitRate = entity.bitrate;
                frameRate = entity.frameRate;
                width = getWidthFromResolution(entity.resolution);
                height = getHeightFromResolution(entity.resolution);
                basisSetting = -1;
                break;
            case BasisSetting_BHD:
                bitRate = 35000;
                frameRate = 60;
                width = 3840;
                height = 2160;
                basisSetting = 5;
                break;
            default:
                bitRate = entity.bitrate;
                frameRate = entity.frameRate;
                width = getWidthFromResolution(entity.resolution);
                height = getHeightFromResolution(entity.resolution);
                basisSetting = -1;
                break;
        }
        stretchVideo = entity.stretchVideo;
        if (entity.useHevc > 0) {
            hevc = FORCE_H265_OFF;
        } else {
            hevc = FORCE_H265_ON;
        }
        if (entity.enableHdr > 0) {
            hdr = false;
        } else {
            hdr = true;
        }
        prefs.edit()
                .putInt(RESOLUTION_WIDTH_STRING, width)
                .putInt(RESOLUTION_HEIGHT_STRING, height)
                .putInt(FRAME_PER_SECOND_STRING,frameRate)
                .putInt(BITRATE_PREF_STRING,bitRate)
                .putInt(VIDEO_FORMATE_STRING,hevc)
                .putBoolean(ENABLE_HDR_PREF_STRING,hdr)
                .putInt(BASIS_SETTING_STRING,basisSetting)
                .putBoolean(STRETCH_PREF_STRING,stretchVideo)
                .apply();
        //        HXSLog.info("width:"+width+" height:"+height+" framerate:"+frameRate+" bitrate:"+bitRate+" hevc:"+hevc+" hdr:"+hdr + "basissetting:"+basisSetting);

    }
    public static HXSSettingEntity getSettingEntity(){
        if(HXSConstant.App == null){
            HXSLog.info("App Null");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HXSConstant.App);
        HXSSettingEntity entity = HXSSettingEntity.getInstance();
        int basisSetting = prefs.getInt(BASIS_SETTING_STRING,1);
        switch (basisSetting){
            case 1:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_Smooth;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_720P;
                entity.bitrate = 2500;
                entity.frameRate = 30;
                break;
            case 2:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_Standard;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_720P;
                entity.bitrate = 6000;
                entity.frameRate = 60;
                break;
            case 3:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_HD;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_1080P;
                entity.bitrate = 11000;
                entity.frameRate = 60;
                break;
            case 4:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_SHD;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_2K;
                entity.bitrate = 20000;
                entity.frameRate = 60;
                break;
            case 5:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_BHD;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_4K;
                entity.bitrate = 35000;
                entity.frameRate = 60;
                break;
            case -1:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_Advanced;
                int width = prefs.getInt(RESOLUTION_WIDTH_STRING,1280);
                if (width <= 1280){
                    entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_720P;
                }else if ( width <= 1920){
                    entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_1080P;
                }else if (width <= 2560){
                    entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_2K;
                }else {
                    entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_4K;
                }
                entity.bitrate = prefs.getInt(BITRATE_PREF_STRING,2500);
                if (entity.bitrate > 60000){
                    entity.bitrate = 60000;
                }
                entity.frameRate = prefs.getInt(FRAME_PER_SECOND_STRING,30);
                if (entity.frameRate > 144){
                    entity.frameRate = 144;
                }
                break;
            default:
                entity.basisSettings = HXSSettingEntity.BasisSettings.BasisSetting_Smooth;
                entity.resolution = HXSSettingEntity.ResolutionOptions.ResolutionOptions_720P;
                entity.bitrate = 2500;
                entity.frameRate = 30;
                break;
        }
        entity.stretchVideo =prefs.getBoolean(STRETCH_PREF_STRING, DEFAULT_STRETCH);
        entity.useHevc = prefs.getInt(VIDEO_FORMATE_STRING,-1);

        entity.useHevc = -1;
        if (prefs.getBoolean(ENABLE_HDR_PREF_STRING,true)){
            entity.enableHdr = -1;
        }else {
            entity.enableHdr = 1;
        }

        entity.enableHdr = -1;
        HXSLog.info("entity"+entity.toString());
        return entity;
    }

    public static int getWidthFromResolution(HXSSettingEntity.ResolutionOptions option) {
        switch (option) {
            case ResolutionOptions_720P:
                return 1280;
            case ResolutionOptions_1080P:
                return 1920;
            case ResolutionOptions_2K:
                return 2560;
            case ResolutionOptions_4K:
                return 3840;
        }
        return 1280;
    }

    public static int getHeightFromResolution(HXSSettingEntity.ResolutionOptions option) {
        switch (option) {
            case ResolutionOptions_720P:
                return 720;
            case ResolutionOptions_1080P:
                return 1080;
            case ResolutionOptions_2K:
                return 1440;
            case ResolutionOptions_4K:
                return 2160;
        }
        return 720;
    }

    public static boolean isShieldAtvFirmwareWithBrokenHdr() {
        // This particular Shield TV firmware crashes when using HDR
        // https://www.nvidia.com/en-us/geforce/forums/notifications/comment/155192/
        return Build.MANUFACTURER.equalsIgnoreCase("NVIDIA") &&
                Build.FINGERPRINT.contains("PPR1.180610.011/4079208_2235.1395");
    }

    public static PreferenceConfiguration readPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        PreferenceConfiguration config = new PreferenceConfiguration();

        // Migrate legacy preferences to the new locations
        if (prefs.contains(LEGACY_ENABLE_51_SURROUND_PREF_STRING)) {
            if (prefs.getBoolean(LEGACY_ENABLE_51_SURROUND_PREF_STRING, false)) {
                prefs.edit()
                        .remove(LEGACY_ENABLE_51_SURROUND_PREF_STRING)
                        .putString(AUDIO_CONFIG_PREF_STRING, "51")
                        .apply();
            }
        }
        HXSSettingEntity entity = getSettingEntity();
        switch (entity.basisSettings){
            case BasisSetting_Smooth:
                config.bitrate = 2500;
                config.fps = 30;
                config.width = 1280;
                config.height = 720;
                break;
            case BasisSetting_Standard:
                config.bitrate = 6000;
                config.fps = 60;
                config.width = 1280;
                config.height = 720;
                break;
            case BasisSetting_HD:
                config.bitrate = 11000;
                config.fps = 60;
                config.width = 1920;
                config.height = 1080;
                break;
            case BasisSetting_SHD:
                config.bitrate = 20000;
                config.fps = 60;
                config.width = 2560;
                config.height = 1440;
                break;

            case BasisSetting_Advanced:
                config.bitrate = entity.bitrate;
                config.fps = entity.frameRate;
                config.width = getWidthFromResolution(entity.resolution);
                config.height = getHeightFromResolution(entity.resolution);
                break;
            case BasisSetting_BHD:
                config.bitrate = 35000;
                config.fps = 60;
                config.width = 3840;
                config.height = 2160;
                break;
            default:
                config.bitrate = entity.bitrate;
                config.fps = entity.frameRate;
                config.width = getWidthFromResolution(entity.resolution);
                config.height = getHeightFromResolution(entity.resolution);
                break;
        }
        config.height = config.width * DeviceInfo.getDesplay().getHeight() / DeviceInfo.getDesplay().getWidth();
        if (!prefs.contains(SMALL_ICONS_PREF_STRING)) {
            // We need to write small icon mode's default to disk for the settings page to display
            // the current state of the option properly
            prefs.edit().putBoolean(SMALL_ICONS_PREF_STRING, getDefaultSmallMode(context)).apply();
        }



        String audioConfig = prefs.getString(AUDIO_CONFIG_PREF_STRING, DEFAULT_AUDIO_CONFIG);
        if (audioConfig.equals("71")) {
            config.audioConfiguration = MoonBridge.AUDIO_CONFIGURATION_71_SURROUND;
        } else if (audioConfig.equals("51")) {
            config.audioConfiguration = MoonBridge.AUDIO_CONFIGURATION_51_SURROUND;
        } else /* if (audioConfig.equals("2")) */ {
            config.audioConfiguration = MoonBridge.AUDIO_CONFIGURATION_STEREO;
        }


        config.videoFormat = entity.useHevc;

        config.deadzonePercentage = prefs.getInt(DEADZONE_PREF_STRING, DEFAULT_DEADZONE);

        config.oscOpacity = prefs.getInt(OSC_OPACITY_PREF_STRING, DEFAULT_OPACITY);

        config.language = prefs.getString(LANGUAGE_PREF_STRING, DEFAULT_LANGUAGE);

        // Checkbox preferences
        config.disableWarnings = prefs.getBoolean(DISABLE_TOASTS_PREF_STRING, DEFAULT_DISABLE_TOASTS);
        config.enableSops = prefs.getBoolean(SOPS_PREF_STRING, DEFAULT_SOPS);
        config.stretchVideo = prefs.getBoolean(STRETCH_PREF_STRING, DEFAULT_STRETCH);
        config.playHostAudio = prefs.getBoolean(HOST_AUDIO_PREF_STRING, DEFAULT_HOST_AUDIO);
        config.smallIconMode = prefs.getBoolean(SMALL_ICONS_PREF_STRING, getDefaultSmallMode(context));
        config.multiController = prefs.getBoolean(MULTI_CONTROLLER_PREF_STRING, DEFAULT_MULTI_CONTROLLER);
        config.usbDriver = prefs.getBoolean(USB_DRIVER_PREF_SRING, DEFAULT_USB_DRIVER);
        if ("TV".equals(context.getResources().getString(R.string.client_type))){
            config.onscreenController = false;

        }else {
            config.onscreenController = true;

        }
        config.onlyL3R3 = prefs.getBoolean(ONLY_L3_R3_PREF_STRING, ONLY_L3_R3_DEFAULT);
        config.enableHdr = true;
        config.enablePip = prefs.getBoolean(ENABLE_PIP_PREF_STRING, DEFAULT_ENABLE_PIP);
        config.enablePerfOverlay = prefs.getBoolean(ENABLE_PERF_OVERLAY_STRING, DEFAULT_ENABLE_PERF_OVERLAY);
        config.bindAllUsb = prefs.getBoolean(BIND_ALL_USB_STRING, DEFAULT_BIND_ALL_USB);
        config.mouseEmulation = prefs.getBoolean(MOUSE_EMULATION_STRING, DEFAULT_MOUSE_EMULATION);
        config.mouseNavButtons = prefs.getBoolean(MOUSE_NAV_BUTTONS_STRING, DEFAULT_MOUSE_NAV_BUTTONS);
        config.unlockFps = prefs.getBoolean(UNLOCK_FPS_STRING, DEFAULT_UNLOCK_FPS);
        config.vibrateOsc = prefs.getBoolean(VIBRATE_OSC_PREF_STRING, DEFAULT_VIBRATE_OSC);
        config.vibrateFallbackToDevice = prefs.getBoolean(VIBRATE_FALLBACK_PREF_STRING, DEFAULT_VIBRATE_FALLBACK);
        config.flipFaceButtons = prefs.getBoolean(FLIP_FACE_BUTTONS_PREF_STRING, DEFAULT_FLIP_FACE_BUTTONS);
        config.touchscreenTrackpad = prefs.getBoolean(TOUCHSCREEN_TRACKPAD_PREF_STRING, DEFAULT_TOUCHSCREEN_TRACKPAD);
        config.enableLatencyToast = prefs.getBoolean(LATENCY_TOAST_PREF_STRING, DEFAULT_LATENCY_TOAST);
        config.absoluteMouseMode = prefs.getBoolean(ABSOLUTE_MOUSE_MODE_PREF_STRING, DEFAULT_ABSOLUTE_MOUSE_MODE);
        config.enableAudioFx = prefs.getBoolean(ENABLE_AUDIO_FX_PREF_STRING, DEFAULT_ENABLE_AUDIO_FX);
        config.reduceRefreshRate = prefs.getBoolean(REDUCE_REFRESH_RATE_PREF_STRING, DEFAULT_REDUCE_REFRESH_RATE);
        HXSLog.info("分辨率x" + config.width + " y" + config.height + " 帧数:" + config.fps + " 比特率:" + config.bitrate + " 虚拟手柄:" + config.onscreenController + " 硬件解码"
                + config.enableHdr);
        return config;
    }


    public static void completeLanguagePreferenceMigration(Context context) {
        // Put our language option back to default which tells us that we've already migrated it
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(LANGUAGE_PREF_STRING, DEFAULT_LANGUAGE).apply();
    }

}
