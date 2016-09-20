package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.StarBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public interface IStarView extends IBaseView {
    void showData(List<StarBean> startBeanList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
