package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ITempleateListLrcView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class TemplateLrcListPresenter extends BasePresenter<ITempleateListLrcView>{

    public TemplateLrcListPresenter(Context context, ITempleateListLrcView iView) {
        super(context, iView);
    }

    public void getTemplateList(int page){
        params = new HashMap<>();
        params.put("page",""+page);

        httpUtils.post(MyHttpClient.getLyricstemplateList(),params, new AppJsonCalback(context){
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<LyricsDraftBean> list = response.getData();
                if (list == null || list.size()==0){
                    iView.onError("没有更多数据了");
                } else {
                    iView.onSuccess(list);
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                String msg = response.getMessage();
                iView.onError(msg);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<LyricsDraftBean>>(){}.getType();
            }
        });

    }




}
