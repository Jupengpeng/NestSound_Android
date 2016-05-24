package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.SystemBean;
import com.xilu.wybz.bean.ZambiaBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ISystemMsgView extends IBaseView {

    void showSystemData(List<SystemBean> systemBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
