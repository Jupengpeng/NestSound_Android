package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.MatchWorkBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface IMatchWorkView extends IBaseView {

    void showWorkData(List<MatchWorkBean> worksDataList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
