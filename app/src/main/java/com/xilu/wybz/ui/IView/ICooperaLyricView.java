package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CooperaLyricBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public interface ICooperaLyricView extends IBaseView {

    void showCooperaLyricList(List<CooperaLyricBean> lyricBeanList);

    void updatesuccess();

    void noData();

    void noMoreData();
}
