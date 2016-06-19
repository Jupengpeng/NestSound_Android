package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ILoginView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    public LoginPresenter(Context context, ILoginView iView) {
        super(context, iView);
    }

    public void login(String mobliePhone, String password) {
        params = new HashMap<>();
        params.put("mobile", mobliePhone);
        params.put("password", password);
        httpUtils.post(MyHttpClient.getLoginUrl(), params, new AppJsonCalback(context) {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.loginStart();
            }

            @Override
            public Type getDataType() {
                new TypeToken<List<String>>(){}.getType();
                return UserBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                UserBean userBean = response.getData();
                if(userBean.userid>0) {
                    iView.loginSuccess(userBean);
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
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
