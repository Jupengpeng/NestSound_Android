package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.FindSongBean;

/**
 * Created by June on 16/5/8.
 */
public interface ISongView extends IBaseView{
    void showFindSong(FindSongBean findSongBean);
    void showErrorView();
    void loadingFinish();
}
