package com.xilu.wybz.ui.presenter;

import android.content.Context;
import android.view.View;

import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.ui.IView.IBaseView;

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
}
