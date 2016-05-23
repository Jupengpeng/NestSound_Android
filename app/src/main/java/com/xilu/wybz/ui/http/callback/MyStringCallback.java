package com.xilu.wybz.ui.http.callback;

import com.xilu.wybz.http.callback.*;

import okhttp3.Call;
import okhttp3.Request;

public class MyStringCallback extends com.xilu.wybz.http.callback.StringCallback {
    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
    }

    @Override
    public void onAfter() {
        super.onAfter();
    }

    @Override
    public void onError(Call call, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(String response) {
    }

    @Override
    public void inProgress(float progress) {

    }
}