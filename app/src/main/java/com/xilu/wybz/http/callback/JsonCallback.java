package com.xilu.wybz.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.http.rsa.RSAUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/5/26.
 */
public class JsonCallback extends Callback<JsonResponse>{

    protected Type type = null;

    public JsonCallback() {
        this.type = getDataType();
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public JsonResponse parseNetworkResponse(Response response) throws Exception {

        String body = response.body().string();
        JsonResponse jsonResponse = new JsonResponse();
        String decode = "";
        Log.d("url",body);
        try {
            JSONObject jsonObject = new JSONObject(body);

            String data = jsonObject.getString("data");
            String message = jsonObject.getString("message");
            int code = jsonObject.getInt("code");

            jsonResponse.setCode(code);
            jsonResponse.setMessage(message);

            if (!TextUtils.isEmpty(data)) {
                decode = RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(data), "UTF-8"));
//                Log.d("url", decode);
//                LogUtils.iJsonFormat("url",decode,true);
                if (type == null){
                    jsonResponse.setData(decode);
                } else {
                    jsonResponse.setData( new Gson().fromJson(decode,type));
                }
            }

        } catch (Exception e) {
            Log.e("JSONException", e.toString());
            jsonResponse.setCode(999);
            jsonResponse.setMessage("json decode error.");
            jsonResponse.setData(decode);
//            e.printStackTrace();

        }
        return jsonResponse;
    }

    @Override
    public void onError(Call call, Exception e) {
        Log.d("url","onError");
    }

    @Override
    public void onResponse(JsonResponse response) {
        Log.d("url","onResponse");
    }

    public Type getDataType(){
        return null;
    }


    @Override
    public void inProgress(float progress) {
        Log.d("url","inProgress:"+progress);
    }

    @Override
    public void onBefore(Request request) {
        Log.d("url","onBefore");
    }

    @Override
    public void onAfter() {
        Log.d("url","onAfter");
    }
}
