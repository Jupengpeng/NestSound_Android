package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.HotCatalog;

import java.util.List;

/**
 * Created by hujunwei on 16/8/8.
 */
public interface IHotCatalogView extends IBaseView {
    void showHotCatalog(HotBean hotBean);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
