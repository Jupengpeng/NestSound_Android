package com.xilu.wybz.utils;

import android.app.Activity;
import android.util.Log;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;

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
    Activity mContext;
    HttpUtils httpUtils;

    public GetDomainUtil(Activity context) {
        mContext = context;
        httpUtils = new HttpUtils(mContext);
    }
    public void getBootPic() {
        httpUtils.get(MyHttpClient.getBootPic(), null, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.e("getBootPic", response);
                try {
                    DataBean dataBean = ParseUtils.getDataBean(mContext, response);
                    if (dataBean != null && dataBean.code == 200 && StringUtils.
                            isNotBlank(dataBean.data)) {
                        JSONObject jsonObject = new JSONObject(dataBean.data);
                        String pic = jsonObject.getString("pic");
                        if (StringUtils.isNotBlank(pic)) downLoadLogo(pic);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void downLoadLogo(String url) {
        PrefsUtil.putString("applogo", url, mContext);
        if (PermissionUtils.checkSdcardPermission(mContext)) {
            if (!new File(FileDir.logoDir).exists())
                new File(FileDir.logoDir).mkdirs();
            String fileName = MD5Util.getMD5String(url) + ".jpg";
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

    public void getCheck() {
        Map<String, String> params = new HashMap<>();
        UserBean userBean = PrefsUtil.getUserInfo(mContext);
        params.put("token", userBean.loginToken);
        httpUtils.post(MyHttpClient.getTokenCheck(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        UserBean userBean1 = ParseUtils.getUserBean(mContext, response);
                        UserBean userBean2 = PrefsUtil.getUserInfo(mContext);
                        userBean2.loginToken = userBean1.loginToken;
                        PrefsUtil.saveUserInfo(mContext, userBean2);
                    } else {
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
