package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.FansBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public interface IFollowAndFansView extends IBaseView {
    void showFansData(List<FansBean> fansBeanList);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
