package com.meirenmeitu.net.intercepter;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Jooyer on 2016/12/19
 */
public class LoggingInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");
    private String TAG = "LoggerInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String param = "";
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            param = buffer.readString(charset);
        }

        long t1 = System.nanoTime();
        Log.i(TAG, "=======1=======" + String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(TAG, "=======2=======" + String.format("Received response for %s in %.1fms%n%sconnection=%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(), chain.connection()) + "\n request pram: " + param);
        return response;
    }

}
