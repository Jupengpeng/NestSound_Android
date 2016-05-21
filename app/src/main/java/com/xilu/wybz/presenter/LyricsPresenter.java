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

    public void getLyric(final String id, String userId) {
        httpUtils.get(MyHttpClient.getLyricsdisplay(id, userId), new MyStringCallback() {
            @Override
            public void onResponse(String result) {
                try {
                    if (ParseUtils.checkCode(result)) {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        String infoJson = dataJson.getString("info");
                        WorksData lyricsdisplayBean = new Gson().fromJson(infoJson, WorksData.class);
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

    public void zan(String id, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("userid", userId);
        params.put("status", "1");
        httpUtils.postUrl(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
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
