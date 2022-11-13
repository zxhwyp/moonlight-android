package com.hxstream.operation;

import android.app.Application;

import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.UserData.HXSUserModule;
import com.limelight.UserData.HXSVmData;
import com.limelight.controller.HXStreamDelegate;
import com.hxstream.preferences.HXSSettingEntity;
import com.limelight.preferences.HXStreamViewPreference;
import com.limelight.utils.DeviceInfo;


/**
 *
 */
public class HXSConnection {

    private static HXSConnection instance = null;
    private IStreamConnectionCallback callback = null;

    private HXSConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DeviceInfo.netIp = DeviceInfo.GetNetIp();
                HXSLog.info("HXSConnection  " + DeviceInfo.netIp);
            }
        }).start();

    }

    public static HXSConnection getInstance() {
        if (instance == null) {
            instance = new HXSConnection();
        }
        return instance;
    }

    public static enum ConnectionStatus {
        StateNormal(0),//没有正在连接的机器和正在运行的请求进程
        StateRequesting(1),//请求进程正在运行
        StatePending(2),//正在排队
        StateConnecting(3),//排队成功正在连接
        StateConnectSuccess(4),//连接成功
        StateConnectFail(5),//连接失败
        StateStreamRunning(6),//串流画面正在运行
        StateStreamDismiss(7);//串流画面退出，可重连
        private final int value;

        ConnectionStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public static enum ClientMessage {
        MessageSuspendSuccess(0),//挂机成功
        MessageUserBanned(1),//用户被禁
        MessageLackBalance(2),//用户余额不足
        MessageConnectionRunning(3),//存在正在运行游戏
        MessageServerError(4),//服务端错误请重新选择线路重试
        MessageNetworkError(5);//本地网络错误，请检查网络
        private final int value;

        ClientMessage(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void setConnectionCallback(IStreamConnectionCallback callback) {
        this.callback = callback;
    }

    /**
     * 启动云电脑等操作需要提前设置token  串流依赖包有本地存储 切换账号等行为一定要重置token
     *
     * @param token
     */
    public void setUserToken(String token) {
        HXSUserModule.getInstance().setToken(token);
    }

    /**
     * 传入application 作为context
     *
     * @param application
     */
    public void setApplication(Application application) {
        HXSConstant.App = application;
        HXStreamViewPreference.setTouchPadOpen(false);
    }


    /**
     * 用户点击启动云电脑按钮
     */
    public void startButtonClicked() {
        HXStreamDelegate.getInstance().start();
    }

    /**
     * 设置打开游戏进阶选项
     *
     * @param option
     */
    public void setGameOption(GameOption option) {
        HXSVmData.option = option;
    }

    public void saveSettingEntity(HXSSettingEntity entity) {
        HXStreamDelegate.getInstance().settingWithEntity(entity);
//        HXSHttpRequestCenter.requestSendCmd(new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
//            @Override
//            public void returnAnalysis(String result) {
//                HXSLog.info("cmd返回内容" + result);
//            }
//        }, new CmdData(false, "e:\\\\steam\\\\steam.exe", new ArrayList<String>() {{
//            add("-login");
//            add("shenwei90xb");
//            add("90houxb+gg");
//            add("-applaunch");
//            add("632360");
//        }},"gbk","hello"));
    }

    public HXSSettingEntity getSettingEntity() {
        return HXStreamDelegate.getInstance().getSetting();
    }

    /**
     * 选择挂机
     *
     * @param minutes 分钟  int
     */
    public void chooseSuspend(int minutes) {
        HXStreamDelegate.getInstance().requestSuspend(minutes);
    }

    /**
     * 下机
     */
    public void shutDownConnection() {
        HXStreamDelegate.getInstance().requestShutdown();
    }


    /**
     * 查询是否有在线的机器
     */
    public void requestConnectionAlive() {
        HXStreamDelegate.getInstance().requestVmAlive();
    }

    /**
     * 确认重连
     */
    public void confirmeResume() {
        HXStreamDelegate.getInstance().confirmResume();
    }

    /**
     * 设置选择线路
     *
     * @param region 线路字符串
     */
    public void setRegionSelected(String region) {
        HXSVmData.LineSelection = region;
    }

    public void setUserInfo(String regionId, String userName){
        HXSVmData.region = regionId;
        HXSVmData.userName = userName;
    }

    /**
     * 设置消费类型
     *
     * @param chargeType
     */
    public void setChargeType(int chargeType) {
        HXStreamDelegate.getInstance().setChargeType(chargeType);
        HXSLog.info("chargeType " + chargeType);
    }

    /**
     * 获取消费类型
     *
     * @return
     */
    public int getChargeType() {
        return HXStreamDelegate.getInstance().getChargeType();
    }

    /**
     *  设置版本号
     */
    public void setVersion(String version){
        HXSVmData.version = version;
    }


    /**
     * 前端退出登录时调用
     */
    public void onUserLogout() {
        HXStreamDelegate.getInstance().logout();
    }

    public IStreamConnectionCallback getCallback() {
        return callback;
    }


    public interface IStreamConnectionCallback {
        public void existComputerOnline();

        public void updateConnectionState(ConnectionStatus state);

        public void sendMessageToClient(ClientMessage message);

        public void onStreamActivityAppear();

        public void onStreamActivityDissappear();

        public void onShutdownClicked();

        public void updateOrder(int order);

        public void updateCreateTime(String timeStamp);
    }

}
