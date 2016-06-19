package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IForgetPwdView;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class ForgetPwdPresenter extends BasePresenter<IForgetPwdView> {
    public ForgetPwdPresenter(Context context, IForgetPwdView iView) {
        super(context, iView);
    }

    public void getSmsCode(String phone) {
        params = new HashMap<>();
        params.put("type","2");
        params.put("mobile",phone);
        httpUtils.get(MyHttpClient.getSmsCodeUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.getSmsCodeFail();
            }

            @Override
            public void onResponse(String response) {
                iView.getSmsCodeSuccess(response);
            }

        });
    }

    public void modifyPwd(String phone, String password, String code) {
        params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", password);
        httpUtils.post(MyHttpClient.getPasswordUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.modifyPwdFail();
            }

            @Override
            public void onResponse(String response) {
                iView.modifyPwdSuccess(response);
            }

        });
    }
}
