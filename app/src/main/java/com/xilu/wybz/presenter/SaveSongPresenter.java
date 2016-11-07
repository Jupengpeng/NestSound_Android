package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.bean.ShareResponseBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.ISaveSongView;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/5.
 */
public class SaveSongPresenter extends BasePresenter<ISaveSongView> {

    public SaveSongPresenter(Context context, ISaveSongView iView) {
        super(context, iView);
    }


    public void saveSong(WorksData worksData) {
        params = new HashMap<>();
        try {

            params.put("uid", worksData.uid + "");
            params.put("title", worksData.title);
            params.put("author", worksData.author);
            params.put("lyrics", worksData.lyrics);
            params.put("createtype", "HOT");
            params.put("useheadset", worksData.useheadset);//耳机
            params.put("hotid", "" + worksData.hotid);
            params.put("pic", worksData.pic);
            params.put("is_issue", "" + worksData.is_issue);
            params.put("mp3", worksData.musicurl);
            params.put("effect", "" + worksData.effect);
            params.put("diyids", worksData.detail);

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        Log.e("url", params.toString());

        httpUtils.postLong(MyHttpClient.getSaveSongUrl(), params, new AppJsonCalback(context) {

                    @Override
                    public Type getDataType() {
                        return ShareResponseBean.class;
                    }

                    @Override
                    public void onResult(JsonResponse<? extends Object> response) {
                        super.onResult(response);
                        ShareResponseBean sharebean = response.getData();
                        if (sharebean != null) {
                            iView.saveWordSuccess(sharebean);
                        } else {
                            iView.saveWordFail();
                        }
                    }

                    @Override
                    public void onResultError(JsonResponse<? extends Object> response) {
                        super.onResultError(response);
                        iView.saveWordFail();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        super.onError(call, e);
                        iView.saveWordFail();
                    }
                }
        );
    }

    public void saveCooperaSong(WorksData worksData, PreinfoBean preinfoBean,int did) {
        params = new HashMap<>();
        try {
            params.put("did", did + "");
            params.put("lUid", preinfoBean.getlUid() + "");
            params.put("lUsername", preinfoBean.getlUsername());
            params.put("wUsername", preinfoBean.getwUsername());
            params.put("wUid", preinfoBean.getwUid() + "");

            params.put("title", preinfoBean.getTitle());
            params.put("lyrics", preinfoBean.getLyrics());
            params.put("createtype", "HOT");
            params.put("useheadset", worksData.useheadset);//耳机
            params.put("hotid", "" + worksData.hotid);
            params.put("pic", worksData.pic);
            params.put("is_issue", "" + worksData.is_issue);
            params.put("mp3", worksData.musicurl);
//            params.put("effect", "" + worksData.effect);
            params.put("diyids", worksData.detail);

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        Log.e("url", params.toString());

        httpUtils.postLong(MyHttpClient.publishSong(), params, new AppJsonCalback(context) {

                    @Override
                    public Type getDataType() {
                        return ShareResponseBean.class;
                    }

                    @Override
                    public void onResult(JsonResponse<? extends Object> response) {
                        super.onResult(response);
                        ShareResponseBean sharebean = response.getData();
                        if (sharebean != null) {
                            iView.saveWordSuccess(sharebean);
                        } else {
                            iView.saveWordFail();
                        }
                    }

                    @Override
                    public void onResultError(JsonResponse<? extends Object> response) {
                        super.onResultError(response);
                        iView.saveWordFail();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        super.onError(call, e);
                        iView.saveWordFail();
                    }
                }
        );
    }

}
