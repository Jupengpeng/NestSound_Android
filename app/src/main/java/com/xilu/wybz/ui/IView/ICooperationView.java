package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.bean.PreinfoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public interface ICooperationView extends IBaseView {

    void showCooperation(List<CooperationBean> cooperationBeanList);

    void noMoreData();

    void showpreinfoBean(PreinfoBean preinfoBean);

    void noData();

}
