package com.limelight.Infrastructure.httpUtils;

import org.json.JSONObject;

public class HXSPostBodyJson {
    private String postJson = "";

    public void putBody(String key, String value) {
        if (!postJson.equals("")) {
            postJson = postJson + ",";
        }
        postJson = postJson + "\"" + key + "\":\"" + value + "\"";
    }

    public void putBody(String key, int value) {
        if (!postJson.equals("")) {
            postJson = postJson + ",";
        }
        postJson = postJson + "\"" + key + "\":" + value;
    }
    public void putBody(String key, JSONObject object){
        if (!postJson.equals("")){
            postJson = postJson + ",";
        }
        postJson = postJson + "\"" + key + "\":" + object.toString();
    }

    public String getPostJson() {
        String result = "{" +
                postJson +
                "}";
        return result;
    }
}
