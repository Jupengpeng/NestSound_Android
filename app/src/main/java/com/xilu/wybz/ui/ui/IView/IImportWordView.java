package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface IImportWordView extends com.xilu.wybz.ui.IView.IBaseView {
    void showLyricsData(List<WorksData> worksDataList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
