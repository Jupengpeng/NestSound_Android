package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface IMyWorkView extends IBaseView{
    void showData(List<WorksData> worksDataList);

    void loadFail();

    void loadNoMore();

    void loadNoData();

    void attendSuccess();

    void attendFail();
}
