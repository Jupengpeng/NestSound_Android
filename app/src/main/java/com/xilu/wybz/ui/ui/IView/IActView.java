package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.ui.IView.IBaseView;

import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public interface IActView extends IBaseView {
    void showActList(List<ActBean> actBeanList);
    void showNoData();
    void showNoMore();
    void showErrorView();
}
