package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ICooperationView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CooperationPresenter extends BasePresenter<ICooperationView> {


    public CooperationPresenter(Context context, ICooperationView iView) {
        super(context, iView);
    }

    public void getCooperationList(int page) {
        params = new HashMap<>();
        params.put("page", page + "");
//        params.put("token",PrefsUtil.getUserInfo(context).loginToken) ;

        httpUtils.post(MyHttpClient.getDemandlist(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);

            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<CooperationBean> cooperationBeanList = response.getData();
                if (page == 1) {
                    if (cooperationBeanList.size() == 0) {
                        iView.noData();
                    }
                } else {
                    if (cooperationBeanList.size() == 0 ) {
                        iView.noMoreData();
                    }
                }


                iView.showCooperation(cooperationBeanList);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<CooperationBean>>() {
                }.getType();
            }
        });
    }
    public void getPreinfo(int did) {
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("did", did + "");
        httpUtils.post(MyHttpClient.getPreinfo(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);

            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                PreinfoBean preinfoBean = response.getData();
                iView.showpreinfoBean(preinfoBean);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<PreinfoBean>() {
                }.getType();
            }
        });

    }

}
