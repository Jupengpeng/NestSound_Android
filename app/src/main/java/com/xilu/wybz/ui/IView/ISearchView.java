package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface ISearchView extends IBaseView{
    void showWorksData(List<WorksData> worksDataList);
    void showUserData(List<FansBean> userBeenList);
    void loadFail();
    void loadNoMore();
    void loadNoData();

}
