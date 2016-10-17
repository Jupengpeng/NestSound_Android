package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ISampleView;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/30.
 */

public class SamplePresenter<T> extends BasePresenter<ISampleView<T>> {

    public boolean mockAble = false;
    public String url;
    public Type resultType = null;


    /**
     * SamplePresenter.
     * @param context
     * @param iView
     */

    public SamplePresenter(Context context, ISampleView<T> iView) {
        super(context, iView);
    }

    public void getData(Map<String, String> params) {
        getData(url, params);
    }

    public void getData(String url, Map<String, String> params) {

        if (mockAble) {
            mock(url);
            return;
        }

        httpUtils.post(url, params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                try {
                    T data = response.getData();
                    iView.onSuccess(data);
                } catch (Exception e){
                    iView.onError(response.getMessage());
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                String msg = response.getMessage();
                iView.onError(msg);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.onError(e.getMessage());
            }

            @Override
            public Type getDataType() {
                return resultType;
            }
        });

    }

    public void mock(String url){




    }


}
