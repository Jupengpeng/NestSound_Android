package com.xilu.wybz.ui.IView;

/**
 * Created by Administrator on 2016/6/6.
 */
public interface IUploadMusicView extends IBaseView {

    void uploadSuccess(String message);

    void uploadFailed(String message);

    void uploadProgress(int progress);
}
