package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.FindSongBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
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
public class SongPresenter extends BasePresenter<ISongView> {
    public SongPresenter(Context context, ISongView iView) {
        super(context, iView);
    }
    /*
    * orderType 1=最新(或不填)，2=热门
     */
    public void getWorkList(int userId,int type) {
        params = new HashMap<>();
        params.put("uid",userId+"");
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
                        FindSongBean findSongBean = new Gson().fromJson(new JSONObject(response).getString("data"),FindSongBean.class);
                        if(findSongBean.newList!=null)
                        iView.showNewSong(findSongBean.newList);
                        if(findSongBean.redList!=null)
                        iView.showHotSong(findSongBean.redList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
