package com.xilu.wybz.presenter;

import android.content.Context;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IPlayView;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class PlayPresenter extends BasePresenter<IPlayView> {


    public PlayPresenter(Context context, IPlayView iView) {
        super(context, iView);
    }

    public void getPlayDetail(String userId, String music_id, String com, String gedanid) {
        params = new HashMap<>();
        params.put("uid",userId);
        params.put("openmodel", PrefsUtil.getInt("playmodel",context)+"");
        params.put("id",music_id);
        params.put("gedanid",gedanid);
        params.put("com",com);
        httpUtils.get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                iView.getMusicSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.getMusicFail();
            }
        });
    }

    public void getHotDetail(String hotId) {
        httpUtils.get(MyHttpClient.getHotDetailUrl(hotId), new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                iView.getHotSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.getHotFail();
            }
        });
    }

    public void setCollectionState(String userId, String music_id, int is_fov) {
        httpUtils.get(is_fov == 0 ? MyHttpClient.getAddFavUrl(music_id, userId) : MyHttpClient.getRemoveFavUrl(music_id, userId), new MyStringCallback() {
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

    public void setZambiaState(String userId, String music_id) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", userId);
        params.put("id", music_id);
        params.put("status", "2");
        httpUtils.postUrl(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
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
