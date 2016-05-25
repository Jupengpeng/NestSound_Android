package com.xilu.wybz.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.http.rsa.RSAUtils;
import com.xilu.wybz.utils.PhoneInfoUtil;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
/**
 * Created by June on 16/4/28.
 */
public class HttpUtils {
    Map<String, String> headers = new HashMap<>();
    Context context;
    public HttpUtils(Context context) {
        this.context = context;
        headers.put("APP-Key", "APP-Secret222");
        headers.put("machine", PhoneInfoUtil.getMachine(context));
    }

    //普通post提交
    public void post(String url, Map<String, String> params, MyStringCallback stringCallback) {
        Log.e("url",MyHttpClient.BASE_URL + url);
        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneInfoUtil.getPhoneImei(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        Log.e("params",new Gson().toJson(params));
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(new Gson().toJson(params)).getBytes());
        Log.e("content",content);
        OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(url)
                .addParams("data", content)
                .headers(headers)
                .build()
                .execute(stringCallback);

    }
    //普通post提交
    public void postUrl(String url, Map<String, String> params, MyStringCallback stringCallback) {
        OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(url)
                .params(params)
                .headers(headers)
                .build()
                .execute(stringCallback);
    }
    //提post加密后的json串
    public void postString(String url, String content, MyStringCallback stringCallback) {
        OkHttpUtils
                .postString()
                .tag(url)
                .headers(headers)
                .url(MyHttpClient.BASE_URL + url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(content)
                .build()
                .execute(stringCallback);
    }

    //put加密后的json串
    public void putString(String url, String content, MyStringCallback stringCallback) {
        OkHttpUtils
                .put()
                .tag(url)
                .headers(headers)
                .url(MyHttpClient.BASE_URL + url)
                .requestBody(content)
                .build()
                .execute(stringCallback);
    }
    //加密get
    public void get(String url, Map<String, String> params, MyStringCallback stringCallback) {
        Log.e("url",url);
        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneInfoUtil.getPhoneImei(context));
        params.put("token",PrefsUtil.getUserInfo(context).loginToken);
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(new Gson().toJson(params)).getBytes());
        Log.e("content",content);
        OkHttpUtils
                .get()
                .headers(headers)
                .tag(url)
                .url(MyHttpClient.BASE_URL + url)
                .addParams("data", content)
                .build()
                .execute(stringCallback);
    }
    //普通get
    public void get(String url, MyStringCallback stringCallback) {
        Log.e("url",url);
        OkHttpUtils
                .get()
                .headers(headers)
                .tag(MyHttpClient.BASE_URL + url)
                .url(MyHttpClient.BASE_URL + url)
                .build()
                .execute(stringCallback);
    }
    //获取网络图片返回bitmap
    public void getImage(String url, BitmapCallback bitmapCallback) {
        OkHttpUtils
                .get()
                .url(url)
                .tag(url)
                .headers(headers)
                .build()
                .execute(bitmapCallback);
    }

    //下载网络问文件 返回file文件
    public void getFile(String url, FileCallBack fileCallBack) {
        OkHttpUtils
                .get()
                .url(url)
                .tag(url)
                .headers(headers)
                .build()
                .execute(fileCallBack);
    }
}
