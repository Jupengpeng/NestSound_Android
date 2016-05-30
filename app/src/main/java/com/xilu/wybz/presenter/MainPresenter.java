package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MainBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHomeView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 2016/4/28.
 */
public class MainPresenter extends BasePresenter<IHomeView> {
    public MainPresenter(Context context, IHomeView iView) {
        super(context, iView);
    }

    public void getHomeData() {
        httpUtils.get(MyHttpClient.getHomeUrl(),null, new MyStringCallback() {
            @Override
            public void onAfter() {
                super.onAfter();
                iView.loadDataFinish();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadDataFail(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                MainBean mainBean = ParseUtils.getMainBean(context,response);
                iView.showMainData(mainBean);
            }
        });
    }
}
