package com.xilu.wybz.ui.http.builder;

import com.xilu.wybz.http.builder.OkHttpRequestBuilder;

import java.util.Map;

/**
 * Created by June on 16/3/1.
 */
public interface HasParamsable {
    OkHttpRequestBuilder params(Map<String, String> params);

    OkHttpRequestBuilder addParams(String key, String val);

}
