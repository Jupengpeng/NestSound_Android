package com.xilu.wybz.presenter;

import android.content.Context;
import android.os.Handler;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.mock.MockCallback;
import com.xilu.wybz.mock.PreservationListMock;
import com.xilu.wybz.mock.ProductAllListMock;
import com.xilu.wybz.ui.IView.IDefaultListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/13.
 */
public class DefaultListPresenter<T> extends BasePresenter<IDefaultListView>{

    public  boolean mockAble = false;
    public Type resultType = null;
    private String url;
    /**
     *
     * @param context
     * @param iView
     */
    public DefaultListPresenter(Context context, IDefaultListView iView) {
        super(context, iView);
        params = new HashMap<>();
    }

    /**
     * setUrl.
     * @param url
     */
    public void setUrl(String url){
        this.url = url;
    }

    public void setParams(Map<String,String> params){
        this.params = params;
    }

    public void getData(int page){
        params.put("page",""+page);
        getData(url,params);
    }
    public void getData(Map<String,String> params){
        getData(url,params);
    }

    public void getData(String url,Map<String,String> params){

        if (mockAble){
            mock(url);
            return;
        }

        httpUtils.post(url,params, new AppJsonCalback(context){
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<T> list = response.getData();
                if (list == null){
                    list = new ArrayList<>();
                }
                iView.onSuccess(list);
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

        MockCallback mockCallback = null;

        if (url.equals(MyHttpClient.getpreservationList())){
            mockCallback = new PreservationListMock();
        } else if (url.equals(MyHttpClient.getProductAllList())){
            mockCallback = new ProductAllListMock();
        }

        final MockCallback mock = mockCallback;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iView.onSuccess(mock.getMockList());
            }
        }, 400);

    }




}
