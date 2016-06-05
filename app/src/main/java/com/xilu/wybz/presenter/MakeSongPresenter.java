package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

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

    File file;
    String filename;
    public void loadFile(String url, String fileName){

        this.file = new File(fileName);
        this.filename = fileName;
        Log.d("url", "url:"+url);
        if (!url.startsWith("http")){
            return;
        }

        httpUtils.getFile(url, new FileCallBack(file.getParent(),file.getName()) {
            @Override
            public void inProgress(float progress, long total) {

                iView.setLoadProgress((int)(100*progress));
                Log.d("url","loadFile ok..:"+progress);

                if (progress == 1.0f){
                    Log.d("url","loadFile ok..");
                    try{
                        file.renameTo(new File(filename+".mp3"));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
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
