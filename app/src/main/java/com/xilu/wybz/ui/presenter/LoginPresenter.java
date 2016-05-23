package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.ILoginView;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class LoginPresenter extends com.xilu.wybz.presenter.BasePresenter<ILoginView> {
    public LoginPresenter(Context context, ILoginView iView) {
        super(context, iView);
    }
    public void login(String mobliePhone,String password){
        params.put("mobile",mobliePhone);
        params.put("password",password);
        httpUtils.post(MyHttpClient.getLoginUrl(), params, new MyStringCallback(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.loginStart();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.loginSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loginFail();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.loginFinish();
            }
        });
    }
}
