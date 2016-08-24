package com.xilu.wybz.ui.IView;

import android.graphics.Bitmap;

/**
 * Created by hujunwei on 16/7/29.
 */
public interface ILoadPicView extends IBaseView {
    void setPic(String path);
    void downPicFail();
}
