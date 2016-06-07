package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.ToastUtils;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class LoginPresenter extends BasePresenter<ILoginView>{
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
                UserBean userBean = ParseUtils.getUserBean(context,response);
                if(userBean.userid>0)
                    iView.loginSuccess(userBean);
                else
                    ToastUtils.toast(context,"用户Id获取失败");
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
