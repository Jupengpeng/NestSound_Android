package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public interface ISongView extends IBaseView{
    void showNewSong(List<WorksData> newWorksDatas);
    void showHotSong(List<WorksData> hotWorksDatas);
    void showErrorView();
    void loadingFinish();
}
