package com.limelight.preferences;

import com.limelight.Infrastructure.httpUtils.HXSPostBodyJson;

import org.json.JSONException;
import org.json.JSONObject;

public class HXSControllerPosition {
    public double width;
    public double height;
    public double size;
    public boolean visitable;

    public HXSControllerPosition(JSONObject jsonObject){
        try {
            width = jsonObject.getDouble("width");
            height = jsonObject.getDouble("height");
            size = jsonObject.getDouble("size");
            visitable = jsonObject.getBoolean("visible");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject getJson(){
        HXSPostBodyJson hxsPostBodyJson = new HXSPostBodyJson();
        hxsPostBodyJson.putBody("width",""+width);
        hxsPostBodyJson.putBody("height",""+height);
        hxsPostBodyJson.putBody("size",""+size);
        hxsPostBodyJson.putBody("visible",""+visitable);
        try {
            return new JSONObject(hxsPostBodyJson.getPostJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
