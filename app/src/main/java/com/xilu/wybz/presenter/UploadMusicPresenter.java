package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IUploadMusicView;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class UploadMusicPresenter extends BasePresenter<IUploadMusicView>{

    public UploadMusicPresenter(Context context, IUploadMusicView iView) {
        super(context, iView);
    }

    @Override
    public void init() {
//        super.init();
    }
    public void uploadMusicFile( String fileName) {

        httpUtils.uploadFile(MyHttpClient.getuploadmp3Url(), fileName, new AppJsonCalback(context) {


            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                iView.uploadProgress((int) (100 * progress));
                Log.d("upload", "inProgress:" + progress);
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                Log.d("upload", "upload ok:" + response.getData());
                iView.uploadSuccess(response.getData());
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.uploadFailed("onResultError");
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.uploadFailed(e.toString());
            }
        });
    }



}
