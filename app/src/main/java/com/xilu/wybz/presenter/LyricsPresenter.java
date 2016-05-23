package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILyricsView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/4/5.
 */
public class LyricsPresenter extends BasePresenter<ILyricsView> {
    public LyricsPresenter(Context context, ILyricsView iView) {
        super(context, iView);
    }

    public void getLyric(String id, String userId) {
        params = new HashMap<>();
        params.put("uid",userId);
        params.put("id",id);
        httpUtils.get(MyHttpClient.getLyricsdisplay(), params, new MyStringCallback() {
            @Override
            public void onResponse(String result) {
                try {
                    if (ParseUtils.checkCode(result)) {
                        String dataJson = new JSONObject(result).getString("data");
                        WorksData lyricsdisplayBean = new Gson().fromJson(dataJson, WorksData.class);
                        iView.loadLyrics(lyricsdisplayBean);
                    } else {
                        ParseUtils.showMsg(context, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    iView.showErrorView();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.showProgressBar();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.hideProgressBar();
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.showErrorView();
            }
        });
    }

    public void zan(String id, String userId, int target_uid) {
        Map<String, String> params = new HashMap<>();
        params.put("target_uid", target_uid+"");//作者id
        params.put("user_id", userId);
        params.put("work_id", id);
        params.put("wtype", "2");//2歌词 1歌曲
        httpUtils.post(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    if (ParseUtils.checkCode(response)) {
                        iView.zanSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.zanFail();
            }
        });
    }
}
