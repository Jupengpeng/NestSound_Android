package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.FindSongBean;
import com.xilu.wybz.bean.StartBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public interface IStarView extends IBaseView {
    void showData(List<StartBean> startBeanList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
