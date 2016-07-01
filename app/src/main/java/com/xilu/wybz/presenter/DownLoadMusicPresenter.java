package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.ui.IView.IDownloadMusicView;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DownloadMusicPresenter extends BasePresenter<IDownloadMusicView> {


    protected File file;
    protected String filename;

    public DownloadMusicPresenter(Context context, IDownloadMusicView iView) {
        super(context, iView);
    }


    @Override
    public void init() {
//        super.init();


    }

    public void downloadFile(String url, String fileName){

        this.file = new File(fileName);
        this.filename = fileName;
        Log.d("url", "url:"+url);
        if (!url.startsWith("http")){
            return;
        }

        httpUtils.getFile(url, new FileCallBack(file.getParent(),file.getName()) {
            @Override
            public void inProgress(float progress, long total) {

                iView.downloadProgress((int)(100*progress));
                Log.d("url","loadFile progress:"+progress);

            }

            @Override
            public void onError(Call call, Exception e) {
               iView.downloadFailed(e.toString());
            }

            @Override
            public void onResponse(File response) {
                if (response != null && response.exists()){
                    Log.d("url","loadFile ok..");
                } else {
                    return;
                }
                try{
                    response.renameTo(new File(filename+".mp3"));
                    iView.downloadSuccess(filename+".mp3");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }



}
