package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.StarInfoBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IStarInfoView;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by June on 16/5/4.
 */
public class StarInfoPresenter extends BasePresenter<IStarInfoView>{
    public StarInfoPresenter(Context context, IStarInfoView iView) {
        super(context, iView);
    }
    public void getStarInfo(int uid, int page){
        params = new HashMap<>();
        params.put("uid",uid+"");
        params.put("page",page+"");
        httpUtils.post(MyHttpClient.getMusicianDetailUrl(), params, new AppJsonCalback(context){
            @Override
            public Type getDataType() {
                return StarInfoBean.class;
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                StarInfoBean starInfoBean = response.getData();
                if(starInfoBean==null)
                    iView.loadFail();
                else {
                    iView.showData(starInfoBean);
                }
            }
        });
    }
}
