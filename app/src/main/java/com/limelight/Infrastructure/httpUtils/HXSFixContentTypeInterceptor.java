package com.limelight.Infrastructure.httpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HXSFixContentTypeInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request fixedRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(fixedRequest);
    }
}
