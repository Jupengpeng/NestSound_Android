package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.HotBean;

/**
 * Created by hujunwei on 16/8/8.
 */
public interface IHotCatalogView extends IBaseView {
    void showHotCatalog(HotBean hotBean);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
