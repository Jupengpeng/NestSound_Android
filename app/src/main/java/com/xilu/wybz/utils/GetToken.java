package com.xilu.wybz.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.Response;
import com.xilu.wybz.bean.TokenBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
        new HttpUtils(context).get(MyHttpClient.getQnToken(), params, new AppStringCallback(context) {
            @Override
            public Type getDataType() {
                return new TypeToken<Response<TokenResult>>(){}.getType();
            }

            @Override
            public void onResponse(Response<? extends Object> response) {
                super.onResponse(response);
                TokenBean tokenBean = response.getData();
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
