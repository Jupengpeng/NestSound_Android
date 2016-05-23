package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public interface ISongView extends com.xilu.wybz.ui.IView.IBaseView {
    void showNewSong(List<WorksData> newWorksDatas);
    void showHotSong(List<WorksData> hotWorksDatas);
    void showErrorView();
}
