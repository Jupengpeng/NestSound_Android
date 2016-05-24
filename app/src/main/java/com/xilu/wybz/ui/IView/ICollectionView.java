package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.ZambiaBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ICollectionView extends IBaseView {

    void showCollectionData(List<CollectionBean> collectionBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
