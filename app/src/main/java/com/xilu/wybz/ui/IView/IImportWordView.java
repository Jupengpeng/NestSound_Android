package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.bean.ZambiaBean;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface IImportWordView extends IBaseView{
    void showLyricsData(List<WorksData> worksDataList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
