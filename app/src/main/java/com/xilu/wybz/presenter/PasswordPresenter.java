package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IPasswordView;
import com.xilu.wybz.utils.ParseUtils;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class PasswordPresenter extends BasePresenter<IPasswordView>{
    public PasswordPresenter(Context context, IPasswordView iView) {
        super(context, iView);
    }
    public void modifyPwd(String phone,String code,String password,String repassword){
        params = new HashMap<>();
        params.put("mobile",phone);
        params.put("code",code);
        params.put("password",password);
        httpUtils.post(MyHttpClient.getResetPwdUrl(), params, new MyStringCallback(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.modifyPwdStart();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                UserBean userBean = ParseUtils.getUserBean(context, response);
                if(userBean!=null&&userBean.userid>0)
                    iView.modifyPwdSuccess(userBean);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.modifyPwdFail();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.modifyPwdFinish();
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
