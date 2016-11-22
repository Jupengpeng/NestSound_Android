package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.message.SystemMessageBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ICooperaMsgView extends IBaseView {

    void showSystemData(List<SystemMessageBean> systemBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
