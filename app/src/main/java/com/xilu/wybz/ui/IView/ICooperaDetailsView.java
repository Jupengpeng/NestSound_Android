package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CooperaDetailsBean;

/**
 * Created by Administrator on 2016/10/26.
 */

public interface ICooperaDetailsView extends IBaseView {

    void showCooperaDetailsBean(CooperaDetailsBean cooperaDetailsBean);

    void collectSuccess();
}
