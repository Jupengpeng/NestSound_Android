package com.xilu.wybz.ui.http.request;

import com.xilu.wybz.http.request.OkHttpRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by June on 16/04/28.
 */
public class GetRequest extends OkHttpRequest {
    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }


    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }


}
