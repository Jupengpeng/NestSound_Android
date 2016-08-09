package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.FindSongBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public interface ISongView extends IBaseView{
    void showFindSong(FindSongBean findSongBean);
    void showErrorView();
    void loadingFinish();
}
