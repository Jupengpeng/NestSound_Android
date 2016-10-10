package com.xilu.wybz.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.http.callback.Callback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.request.RequestCall;
import com.xilu.wybz.http.rsa.RSAUtils;
import com.xilu.wybz.utils.PhoneUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;

/**
 * Created by June on 16/4/28.
 */
public class HttpUtils {

    private Map<String, String> headers;
    private Context context;
    private String httpTag;

    public HttpUtils(Context context) {
        this(context,null);
        this.context = context;
    }

    public HttpUtils(Context context, String viewTag) {
        this.context = context;
        this.httpTag = viewTag;
        headers = new HashMap<>();
        headers.put("User-Agent", "yinchao android");
        headers.put("charset", "UTF-8");
        headers.put("machine", PhoneUtils.getMachine(context));
    }
    //普通post提交
    public void post(String url, Map<String, String> params, Callback callback) {
        if(params==null){
            params = new HashMap<>();
        }
//        params.put("expiretime",System.currentTimeMillis()+ PhoneUtils.getPhoneImei(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);

        String paramString = new Gson().toJson(params);

        Log.e("url","url:"+url+"?data="+paramString);
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(paramString).getBytes());
        Log.e("url","encode:"+content);
        OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(httpTag)
                .addParams("data", content)
                .headers(headers)
                .build()
                .execute(callback);

    }
    //普通post提交
  /*  public void postHost(String url, Map<String, String> params, Callback callback) {
        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneUtils.getPhoneImei(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);

        String paramString = new Gson().toJson(params);

        Log.e("url","url:"+url+":params:"+paramString);
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(paramString).getBytes());
        Log.e("url","encode:"+content);
        OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(httpTag)
                .addParams("data", content)
                .headers(headers)
                .build()
                .execute(callback);

    }*/


    //普通post提交
    public RequestCall postLong(String url, Map<String, String> params, Callback stringCallback) {
        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneUtils.getPhoneImei(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);

        String paramString = new Gson().toJson(params);

        Log.e("url","url:"+url+"params:"+paramString);
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(paramString).getBytes());
        Log.e("url","encode:"+content);
        RequestCall call = OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(httpTag)
                .addParams("data", content)
                .headers(headers)
                .build()
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000);

                call.execute(stringCallback);

        return call;
    }

    //普通post提交
    public RequestCall post(String tag, String url, Map<String, String> params, Callback stringCallback) {

        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneUtils.getPhoneImei(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);

        String paramString = new Gson().toJson(params);

        Log.e("url","url:"+url+"params:"+paramString);
        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(paramString).getBytes());
        Log.e("url","encode:"+content);
        RequestCall call = OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(tag)
                .addParams("data", content)
                .headers(headers)
                .build();

        call.execute(stringCallback);
        return call;
    }


    //普通post提交
    public void postUrl(String url, Map<String, String> params, Callback stringCallback) {

        OkHttpUtils.post()
                .url(MyHttpClient.BASE_URL + url)
                .tag(httpTag)
                .params(params)
                .headers(headers)
                .build()
                .execute(stringCallback);
    }
    //提post加密后的json串
    public void postString(String url, String content, Callback stringCallback) {
        OkHttpUtils
                .postString()
                .tag(httpTag)
                .headers(headers)
                .url(MyHttpClient.BASE_URL + url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(content)
                .build()
                .execute(stringCallback);
    }

    //put加密后的json串
    public void putString(String url, String content, Callback stringCallback) {
        OkHttpUtils
                .put()
                .tag(httpTag)
                .headers(headers)
                .url(MyHttpClient.BASE_URL + url)
                .requestBody(content)
                .build()
                .execute(stringCallback);
    }
    //加密get
    public void get(String url, Map<String, String> params, Callback stringCallback) {
        if(params==null){
            params = new HashMap<>();
        }
        params.put("expiretime",System.currentTimeMillis()+ PhoneUtils.getPhoneImei(context));
        params.put("token",PrefsUtil.getUserInfo(context).loginToken);

        String paramString = new Gson().toJson(params);
        Log.e("url","url:"+url+"params:"+paramString);

        String content = RSAUtils.encodeConvert(RSAUtils.encryptByPublicKey(paramString).getBytes());
        Log.e("url","encode:"+content);
        OkHttpUtils
                .get()
                .headers(headers)
                .tag(httpTag)
                .url(MyHttpClient.BASE_URL + url)
                .addParams("data", content)
                .build()
                .execute(stringCallback);
    }
    //普通get
    public void get(String url, Callback stringCallback) {
        OkHttpUtils
                .get()
                .headers(headers)
                .tag(httpTag)
                .url(url)
                .build()
                .execute(stringCallback);
    }
    //获取网络图片返回bitmap
    public void getImage(String url, BitmapCallback bitmapCallback) {
        if(checkUrl(url)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .tag(httpTag)
                    .headers(headers)
                    .build()
                    .execute(bitmapCallback);
        }
    }

    //下载网络问文件 返回file文件
    public void getFile(String url, FileCallBack fileCallBack) {

        if(checkUrl(url)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .tag(httpTag)
                    .headers(headers)
                    .build()
                    .readTimeOut(30000)
                    .writeTimeOut(30000)
                    .execute(fileCallBack);
        }
    }

    public void uploadFile(String url, String fileName,Callback callback){
        if(!checkUrl(url)) {
            return;
        }

        Log.e("upload","url:"+url);
        File file = new File(fileName);
        OkHttpUtils
                .post()
                .url(url)
                .tag(httpTag)
                .addFile("file",file.getName(),file)
                .headers(headers)
                .build()
                .readTimeOut(30000)
                .writeTimeOut(30000)
                .execute(callback);
    }


    public void cancelHttpByTag(String url){

        OkHttpUtils.getInstance().cancelTag(url);
    }

    /**
     * 编码为utf-8.
     * @param text
     * @return
     */
    protected String stringEncode(String text){
        return StringUtils.toUtf8(text);
    }



    public boolean checkUrl(String url){
        if (url == null){
            ToastUtils.toast(context,"输入的请求地址不合法！");
            return false;
        }
        // Silently replace websocket URLs with HTTP URLs.
        if (url.regionMatches(true, 0, "ws:", 0, 3)) {
            url = "http:" + url.substring(3);
        } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
            url = "https:" + url.substring(4);
        }

        HttpUrl parsed = HttpUrl.parse(url);
        if (parsed == null) {
            ToastUtils.toast(context,"输入的请求地址不合法！");
            return false;
        }
        return true;
    }
}
