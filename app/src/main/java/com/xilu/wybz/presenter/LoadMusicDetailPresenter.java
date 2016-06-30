package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IForgetPwdView;
import com.xilu.wybz.ui.IView.IMusicDetailView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class LoadMusicDetailPresenter extends BasePresenter<IMusicDetailView> {
    public LoadMusicDetailPresenter(Context context, IMusicDetailView iView) {
        super(context, iView);
    }

    public void loadMusicDetail(int id) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("openmodel", "1");
        params.put("id",id+"");
        params.put("gedanid","0");
        params.put("com","");
        httpUtils.get(MyHttpClient.getSmsCodeUrl(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                iView.showMusicDetail(response.getData());
            }

            @Override
            public Type getDataType() {
                return WorksData.class;
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }
        });
    }

}
