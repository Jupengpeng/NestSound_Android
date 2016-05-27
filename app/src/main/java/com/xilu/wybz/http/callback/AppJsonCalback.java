package com.xilu.wybz.http.callback;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/26.
 */
public class AppJsonCalback extends JsonCallback {

    Context context;

    public AppJsonCalback(Type type) {
        super(type);
    }

    public AppJsonCalback(Context context) {
        this.context = context;
    }

    public AppJsonCalback(Context context,Type type) {
        super(type);
        this.context = context;
    }


    @Override
    public void onResponse(JsonResponse response) {
        if (response.getCode() == 200){
            onResult(response);
        }else {
            onResultError(response);
        }
    }

    /**
     * 返回码正确.
     * @param response
     */
    public void onResult(JsonResponse<? extends Object> response){

    }

    /**
     * 返回码错误.
     * @param response
     */
    public void onResultError(JsonResponse<? extends Object> response){
        if (response.getCode() == 999){
            return;
        }
        if(response.getCode()==53001){
            ToastUtils.toast(getContext(),"登录状态失效，请重新进行登录！");
            return;
        }
        if (StringUtil.isNotBlank(response.getMessage())){
            ToastUtils.toast(getContext(),response.getMessage());
        }


    }


    /**
     * 简单处理网络访问错误.
     * @param call
     * @param e
     */
    @Override
    public void onError(Call call, Exception e) {
        super.onError(call, e);
        if (!NetWorkUtil.isNetworkAvailable(getContext())){
            ToastUtils.toast(context!=null ? context:getContext(),"网络连接失败");
        } else {
            ToastUtils.toast(context!=null ? context:getContext(),"服务器错误");
        }
    }

    /**
     * 获取Context.
     * @return
     */
    public Context getContext(){
        return context==null?MyApplication.context:context;
    }

}
