package com.xilu.wybz.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.utils.BitmapUtils;

import java.io.File;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/7/29.
 */
public class DownPicPresenter extends BasePresenter<ILoadPicView>{
    public DownPicPresenter(Context context, ILoadPicView iView) {
        super(context, iView);
    }

    public void downLoadPic(String imageUrl, String path){
        if(!imageUrl.startsWith("http"))imageUrl = MyHttpClient.QINIU_URL+imageUrl;
        File file = new File(path);
        if(!new File(file.getParent()).exists())new File(file.getParent()).mkdirs();
        httpUtils.getFile(imageUrl, new FileCallBack(file.getParent(),file.getName()) {
            @Override
            public void inProgress(float progress, long total) {
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.downPicFail();
            }
            @Override
            public void onResponse(File response) {
                if (response != null && response.exists()){
                    iView.setPic(path);
                } else {
                    iView.downPicFail();
                }
            }
        });
    }

    public void downLoadBitmap(String imageUrl, String path){
        if(!imageUrl.startsWith("http"))imageUrl = MyHttpClient.QINIU_URL+imageUrl;
        File file = new File(path);
        if(!new File(file.getParent()).exists())new File(file.getParent()).mkdirs();
        httpUtils.getImage(imageUrl, new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }
            @Override
            public void onResponse(Bitmap response) {
                if(response==null){
                    return;
                }
                Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(response, 200), 30);
                BitmapUtils.toSaveFile(path,bmp);
                bmp.recycle();
                iView.setPic(path);
            }
        });
    }
}
