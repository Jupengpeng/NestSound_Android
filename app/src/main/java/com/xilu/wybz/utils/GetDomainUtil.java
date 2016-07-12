package com.xilu.wybz.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.http.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/5/18.
 */
public class GetDomainUtil {
    Context mContext;
    HttpUtils httpUtils;
    public GetDomainUtil(Context context){
        mContext = context;
        httpUtils = new HttpUtils(mContext);
    }
    public void getNewIp() {
        httpUtils.get(MyHttpClient.getDomain(), new StringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    String api_domain = new JSONObject(response).getString("api_domain");
//                    String page_start_pic = new JSONObject(response).getString("page_start_pic");
                    if(StringUtil.isNotBlank(api_domain)) {
                        PrefsUtil.putString("domain", api_domain, mContext);
                        MyHttpClient.ROOT_URL = api_domain;
                    }
//                    PrefsUtil.putString("applogo", page_start_pic, mContext);
//                    downLoadLogo(page_start_pic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }
    public void downLoadLogo(String url){
        if(ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (!new File(FileDir.logoDir).exists())
                new File(FileDir.logoDir).mkdirs();
            String fileName = MD5Util.getMD5String(url) + ".png";
            String filePath = FileDir.logoDir + fileName;
            if (!new File(filePath).exists()) {
                httpUtils.getFile(url, new FileCallBack(FileDir.logoDir, fileName) {
                    @Override
                    public void inProgress(float progress, long total) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(File response) {

                    }
                });
            }
        }
    }
    public void getCheck(){
        Map<String, String> params = new HashMap<>();
        UserBean userBean = PrefsUtil.getUserInfo(mContext);
        params.put("token", userBean.loginToken);
        httpUtils.post(MyHttpClient.getTokenCheck(),params,new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200){
                        UserBean userBean = ParseUtils.getUserBean(mContext,response);
                        PrefsUtil.saveUserInfo(mContext, userBean);
                    }else{
                        //清除本地用户信息
                        PrefsUtil.saveUserInfo(mContext, new UserBean());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
