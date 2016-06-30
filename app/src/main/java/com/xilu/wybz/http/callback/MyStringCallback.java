package com.xilu.wybz.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.http.rsa.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class MyStringCallback extends StringCallback {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        String content = response.body().string();
        Log.i("url",content);
        try {
            JSONObject jsonObject = new JSONObject(content);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                String newData = RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(data), "UTF-8"));
                jsonObject.put("data", newData);
                Log.i("url",newData);
                return jsonObject.toString();
            }
        } catch (JSONException e) {
            Log.i("url JSONException", e.toString());
        }
        return content;
    }
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
//        e.printStackTrace();
    }

    @Override
    public void onResponse(String response) {

    }
    @Override
    public void inProgress(float progress) {

    }
}