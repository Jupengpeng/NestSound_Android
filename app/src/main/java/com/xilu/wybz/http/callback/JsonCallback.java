package com.xilu.wybz.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.http.rsa.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/26.
 */
public class JsonCallback extends Callback<JsonResponse>{

    protected Type type = null;

    public JsonCallback() {

    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public JsonResponse parseNetworkResponse(Response response) throws Exception {

        String body = response.body().string();
        JsonResponse jsonResponse = new JsonResponse();

        try {
            JSONObject jsonObject = new JSONObject(body);

            String data = jsonObject.getString("data");
            String message = jsonObject.getString("message");
            int code = jsonObject.getInt("code");

            jsonResponse.setCode(code);
            jsonResponse.setMessage(message);

            if (!TextUtils.isEmpty(data)) {
                String decode = RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(data), "UTF-8"));
                jsonResponse.setData(new Gson().fromJson(decode,type!=null?type:getDataType()));
            }

        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
            jsonResponse.setCode(999);
            jsonResponse.setMessage("json decode error.");

            e.printStackTrace();

        }
        return jsonResponse;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(JsonResponse response) {

    }

    public Type getDataType(){
        return new TypeToken<JsonResponse<String>>(){}.getType();
    }



}
