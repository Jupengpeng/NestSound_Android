package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

/**
 * Created by hujunwei on 16/6/29.
 */
public interface IMusicDetailView extends IBaseView{
    void showMusicDetail(WorksData worksData);
    void loadFail();
}
