package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface IOtherCenterListView extends IBaseView{
    void showWorksData(List<WorksData> worksDataList);
    void loadFail();
    void loadNoData();
    void loadNoMore();
}
