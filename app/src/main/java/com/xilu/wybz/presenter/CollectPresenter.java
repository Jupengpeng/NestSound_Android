package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ICollectView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/24.
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    public CollectPresenter(Context context, ICollectView iView) {
        super(context, iView);
    }

    public void getCollectList(int page) {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getFovlist(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<CollectBean> collectBeanList = response.getData();
                iView.showCollectList(collectBeanList);

            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<CollectBean>>() {
                }.getType();
            }
        });
    }
}
