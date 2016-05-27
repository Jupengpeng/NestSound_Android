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

    public void setCollectionState(int music_id, int target_uid) {
        params = new HashMap<>();
        params.put("uid",PrefsUtil.getUserId(context)+"");;
        params.put("work_id", music_id+"");
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

    public void setZambiaState(int music_id, int target_uid) {
        params = new HashMap<>();
        params.put("uid",PrefsUtil.getUserId(context)+"");;
        params.put("work_id", music_id+"");
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
