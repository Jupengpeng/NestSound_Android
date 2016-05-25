package com.xilu.wybz.http.callback;

import okhttp3.Call;
import okhttp3.Request;

public class MyStringCallback extends StringCallback {
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