package com.xilu.wybz.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.http.rsa.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by June on 16/04/28.
 */
public class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(String response) {

    }
}
