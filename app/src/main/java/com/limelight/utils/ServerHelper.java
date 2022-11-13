package com.limelight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.limelight.AppView;
import com.limelight.Game;
import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.Infrastructure.httpUtils.HXSPostBodyJson;
import com.limelight.PcView;
import com.limelight.R;
import com.limelight.ShortcutTrampoline;
import com.limelight.UserData.HXSVmData;
import com.limelight.binding.PlatformBinding;
import com.limelight.computers.ComputerManagerService;
import com.limelight.controller.HXStreamDelegate;
import com.limelight.nvstream.http.ComputerDetails;
import com.limelight.nvstream.http.GfeHttpResponseException;
import com.limelight.nvstream.http.NvApp;
import com.limelight.nvstream.http.NvHTTP;
import com.limelight.nvstream.jni.MoonBridge;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.cert.CertificateEncodingException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.limelight.UserData.HXSVmData.certString;
import static com.limelight.UserData.HXSVmData.keyString;

public class ServerHelper {
    public static final String CONNECTION_TEST_SERVER = "android.conntest.moonlight-stream.org";

    public static String getCurrentAddressFromComputer(ComputerDetails computer) throws IOException {
        return computer.activeAddress;
    }

    public static Intent createPcShortcutIntent(Activity parent, ComputerDetails computer) {
        Intent i = new Intent(parent, ShortcutTrampoline.class);
        i.putExtra(AppView.NAME_EXTRA, computer.name);
        i.putExtra(AppView.UUID_EXTRA, computer.uuid);
        i.setAction(Intent.ACTION_DEFAULT);
        return i;
    }

    private static void returnUpdateSession(Object object) {
        try {
            HXSLog.info("保存重连返回信息" + object.toString());
            if (HXSConstant.USER_NOT_AUTHORIZED_STRING.equals(object.toString())) {
                //                StarVictoryApp.getInstance().onUserTokenInvalid();
                return;
            }
            JSONObject result = new JSONObject(object.toString());
            switch (result.getInt("code")) {
                case 200:

                    break;
                case 400:
                    HXSLog.info("保存重连请求错误：您提交的内容错误");
                    break;
                case 404:
                    HXSLog.info("保存重连请求错误：您提交的机器信息不存在");
                    break;
                case 500:
                    HXSLog.info("保存重连请求错误：服务器繁忙，请稍后重试");
                    break;
            }
        } catch (Exception e) {
            Log.d("updateVMSession", e.toString());
        }
    }

    public static Intent createAppShortcutIntent(Activity parent, ComputerDetails computer, NvApp app) {
        Intent i = new Intent(parent, ShortcutTrampoline.class);
        i.putExtra(AppView.NAME_EXTRA, computer.name);
        i.putExtra(AppView.UUID_EXTRA, computer.uuid);
        i.putExtra(Game.EXTRA_APP_NAME, app.getAppName());
        i.putExtra(Game.EXTRA_APP_ID, "" + app.getAppId());
        i.putExtra(Game.EXTRA_APP_HDR, app.isHdrSupported());
        i.setAction(Intent.ACTION_DEFAULT);
        return i;
    }

    public static Intent createStartIntent(Context parent, NvApp app, ComputerDetails computer,
                                           ComputerManagerService.ComputerManagerBinder managerBinder) {

        if (!ComputerManagerService.BooleanResume) {
            HXSPostBodyJson json1 = new HXSPostBodyJson();
            json1.putBody("uuid", computer.uuid);
            json1.putBody("hostname", computer.name);
            json1.putBody("mac", computer.macAddress);
            json1.putBody("localaddress", computer.localAddress);
            json1.putBody("remoteaddress", computer.remoteAddress);
            json1.putBody("manualaddress", computer.manualAddress);
            json1.putBody("activeAddress", computer.activeAddress);
            json1.putBody("runningGameId", computer.runningGameId);
            json1.putBody("certString", certString);
            json1.putBody("keyString", keyString);
            String serverCertBase64 = Base64.encodeToString(ComputerManagerService.serverCertString.getBytes(), Base64.NO_WRAP);
            json1.putBody("srvcert", serverCertBase64);
            json1.putBody("port_offset", HXSVmData.portOffset);
            json1.putBody("vm_uuid", HXSVmData.VM_UUID);
            String jsonBase64 = Base64.encodeToString(json1.getPostJson().getBytes(), Base64.NO_WRAP);
            HXSHttpRequestCenter.updateVMSession(jsonBase64, new HXSHttpRequestCenter.IOnRequestCompletionDataReturn() {
                @Override
                public void returnAnalysis(String result) {
                    returnUpdateSession(result);
                }
            });

        }

        Intent intent = new Intent(parent, Game.class);
        intent.putExtra(Game.EXTRA_HOST, computer.activeAddress);
        intent.putExtra(Game.EXTRA_APP_NAME, app.getAppName());
        intent.putExtra(Game.EXTRA_APP_ID, app.getAppId());
        intent.putExtra(Game.EXTRA_APP_HDR, app.isHdrSupported());
        intent.putExtra(Game.EXTRA_UNIQUEID, managerBinder.getUniqueId());
        intent.putExtra(Game.EXTRA_PC_UUID, computer.uuid);
        intent.putExtra(Game.EXTRA_PC_NAME, computer.name);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        try {
            if (computer.serverCert != null) {
                intent.putExtra(Game.EXTRA_SERVER_CERT, computer.serverCert.getEncoded());
            }
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public static void doStart(Context parent, NvApp app, ComputerDetails computer,
                               ComputerManagerService.ComputerManagerBinder managerBinder) {
        HXSVmData.setApp(app);
        HXSVmData.setComputerDetails(computer);
        HXSHttpRequestCenter.requestPing(HXStreamDelegate.getInstance().pingListener);
        if (computer.state == ComputerDetails.State.OFFLINE || computer.activeAddress == null) {
            Toast.makeText(parent, parent.getResources().getString(R.string.pair_pc_offline), Toast.LENGTH_SHORT).show();
            return;
        }
        parent.startActivity(createStartIntent(parent, app, computer, managerBinder));
    }

    public static void doStart(Context parent, NvApp app, ComputerDetails computer) {
        HXSVmData.setApp(app);
        HXSVmData.setComputerDetails(computer);
        HXSHttpRequestCenter.requestPing(HXStreamDelegate.getInstance().pingListener);
        try {
            if (computer.state == ComputerDetails.State.OFFLINE ||
                    ServerHelper.getCurrentAddressFromComputer(computer) == null) {
                Toast.makeText(parent, parent.getResources().getString(R.string.pair_pc_offline), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException ignored) {
            return;
        }
        parent.startActivity(createStartIntent(parent, app, computer));
    }

    public static Intent createStartIntent(Context parent, NvApp app, ComputerDetails computer) {
        Intent intent = new Intent(parent, Game.class);
        try {
            intent.putExtra(Game.EXTRA_HOST, getCurrentAddressFromComputer(computer));
        } catch (Exception ignored) {
        }
        intent.putExtra(Game.EXTRA_APP_NAME, app.getAppName());
        intent.putExtra(Game.EXTRA_APP_ID, app.getAppId());
        intent.putExtra(Game.EXTRA_APP_HDR, app.isHdrSupported());
        intent.putExtra(Game.EXTRA_UNIQUEID, HXSVmData.getUniqueId());
        intent.putExtra(Game.EXTRA_PC_UUID, computer.uuid);
        intent.putExtra(Game.EXTRA_PC_NAME, computer.name);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        try {
            if (computer.serverCert != null) {
                intent.putExtra(Game.EXTRA_SERVER_CERT, computer.serverCert.getEncoded());
            }
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public static void doNetworkTest(final Activity parent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerDialog spinnerDialog = SpinnerDialog.displayDialog(parent,
                        parent.getResources().getString(R.string.nettest_title_waiting),
                        parent.getResources().getString(R.string.nettest_text_waiting),
                        false);

                int ret = MoonBridge.testClientConnectivity(CONNECTION_TEST_SERVER, 443, MoonBridge.ML_PORT_FLAG_ALL);
                spinnerDialog.dismiss();

                String dialogSummary;
                if (ret == MoonBridge.ML_TEST_RESULT_INCONCLUSIVE) {
                    dialogSummary = parent.getResources().getString(R.string.nettest_text_inconclusive);
                } else if (ret == 0) {
                    dialogSummary = parent.getResources().getString(R.string.nettest_text_success);
                } else {
                    dialogSummary = parent.getResources().getString(R.string.nettest_text_failure);
                    dialogSummary += MoonBridge.stringifyPortFlags(ret, "\n");
                }

                Dialog.displayDialog(parent,
                        parent.getResources().getString(R.string.nettest_title_done),
                        dialogSummary,
                        false);
            }
        }).start();
    }

    public static void doQuit(final Activity parent,
                              final ComputerDetails computer,
                              final NvApp app,
                              final ComputerManagerService.ComputerManagerBinder managerBinder,
                              final Runnable onComplete) {
        Toast.makeText(parent, parent.getResources().getString(R.string.applist_quit_app) + " " + app.getAppName() + "...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NvHTTP httpConn;
                String message;
                try {
                    httpConn = new NvHTTP(ServerHelper.getCurrentAddressFromComputer(computer),
                            managerBinder.getUniqueId(), computer.serverCert, PlatformBinding.getCryptoProvider(parent));
                    if (httpConn.quitApp()) {
                        message = parent.getResources().getString(R.string.applist_quit_success) + " " + app.getAppName();
                    } else {
                        message = parent.getResources().getString(R.string.applist_quit_fail) + " " + app.getAppName();
                    }
                } catch (GfeHttpResponseException e) {
                    if (e.getErrorCode() == 599) {
                        message = "This session wasn't started by this device," +
                                " so it cannot be quit. End streaming on the original " +
                                "device or the PC itself. (Error code: " + e.getErrorCode() + ")";
                    } else {
                        message = e.getMessage();
                    }
                } catch (UnknownHostException e) {
                    message = parent.getResources().getString(R.string.error_unknown_host);
                } catch (FileNotFoundException e) {
                    message = parent.getResources().getString(R.string.error_404);
                } catch (IOException | XmlPullParserException e) {
                    message = e.getMessage();
                    e.printStackTrace();
                } finally {
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }

                final String toastMessage = message;
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent, toastMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}
