package com.hxstream.preferences;

public class HXSSettingEntity {

    private static HXSSettingEntity instance;
    private HXSSettingEntity(){

    }
    public static HXSSettingEntity getInstance(){
        if (instance==null){
            instance = new HXSSettingEntity();
        }
        return instance;
    }
    public static enum ResolutionOptions{
        ResolutionOptions_720P(0),
        ResolutionOptions_1080P(1),
        ResolutionOptions_2K(2),
        ResolutionOptions_4K(3);
        private final int value;
        private ResolutionOptions(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }
    public static enum BasisSettings{
        BasisSetting_Smooth(1),//流畅画质
        BasisSetting_Standard(2),//标准画质
        BasisSetting_HD(3),//高清画质
        BasisSetting_SHD(4),//超清画质
        BasisSetting_BHD(5),//蓝光画质
        BasisSetting_Advanced(-1);//进阶设置
        private final int value;
        private BasisSettings(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }
    public ResolutionOptions resolution;
    public int frameRate;
    public int bitrate;
    public int onScreenControls;
    public int useHevc;
    public int enableHdr;
    public BasisSettings basisSettings;
    public boolean stretchVideo = false;

    @Override
    public String toString() {
        return "HXSSettingEntity{" +
                "resolution=" + resolution +
                ", frameRate=" + frameRate +
                ", bitrate=" + bitrate +
                ", onScreenControls=" + onScreenControls +
                ", useHevc=" + useHevc +
                ", enableHdr=" + enableHdr +
                ", basisSettings=" + basisSettings +
                '}';
    }
}
