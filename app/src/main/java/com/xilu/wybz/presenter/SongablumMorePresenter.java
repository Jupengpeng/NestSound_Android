package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ISongablumMoreView;
import com.xilu.wybz.utils.ParseUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/5/20.
 */
public class SongablumMorePresenter extends BasePresenter<ISongablumMoreView> {

    public SongablumMorePresenter(Context context, ISongablumMoreView iView) {
        super(context, iView);
    }

    public void loadData(String userId, int page) {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("uid", userId);
        httpUtils.get(MyHttpClient.getGleeListUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                List<SongAlbum> songAlbumList = ParseUtils.getSongAlbumsData(context, response);
                if (songAlbumList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showSongAblumData(songAlbumList);
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
