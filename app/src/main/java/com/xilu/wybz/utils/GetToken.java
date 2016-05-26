package com.xilu.wybz.utils;

import android.content.Context;

import com.xilu.wybz.bean.TokenBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class GetToken {
    TokenResult tokenResult;
    Context context;

    public GetToken(Context context) {
        this.context = context;
    }

    public void setTokenResult(TokenResult tokenResult) {
        this.tokenResult = tokenResult;
    }

    public void getToken(int type, String fixx) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("fixx", fixx);
        new HttpUtils(context).get(MyHttpClient.getQnToken(), params, new MyStringCallback() {

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                TokenBean tokenBean = ParseUtils.getTokenBean(context,response);
                if(tokenBean!=null){
                    String token = tokenBean.token;
                    String filename = tokenBean.filename;
                    String qnurl = tokenBean.domain_qiliu;
                    MyHttpClient.QINIU_URL = qnurl + "/";
                    if (tokenResult != null) {
                        tokenResult.OnResult(token, filename);
                    }
                }
            }
        });

    }

    public interface TokenResult {
        void OnResult(String token, String filename);
    }

}
