package com.xilu.wybz.ui.IView;

/**
 * Created by Administrator on 2016/6/6.
 */
public interface IDownloadMusicView extends IBaseView{

    void downloadSuccess(String message);
    void downloadFailed(String message);
    void downloadProgress(int progress);
}
