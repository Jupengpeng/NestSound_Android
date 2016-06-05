package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.ISaveSongView;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/5.
 */
public class SaveSongPresenter extends BasePresenter<ISaveSongView> {

    public SaveSongPresenter(Context context, ISaveSongView iView) {
        super(context, iView);
    }


    public void saveSong(WorksData worksData) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("uid", PrefsUtil.getUserId(context)+"");
            map.put("title", worksData.title);
            map.put("author", worksData.author);
            map.put("lyrics", worksData.lyrics);
            map.put("createtype", "HOT");
            map.put("useheadset", worksData.useheadset);//耳机
            map.put("hotid", ""+worksData.hotid);
            map.put("pic", worksData.pic);
            map.put("is_issue", ""+worksData.is_issue);
            map.put("mp3", worksData.recordmp3 );
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        httpUtils.post(MyHttpClient.getSaveSongUrl(), map, new AppStringCallback(context) {
                    @Override
                    public void onResponse(JsonResponse<? extends Object> response) {
                        super.onResponse(response);
                        iView.saveWordSuccess(response.getData());
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        super.onError(call, e);
                        iView.saveWordFail();
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        iView.onFinish();
                    }
                }
        );
    }



}
