package com.xilu.wybz.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.ui.IView.IBaseView;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 基础presenter
 * Created by june on 2016/5/1.
 */
public abstract class BasePresenter<T extends IBaseView> {
    protected HttpUtils httpUtils;
    protected Map<String, String> params;
    protected Context context;
    protected T iView;
    public BasePresenter(Context context, T iView) {
        this.context = context;
        this.iView = iView;
        httpUtils = new HttpUtils(context);
        params = new HashMap<>();
    }
    public void init(){
        iView.initView();
    }


    public void showMessage(String message){
        if (!TextUtils.isEmpty(message)) {
            ToastUtils.toast(context, message);
        }
    }
}
