package com.limelight.Infrastructure.httpUtils;

import android.os.Handler;
import android.os.Looper;


import com.limelight.module.CmdData;
import com.limelight.HXSLog;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.UserData.HXSUserModule;
import com.limelight.UserData.HXSVmData;
import com.limelight.controller.HXStreamDelegate;
import com.limelight.nvstream.PingThread;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class HXSHttpRequestCenter {
    private static Handler handler = null;

    public static void UpdateVMSuspend(final int suspendMinutes, final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String OkHttpReturn = "";
                HXSPostBodyJson json1 = new HXSPostBodyJson();
                json1.putBody("suspend_time", suspendMinutes);
                HXSOkHttpClient HttpClient = null;
                try {
                    HttpClient = new HXSOkHttpClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    OkHttpReturn = HttpClient.patch(HXSConstant.VmsURL + HXSVmData.VM_UUID + "/suspend", json1.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());
                } catch (IOException e) {
                    HXSLog.info(e.toString());
                }
                final String finalOkHttpReturn = OkHttpReturn;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.returnAnalysis(finalOkHttpReturn);

                    }
                });
            }
        }).start();
    }

    public static void requestIp(final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json = new HXSPostBodyJson();
                json.putBody("region", HXSVmData.LineSelection);
                json.putBody("uuid", HXSVmData.VM_UUID);
                json.putBody("charge_type", HXStreamDelegate.getInstance().getChargeType());
//                json.putBody("charge_type", "0");
                json.putBody("client_version", HXSVmData.version);

                String okHttpReturn;
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    okHttpReturn = httpClient.get(HXSConstant.ApplyVMURL, json.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());
                    final String finalOkHttpReturn = okHttpReturn;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(finalOkHttpReturn);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestShutdownVm(final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json = new HXSPostBodyJson();
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    final String result = httpClient.post(HXSConstant.VmsURL + HXSVmData.VM_UUID + "/shutdown", json.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public static void requestPair(final String pin, final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HXSPostBodyJson json = new HXSPostBodyJson();
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    json.putBody("token", pin);
                    final String result = httpClient.postSSLIgnored(HXSVmData.PairURL, json.getPostJson());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(result);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestVmSession(final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSOkHttpClient httpClient = new HXSOkHttpClient();
                try {
                    final String result = httpClient.get(HXSConstant.RegisteredURI + "/username/vm_session", "Authorization", HXSUserModule.getInstance().getToken());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestSendKey(final String key, final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json = new HXSPostBodyJson();
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    json.putBody("vkey", key);
                    if ("".equals(HXSVmData.ComboURL) || HXSVmData.ComboURL == null) {
                        HXSVmData.ComboURL = "https://" + HXSVmData.ip + ":" + (50052 + HXSVmData.portOffset) + "/sendkey";
                    }
                    final String result = httpClient.postSSLIgnored(HXSVmData.ComboURL, json.getPostJson());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener == null) {
                                return;
                            }
                            listener.returnAnalysis(result);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestSendResolution(final String width, String height, String fps) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json = new HXSPostBodyJson();
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    json.putBody("width", width);
                    json.putBody("height", height);
                    json.putBody("fps", fps);
                    HXSLog.info("requestSendResolution" + json.getPostJson());

                   String comboURL= "https://" + HXSVmData.ip + ":" + (50052 + HXSVmData.portOffset) + "/resolution";
                    HXSLog.info("requestSendResolution" + httpClient.postSSLIgnored(comboURL, json.getPostJson()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestStartCache(final String ip, String region, String userName) {

        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json = new HXSPostBodyJson();
                HXSOkHttpClient httpClient;
                try {
                    httpClient = new HXSOkHttpClient();
                    json.putBody("username", userName);
                    json.putBody("region", region);
                    json.putBody("ip", ip);
                    HXSLog.info("requestStartCache" + json.getPostJson());

                    String comboURL = "https://" + HXSVmData.ip + ":" + (50052 + HXSVmData.portOffset) + "/cutScreenInfo";
                    HXSLog.info("requestStartCache" + httpClient.postSSLIgnored(comboURL, json.getPostJson()));
                    HXSLog.info("HXSVmData.ComboURL" + comboURL);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestPing(final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        PingThread.getInstance().start();
    }

    public static void requestSendFeedback(final IOnRequestCompletionDataReturn listener, final String type, final String message, final int rate) {
        Thread pingRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                HXSPostBodyJson postBodyJson = new HXSPostBodyJson();
                postBodyJson.putBody("feedback_type", type);
                postBodyJson.putBody("question", message);
                postBodyJson.putBody("score", rate);
                HXSOkHttpClient post = new HXSOkHttpClient();
                try {
                    String pingReturn = post.post(HXSConstant.Feedback, postBodyJson.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());
                    HXSLog.info("反馈返回信息" + pingReturn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pingRunnable.start();
    }

    public static void requestSendCmd(final IOnRequestCompletionDataReturn listener, CmdData data) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HXSOkHttpClient httpClient = new HXSOkHttpClient();
                    String url = "https://" + HXSVmData.ip + ":" + (50052 + HXSVmData.portOffset) + "/process";
                    HXSLog.info("cmd 链接 " + url);
                    final String result = httpClient.postSSLIgnored(url, data.toJsonString());
                    HXSLog.info("cmd发送内容   " + data.toJsonString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static void refreshBalance(final IOnRequestCompletionDataReturn listener) {
        Thread balanceRunnable = new Thread() {


            @Override
            public void run() {
                super.run();
                HXSOkHttpClient post;
                String requestReturn = "";
                try {
                    post = new HXSOkHttpClient();
                    String URI = HXSConstant.RegisteredURI + "/name/userinfo";
                    requestReturn = post.get(URI, "Authorization", HXSUserModule.getInstance().getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String finalRequestReturn = requestReturn;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.returnAnalysis(finalRequestReturn);
                    }
                });
            }
        };
        balanceRunnable.start();
    }

    public static void autoClick(String x, String y, String progressName) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSOkHttpClient httpClient = new HXSOkHttpClient();
                HXSPostBodyJson json = new HXSPostBodyJson();
                json.putBody("dx", x);
                json.putBody("dy", y);
                json.putBody("procname", progressName);
                String url = "https://" + HXSVmData.ip + ":" + (50052 + HXSVmData.portOffset) + "/autoclick";
                try {
                    final String result = httpClient.postSSLIgnored(url, json.getPostJson());
                    HXSLog.info("autoClick返回结果" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void updateVMSession(final String session, final IOnRequestCompletionDataReturn listener) {
        checkHandlerExist();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HXSPostBodyJson json1 = new HXSPostBodyJson();
                json1.putBody("session", session);
                try {
                    HXSOkHttpClient httpClient = new HXSOkHttpClient();
                    final String result = httpClient.patch(HXSConstant.VmsURL + HXSVmData.VM_UUID + "/session", json1.getPostJson(), "Authorization", HXSUserModule.getInstance().getToken());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.returnAnalysis(result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static void checkHandlerExist() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public interface IOnRequestCompletionDataReturn {
        void returnAnalysis(String result);
    }
}
