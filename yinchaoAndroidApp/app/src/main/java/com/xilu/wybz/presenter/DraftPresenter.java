package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ISimpleView;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/13.
 */
public class DraftPresenter extends BasePresenter<ISimpleView> {

    public DraftPresenter(Context context, ISimpleView iView) {
        super(context, iView);
    }


    @Override
    public void init() {

    }



    public void saveDraft(LyricsDraftBean bean){

        params = new HashMap<>();
        params.put("title",bean.title);
        params.put("content",bean.content);
        params.put("draftdesc",bean.draftdesc);
        params.put("uid",""+ PrefsUtil.getUserId(context));

        httpUtils.post(MyHttpClient.getDraftSave(),params, new AppJsonCalback(context){
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                    iView.onSuccess(1,response.getMessage());
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                String msg = response.getMessage();
                iView.onError(1,response.getMessage());
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.onError(1,e.getMessage());
            }
        });

    }



    public void deleteDraft(String id){

        params = new HashMap<>();
        params.put("id",""+id);
        httpUtils.post(MyHttpClient.getDraftDelete(),params, new AppJsonCalback(context){
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                iView.onSuccess(2,response.getMessage());
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                String msg = response.getMessage();
                iView.onError(2,response.getMessage());
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.onError(2,e.getMessage());
            }
        });
    }





}
