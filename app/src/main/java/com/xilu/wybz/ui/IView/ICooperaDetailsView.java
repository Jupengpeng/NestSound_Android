package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.bean.PreinfoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public interface ICooperaDetailsView extends IBaseView {

    void showCooperaDetailsBean(CooperaDetailsBean cooperaDetailsBean);
    void showCooperaCommentList(List<CooperaDetailsBean.CommentListBean> commentListBean);
    void showCooperaCompleteList(List<CooperaDetailsBean.CompleteListBean> completeListBeen);

    void collectSuccess(int type);

    void showpreinfoBean(PreinfoBean preinfoBean);
}
