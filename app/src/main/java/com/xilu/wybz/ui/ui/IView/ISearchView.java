package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface ISearchView extends com.xilu.wybz.ui.IView.IBaseView {
    void showWorksData(List<WorksData> worksDataList);
    void showUserData(List<UserBean> userBeenList);
    void loadFail();
    void loadNoMore();
    void loadNoData();

}
