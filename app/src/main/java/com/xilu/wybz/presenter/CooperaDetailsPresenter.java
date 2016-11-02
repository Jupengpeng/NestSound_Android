package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.ui.IView.ICooperaDetailsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CooperaDetailsPresenter extends BasePresenter<ICooperaDetailsView> {

    public CooperaDetailsPresenter(Context context, ICooperaDetailsView iView) {
        super(context, iView);
    }

    public void getCooperaDetailsBean() {
        List<CooperaDetailsBean.CompleteListBean> completeListBean = new ArrayList<>();
        List<CooperaDetailsBean.CommentListBean> commentListBeen = new ArrayList<>();

        CooperaDetailsBean cooperaDetailsBean = new CooperaDetailsBean();
        CooperaDetailsBean.UserInfoBean userInfoBean = new CooperaDetailsBean.UserInfoBean();
        userInfoBean.setNickname("qqqqq");
        userInfoBean.setSignature("wwwwww");
        cooperaDetailsBean.setUserInfo(userInfoBean);
        CooperaDetailsBean.DemandInfoBean demandInfoBean = new CooperaDetailsBean.DemandInfoBean();
        demandInfoBean.setTitle("wqewqewqe");
        demandInfoBean.setLyrics("我期待着我的梦想\n希望有一天能发出光芒\n\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
        demandInfoBean.setCommentnum(100);
        demandInfoBean.setCreatetime(151515115);
        demandInfoBean.setRequirement("我期待着我的梦想\n希望有一天能发出光芒\n\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
        demandInfoBean.setEndtime(151515115);
        cooperaDetailsBean.setDemandInfo(demandInfoBean);

        for (int i = 0; i < 3; i++) {
            CooperaDetailsBean.CompleteListBean completeBean = new CooperaDetailsBean.CompleteListBean();
            CooperaDetailsBean.CommentListBean commentBean = new CooperaDetailsBean.CommentListBean();
            commentBean.setComment("qqqqqq");
            commentBean.setNickname("ooooo");
            commentBean.setCreatedate(121545545);
            completeBean.setCreatetime(151515115);
            completeBean.setTitle("cccccc");
            completeBean.setLUsername("jjjjjjj");
            completeBean.setWUsername("lllllll");
            completeBean.setAccess(i);
            completeListBean.add(completeBean);
            commentListBeen.add(commentBean);
        }
        cooperaDetailsBean.setCompleteList(completeListBean);
        cooperaDetailsBean.setCommentList(commentListBeen);

        iView.showCooperaDetailsBean(cooperaDetailsBean);

    }

}
