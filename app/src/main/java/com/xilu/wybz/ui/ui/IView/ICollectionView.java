package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ICollectionView extends com.xilu.wybz.ui.IView.IBaseView {

    void showCollectionData(List<CollectionBean> collectionBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
