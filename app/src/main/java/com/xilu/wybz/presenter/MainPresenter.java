package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHomeView;

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
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.loadDataStart();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.loadDataFinish();
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.loadDataFail(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                iView.loadDataSuccess(response);
            }
        });

    }
}
