package com.meirenmeitu.net.intercepter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 有网时缓存
 * Created by Jooyer on 2018/4/9
 * https://www.jianshu.com/p/dbda0bb8d541
 */

final public class NetCacheInterceptor implements Interceptor {
    // 在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为 0
    private static final int ONLINE_CACHE_TIME = 30; // 单位 秒

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());


        String cacheControl = response.header("Cache-Control");
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + ONLINE_CACHE_TIME)
                    .build();
        } else {
            return response;
        }

//        return response.newBuilder()
//                .header("Cache-Control", "public," +
//                        // 没有超出max-age,不管怎么样都是返回数据，超过了maxAge,发起新的请求获取数据更新，请求失败返回缓存数据
//                        " max-age=" + ONLINE_CACHE_TIME)
//                .removeHeader("Pragma")
//                .build();
    }
}
