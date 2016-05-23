package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.SystemBean;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ISystemMsgView extends com.xilu.wybz.ui.IView.IBaseView {

    void showSystemData(List<SystemBean> systemBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
