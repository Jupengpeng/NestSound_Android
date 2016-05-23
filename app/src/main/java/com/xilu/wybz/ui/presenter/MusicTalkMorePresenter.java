package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.IMusicTalkMoreView;
import com.xilu.wybz.ui.IView.ISongablumMoreView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/5/20.
 */
public class MusicTalkMorePresenter extends com.xilu.wybz.presenter.BasePresenter<IMusicTalkMoreView> {

    public MusicTalkMorePresenter(Context context, IMusicTalkMoreView iView) {
        super(context, iView);
    }
    public void loadData(String userId,int page){
        params = new HashMap<>();
        params.put("page",page+"");
        params.put("uid",userId);
        httpUtils.get(MyHttpClient.getGleeListUrl(),params,new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (ParseUtils.checkCode(response)) {
                    try {
                        String resultlist = new JSONObject(response).getString("data");
                        List<MusicTalk> mList = new Gson().fromJson(resultlist, new TypeToken<List<MusicTalk>>() {
                        }.getType());
                        if (mList.size() == 0) {
                            if (page == 1) {
                                iView.loadNoData();
                            } else {
                                iView.loadNoMore();
                            }
                        } else {
                            iView.showMusicTalkData(mList);
                        }

                    } catch (JSONException e) {
                        iView.loadNoData();
                    }

                } else {
                    ToastUtils.toast(context,ParseUtils.getMsg(response));
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFail();
            }
        });
    }

}
