package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.ui.IView.ICooperationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CooperationPresenter extends BasePresenter<ICooperationView> {


    public CooperationPresenter(Context context, ICooperationView iView) {
        super(context, iView);
    }

    public void getCooperationList() {
        List<CooperationBean> list = new ArrayList<>();
        List<CooperationBean.CommentListBean> commentListBeanList=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
          CooperationBean cooperationBean = new CooperationBean();
            CooperationBean.DemandInfoBean demandInfoBean= new CooperationBean.DemandInfoBean();
            demandInfoBean.setCreatetime(555555555);
            demandInfoBean.setLyrics("我期待着我的梦想\n希望有一天能发出光芒\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
            demandInfoBean.setRequirement("文件看了就为了求可敬了空间为科技路口为妻儿就立刻就看了"+i);
            demandInfoBean.setTitle("wqeqweqw"+i);
            demandInfoBean.setWorknum(i);
            demandInfoBean.setCommentnum(i);
            cooperationBean.setDemandInfo(demandInfoBean);

            CooperationBean.UserInfoBean userInfoBean = new CooperationBean.UserInfoBean();
            userInfoBean.setNickname("dadao"+i);
            cooperationBean.setUserInfo(userInfoBean);

            CooperationBean.CommentListBean commentListBean = new CooperationBean.CommentListBean();

            commentListBean.setNickname("qweqwewq"+i);
            commentListBean.setComment("vvvvvvvv"+i);

            commentListBeanList.add(commentListBean);
            cooperationBean.setCommentList(commentListBeanList);
            list.add(cooperationBean);
        }
        iView.showCooperation(list);
    }

}
