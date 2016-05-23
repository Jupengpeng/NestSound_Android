package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.ISongView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/8.
 */
public class SongPresenter extends com.xilu.wybz.presenter.BasePresenter<ISongView> {

    public SongPresenter(Context context, ISongView iView) {
        super(context, iView);
    }

    /*
    * orderType 1=最新(或不填)，2=热门
     */
    public void getWorkList(String userId,int type) {
        params = new HashMap<>();
        params.put("uid",userId);
        httpUtils.get(type==1?MyHttpClient.getFindSongList():MyHttpClient.getFindLyricsList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.showErrorView();
            }

            @Override
            public void onResponse(String response) {
                if(ParseUtils.checkCode(response)){
                    try {
                        String redList = new JSONObject(response).getJSONObject("data").getString("redList");
                        String newList = new JSONObject(response).getJSONObject("data").getString("newList");
                        List<WorksData> redsDatas = ParseUtils.getWorksData(context, redList);
                        List<WorksData> newsDatas = ParseUtils.getWorksData(context, newList);
                        iView.showNewSong(newsDatas);
                        iView.showHotSong(redsDatas);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
