package com.xilu.wybz.http.callback;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/26.
 */
public class AppJsonCalback extends JsonCallback {

    protected Context context;

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
            ToastUtils.toastLong(context,"数据解码失败");
            return;
        }
        if(response.getCode()==53001){
            ToastUtils.toastLong(context,"登录状态失效，请重新进行登录！");
            return;
        }
        if (StringUtils.isNotBlank(response.getMessage())){
            ToastUtils.toastLong(context,response.getMessage());
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
        e.printStackTrace();
        if (e.getMessage().contains("timeout")){
            Log.i("url",e.getMessage());
            ToastUtils.toast(context,"链接超时");
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(context)){
            ToastUtils.toast(context,"网络连接失败");
        } else {
            ToastUtils.toast(context,"服务器错误"+e.getClass().getCanonicalName());
        }
    }

}
