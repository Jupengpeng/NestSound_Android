package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.ui.IView.IMakeSongView;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MakeSongPresenter extends BasePresenter<IMakeSongView> {

    public MakeSongPresenter(Context context, IMakeSongView iView) {
        super(context, iView);
    }


    public void loadFile(String url, String fileName){

        httpUtils.getFile("", new FileCallBack("","") {
            @Override
            public void inProgress(float progress, long total) {

            }

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(File response) {

            }
        });

    }





}
