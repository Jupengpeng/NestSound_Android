package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.MineBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface IMineView extends IBaseView {

    void  showMineList(List<MineBean> mineBeanList);
}
