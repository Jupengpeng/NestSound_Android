package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CollectBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface ICollectView extends IBaseView {

    void showCollectList(List<CollectBean> collectBeanList);
    void noData();
    void noMoreData();
    void cancleCollectSuccess(int pos);
}
