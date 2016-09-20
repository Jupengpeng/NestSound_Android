package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IDraftListView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/13.
 */
public class DraftListPresenter extends BasePresenter<IDraftListView>{

    public DraftListPresenter(Context context, IDraftListView iView) {
        super(context, iView);
    }
    public void getDraftList(int page){
        params = new HashMap<>();
        params.put("page",""+page);
        params.put("uid",""+ PrefsUtil.getUserId(context));

        httpUtils.post(MyHttpClient.getDraftList(),params, new AppJsonCalback(context){
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<LyricsDraftBean> list = response.getData();
                if (list == null){
                    list = new ArrayList<LyricsDraftBean>();
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
                return new TypeToken<List<LyricsDraftBean>>(){}.getType();
            }
        });

    }



}
