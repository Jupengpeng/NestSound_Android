package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.ui.IView.IBaseView;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface IZanView extends IBaseView {

    void showZambiaData(List<ZambiaBean> zambiaBeanList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
