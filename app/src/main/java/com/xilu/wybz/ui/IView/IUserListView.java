package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public interface IUserListView {

    void addListDatas(List<WorksData> datas);
    void refreshListDatas(List<WorksData> datas);

    void showNoNet();
    void showNoData();
    void showContent();
    void showLoadMore();
    void cancelLoadMore();

}
