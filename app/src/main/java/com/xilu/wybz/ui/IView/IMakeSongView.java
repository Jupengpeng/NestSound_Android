package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.TruningMusicBean;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface IMakeSongView extends IBaseView {

    void setLoadProgress(int progress);

    void uploadSuccess(String message);

    void uploadFailed(String message);

    void tuningMusicSuccess(TruningMusicBean bean);

    void tuningMusicFailed();
}
