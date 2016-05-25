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
        Log.e("response", content);
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONObject data = jsonObject.getJSONObject("data");
            if (!TextUtils.isEmpty(data.toString())) {
                String newData = RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(data.toString()), "UTF-8"));

                jsonObject.put("data", new JSONObject(newData));
                return jsonObject.toString();
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
            e.printStackTrace();
        }
        return content;
    }
}
