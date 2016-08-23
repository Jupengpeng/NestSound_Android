package com.xilu.wybz.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PermissionUtils;

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
        File file = new File(path);
        if(!new File(file.getParent()).exists())new File(file.getParent()).mkdirs();
        httpUtils.getFile(imageUrl, new FileCallBack(file.getParent(),file.getName()) {
            @Override
            public void inProgress(float progress, long total) {
            }

            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(File response) {
                if (response != null && response.exists()){
                    iView.setPic(path);
                } else {
                    return;
                }
            }
        });
    }
}
