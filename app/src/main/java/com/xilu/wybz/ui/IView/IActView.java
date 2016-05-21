package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.ActBean;

import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public interface IActView extends IBaseView{
    void showActList(List<ActBean> actBeanList);
    void showErrorView();
}
