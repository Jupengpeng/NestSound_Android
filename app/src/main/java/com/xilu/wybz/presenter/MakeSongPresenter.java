package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.TruningMusicBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.ui.IView.IMakeSongView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MakeSongPresenter extends BasePresenter<IMakeSongView> {

    public MakeSongPresenter(Context context, IMakeSongView iView) {
        super(context, iView);
    }

    File file;
    File cacheFile;
    String filename;
    public void loadFile(String url, String fileName){

        this.file = new File(fileName);
        this.cacheFile = new File(fileName+".temp");
        this.filename = fileName;
        Log.d("url", "url:"+url);
        if (!url.startsWith("http")){
            return;
        }
        cancelRequest();
        httpUtils.getFile(url, new FileCallBack(cacheFile.getParent(),cacheFile.getName()) {
            @Override
            public void inProgress(float progress, long total) {

                iView.setLoadProgress((int)(100*progress));
                Log.d("url","loadFile progress:"+progress);

                if (progress == 1.0f){
                    Log.d("url","loadFile ok..");
                    try{
                        cacheFile.renameTo(new File(filename));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.setLoadFailed();
            }

            @Override
            public void onResponse(File response) {

            }
        });

    }




    public void uploadmp3File( String fileName){

        httpUtils.uploadFile(MyHttpClient.getuploadmp3Url(),fileName, new AppJsonCalback(context){


            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                Log.d("upload","inProgress:"+progress);
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                Log.d("upload","upload ok:"+response.getData());
                iView.uploadSuccess(response.getData());
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.uploadFailed("");
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.uploadFailed("");
            }
        });

    }

//    createtype	是	string	录音类别：可能值：HOT/DIY
//    hotid	        是	int	伴奏的id
//    uid	        是	int	登录用户的id，做后面做行为分析使用
//    recordingsize	否	int	人声大小，默认1
//    bgmsize	    否	int	伴奏大小，默认1
//    useheadset	否	int	人声大小，默认1
//    musicurl

    public void tuningMusic(String userId, WorksData worksData){
        params = new HashMap<>();
        params.put("createtype","HOT");
        params.put("hotid",""+worksData.hotid);
        params.put("uid",userId);
        params.put("recordingsize","1");
        params.put("bgmsize","1");
        params.put("useheadset",worksData.useheadset);
        params.put("musicurl",worksData.musicurl);

        httpUtils.postLong(MyHttpClient.getTuningSongUrl(),params,new AppJsonCalback(context){

            @Override
            public Type getDataType() {
                return TruningMusicBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                TruningMusicBean bean = null;
                try{
                    bean = response.getData();
                } catch (Exception e){

                }
                if (bean != null){
                    iView.tuningMusicSuccess(bean);
                } else {
                    iView.tuningMusicFailed();
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.tuningMusicFailed();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.tuningMusicFailed();
            }


        });

    }

}
