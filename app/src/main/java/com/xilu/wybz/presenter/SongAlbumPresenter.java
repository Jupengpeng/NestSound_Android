package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.GleeDetailBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IRecSongView;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.ParseUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/4/24.
 * 推荐歌单
 */
public class SongAlbumPresenter extends BasePresenter<IRecSongView> {

    public SongAlbumPresenter(Context context, IRecSongView iView) {
        super(context, iView);
    }

    public void getMusicList(String itemid,int page) {
        params = new HashMap<>();
        params.put("id",itemid);
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getGleeDetailUrl(), params, new MyStringCallback() {
            @Override
            public void onBefore(Request request) {
                iView.showProgressBar();
            }

            @Override
            public void onAfter() {
                iView.hideProgressBar();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.showErrorView();
            }

            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    try {
                        String data = new JSONObject(response).getString("data");
                        GleeDetailBean gleeDetailBean = new Gson().fromJson(data, GleeDetailBean.class);
                        if (gleeDetailBean.workList.size() == 0) {
                            iView.showErrorView();
                        } else {
                            iView.showSongDetail(gleeDetailBean);
                        }
                    } catch (Exception e) {
                        iView.showErrorView();
                    }
                } else {
                    iView.showErrorView();
                }
            }

        });
    }
}
