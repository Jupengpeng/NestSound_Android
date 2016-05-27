package com.xilu.wybz.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.http.callback.StringCallback;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PrefsUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/5/18.
 */
public class GetDomainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getNewIp();
    }
    private void getNewIp() {
        HttpUtils httpUtil = new HttpUtils(GetDomainService.this);
        httpUtil.get(MyHttpClient.getDomain(), new StringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    String api_domain = new JSONObject(response).getString("api_domain");
                    String page_start_pic = new JSONObject(response).getString("page_start_pic");
                    PrefsUtil.putString("domain", api_domain, GetDomainService.this);
                    PrefsUtil.putString("applogo", page_start_pic, GetDomainService.this);
                    if (!new File(DownLoaderDir.logoDir).exists())
                        new File(DownLoaderDir.logoDir).mkdirs();
                    String fileName = MD5Util.getMD5String(page_start_pic) + ".png";
                    String filePath = DownLoaderDir.logoDir + fileName;
                    if (!new File(filePath).exists()) {
                        httpUtil.getFile(page_start_pic, new FileCallBack(DownLoaderDir.logoDir, fileName) {
                            @Override
                            public void inProgress(float progress, long total) {

                            }

                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(File response) {
                                Log.e("LogoFile", response.toString());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    stopSelf();
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                stopSelf();
            }
        });
    }
}
