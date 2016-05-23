package com.xilu.wybz.ui.utils;

import android.content.Context;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import org.json.JSONObject;
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
        new HttpUtils(context).get(MyHttpClient.getQnToken(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject data = new JSONObject(response).getJSONObject("data");
                    String token = data.getString("token");
                    String filename = data.getString("filename");
                    String qnurl = data.getString("domain_qiliu");
                    MyHttpClient.QINIU_URL = qnurl + "/";
                    if (tokenResult != null) {
                        tokenResult.OnResult(token, filename);
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    public interface TokenResult {
        void OnResult(String token, String filename);
    }

}
