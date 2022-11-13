package com.limelight.Infrastructure.httpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.limelight.Infrastructure.httpUtils.HXSSslIgnored.trustAllSslClient;

public class HXSOkHttpClient {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String USER_NOT_AUTHORIZED_STRING = "User not authorized";

    public String post(String url, String json) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String patch(String url, String json) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postWithoutCharset(String url, String json) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HXSFixContentTypeInterceptor())
                .build();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String patch(String url, String json, String headName, String headValue) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String get(String url, String headName, String headValue) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public String get(String url,String json, String headName, String headValue) throws IOException {

        OkHttpClient client = new OkHttpClient();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int index = 0;
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {

                String key = keys.next().toString();
                String value = jsonObject.optString(key);
                if (index == 0) {
                    url = url + "?" + key + "=" + value;
                } else {
                    url = url + "&" + key + "=" + value;
                }
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String post(String url, String json, String headName, String headValue) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postWithoutCharset(String url, String json, String headName, String headValue) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HXSFixContentTypeInterceptor())
                .build();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postSSLIgnored(String url, String json) throws IOException {

        OkHttpClient client1 = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HXSFixContentTypeInterceptor())
                .build();
        OkHttpClient client = trustAllSslClient(client1);

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postSSLIgnored(String url, String json, String headName, String headValue) throws IOException {

        OkHttpClient client1 = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HXSFixContentTypeInterceptor())
                .build();

        OkHttpClient client = trustAllSslClient(client1);

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(headName, headValue)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
