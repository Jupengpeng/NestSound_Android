package com.xilu.wybz.presenter;

import android.content.Context;

import com.umeng.socialize.utils.Log;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.ui.IView.IBaseView;

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

    protected String httpTag;

    public BasePresenter(Context context, T iView) {
        this.context = context;
        this.iView = iView;
        httpTag = iView.getClass().getName() + ":" + iView.hashCode();
        Log.d("tag", httpTag);
        httpUtils = new HttpUtils(context, httpTag);
    }

    public void init() {
        iView.initView();
    }

    public void cancelRequest() {
        httpUtils.cancelHttpByTag(httpTag);
    }



}
