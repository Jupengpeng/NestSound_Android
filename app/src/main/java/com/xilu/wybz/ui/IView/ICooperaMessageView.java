package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CooperaMessageBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */

public interface ICooperaMessageView extends IBaseView {

    void showCooperaMessageList(List<CooperaMessageBean> cooperaMessageBeanList);

    void loadFail();

    void loadNoMore();

    void loadNoData();

    void commentSuccess(int id);

    void commentFail();

    void delSuccess(int pos);

    void delFail();
}
