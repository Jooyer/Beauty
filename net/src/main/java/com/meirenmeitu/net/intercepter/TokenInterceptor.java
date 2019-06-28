package com.meirenmeitu.net.intercepter;

import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * http://www.mamicode.com/info-detail-1375498.html
 * Token
 * Created by WZG on 2016/10/26.
 */

public class TokenInterceptor implements Interceptor {
    private static final String APP_REQUEST_TOKEN = "app_request_token";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request()
                .newBuilder()
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Accept","application/json;charset=UTF-8")
                .addHeader("X-Access-Token",  MMKV.defaultMMKV().decodeString(APP_REQUEST_TOKEN,"")) // TODO
                .build();
//
        Log.i("TokenInterceptor", "=======X-Access-Token : " + MMKV.defaultMMKV().decodeString(APP_REQUEST_TOKEN,""));
        return chain.proceed(newRequest);
    }

}
