package com.anenn.network;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 定义网络拦截器
 * Created by anenn on 3/27/16.
 */
public class NetworkInterceptor implements Interceptor {

    private String userAgent;

    public NetworkInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder requestBuild = chain.request().newBuilder()
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", HttpConstants.CONTENT_TYPE);
        // 根据需要添加 Token, 默认不添加
        if (!TextUtils.isEmpty(HttpConstants.TOKEN)) {
            requestBuild.addHeader("Authorization", HttpConstants.TOKEN);
        }

        final Request request = requestBuild.build();
        final Response.Builder responseBuilder = chain.proceed(request).newBuilder()
                .header("Cache-Control", request.cacheControl().toString());

        return responseBuilder.build();
    }
}