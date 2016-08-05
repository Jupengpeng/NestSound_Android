package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IForgetPwdView;
import com.xilu.wybz.ui.IView.IMusicDetailView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class LoadMusicDetailPresenter extends BasePresenter<IMusicDetailView> {
    public LoadMusicDetailPresenter(Context context, IMusicDetailView iView) {
        super(context, iView);
    }

    public void loadMusicDetail(int id) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("id", id + "");
        httpUtils.get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                WorksData currMdb = ParseUtils.getWorkData(context, response);
                if (currMdb != null && currMdb.itemid > 0) {
                    iView.showMusicDetail(currMdb);
                }else{
                    iView.loadFail();
                }
            }
        });
    }

}
