package com.xilu.wybz.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/12.
 */
public class LogInterceptor implements Interceptor {

    public LogInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

//        Log.d()
        Response response = chain.proceed(request);


        return response;
    }
}
