package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.StarBean;
import com.xilu.wybz.bean.StarInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public interface IStarInfoView extends IBaseView {
    void showData(StarInfoBean starInfoBean);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
