package com.xilu.wybz.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PermissionUtils;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/7/29.
 */
public class DownPicPresenter extends BasePresenter<ILoadPicView>{
    public DownPicPresenter(Context context, ILoadPicView iView) {
        super(context, iView);
    }

    public void downLoadPic(String imageUrl, String path){
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
                iView.setPic(path, bmp);
            }
        });
    }
}
