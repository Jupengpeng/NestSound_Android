package com.xilu.wybz.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.http.rsa.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by June on 16/04/28.
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        String content = response.body().string();
        try {
            JSONObject jsonObject = new JSONObject(content);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                String newData = RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(data), "UTF-8"));
                jsonObject.put("data", newData);
                return jsonObject.toString();
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
            e.printStackTrace();
        }
        return content;
    }
}
