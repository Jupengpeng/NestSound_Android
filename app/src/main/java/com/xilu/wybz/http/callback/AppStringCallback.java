package com.xilu.wybz.http.callback;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.Response;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.ToastUtils;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/5/25.
 */
public class AppStringCallback extends MyStringCallback {

    protected Context context = null;
    protected Type type = null;

    public AppStringCallback(){

    }

    public AppStringCallback(Context context){
        this.context = context;
    }

    public AppStringCallback(Context context, Type type){
        this.context =context;
        this.type = type;
    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);
        Response<Object> result;
        try{
            result = new Gson().fromJson(response,type != null ? type:getDataType());
        } catch (Exception e){
//            e.printStackTrace();
            result = new Response<>();
            result.setCode(999);
            result.setMessage("Json decode error.");
            result.setError(e.toString());
        }

        if (result.getCode() == 200){
            onResponse(result);
            return;
        }

        if (result.getCode() == 999){
//            onResponse(result);
            return;
        }

        if (!TextUtils.isEmpty(result.getMessage())) {
            ToastUtils.toast(context != null ? context:getContext(), result.getMessage());
        }
    }


    public void onResponse(Response<? extends Object> response) {
//        System.out.print(response.toString());
    }


    /**
     * 获取Data的类型.
     * @return
     */
    public Type getDataType(){
        return new TypeToken<Response<String>>(){}.getType();
    }

    /**
     * 获取Context.
     * @return
     */
    public Context getContext(){
        return MyApplication.context;
    }


}
