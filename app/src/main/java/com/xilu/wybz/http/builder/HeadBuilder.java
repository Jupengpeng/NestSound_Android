package com.xilu.wybz.http.builder;


import com.xilu.wybz.http.OkHttpUtils;
import com.xilu.wybz.http.request.OtherRequest;
import com.xilu.wybz.http.request.RequestCall;

/**
 * Created by June on 16/04/28.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
