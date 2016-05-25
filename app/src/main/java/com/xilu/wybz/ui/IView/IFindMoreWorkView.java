package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface IFindMoreWorkView extends IBaseView {

    void showWorkData(List<WorksData> worksDataList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
