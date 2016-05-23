package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.ui.IView.IRegisterView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class RegisterPresenter extends BasePresenter<IRegisterView>{
    public RegisterPresenter(Context context, IRegisterView iView) {
        super(context, iView);
    }
    public void register(String name,String phone,String code,String password,String repassword){
        params = new HashMap<>();
        params.put("name",name);
        params.put("phone",phone);
        params.put("code",code);
        params.put("password",password);
        params.put("repassword",repassword);
        httpUtils.post(MyHttpClient.getRegiterUrl(), params, new MyStringCallback(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.registerStart();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.registerSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.registerFail();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.registerFinish();
            }
        });
    }

    public void getSmsCode(String phone,String type){
        params = new HashMap<>();
        params.put("mobile",phone);
        params.put("type",type);
        httpUtils.get(MyHttpClient.getSmsCodeUrl(), params, new MyStringCallback(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.SmsCodeStart();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.SmsCodeSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.SmsCodeFail();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.SmsCodeFinish();
            }
        });
    }
}
