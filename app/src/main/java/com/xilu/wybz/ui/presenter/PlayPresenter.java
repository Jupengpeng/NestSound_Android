package com.xilu.wybz.ui.presenter;

import android.content.Context;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.IPlayView;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class PlayPresenter extends com.xilu.wybz.presenter.BasePresenter<IPlayView> {


    public PlayPresenter(Context context, IPlayView iView) {
        super(context, iView);
    }

//    public void getPlayDetail(String userId, String music_id, String com, String gedanid) {
//        params = new HashMap<>();
//        params.put("uid",userId);
//        params.put("openmodel", PrefsUtil.getInt("playmodel",context)+"");
//        params.put("id",music_id);
//        params.put("gedanid",gedanid);
//        params.put("com",com);
//        httpUtils.get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
//            @Override
//            public void onResponse(String response) {
//                iView.getMusicSuccess(response);
//            }
//
//            @Override
//            public void onError(Call call, Exception e) {
//                iView.getMusicFail();
//            }
//        });
//    }


    public void setCollectionState(String userId, String music_id, int target_uid) {
        params = new HashMap<>();
        params.put("uid", userId);
        params.put("work_id", music_id);
        params.put("target_uid", target_uid+"");
        params.put("wtype", "1");
        httpUtils.get(MyHttpClient.getWorkFovUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                iView.collectionMusicSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.collectionMusicFail(e.getMessage());
            }
        });
    }

    public void setZambiaState(String userId, String music_id, int target_uid) {
        params = new HashMap<>();
        params.put("uid", userId);
        params.put("work_id", music_id);
        params.put("target_uid", target_uid+"");
        params.put("wtype", "1");
        httpUtils.post(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
            public void onResponse(String response) {
                iView.zambiaMusicSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.zambiaMusicFail();
            }
        });
    }

}
