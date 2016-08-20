package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.LyricsDraftBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public interface IJoinUserView extends IBaseView {

    void showData(List<JoinUserBean> list);

    void loadFail();

    void loadNoMore();

    void loadNoData();
}
