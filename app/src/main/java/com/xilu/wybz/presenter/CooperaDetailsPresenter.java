package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICooperaDetailsView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CooperaDetailsPresenter extends BasePresenter<ICooperaDetailsView> {

    public CooperaDetailsPresenter(Context context, ICooperaDetailsView iView) {
        super(context, iView);
    }

    public void getCooperaDetailsBean(int did, int page) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getCooperaDetail(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                CooperaDetailsBean cooperaDetailsBean = response.getData();
                iView.showCooperaDetailsBean(cooperaDetailsBean);
                iView.showCooperaCommentList(cooperaDetailsBean.getCommentList());
                iView.showCooperaCompleteList(cooperaDetailsBean.getCompleteList());
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<CooperaDetailsBean>() {
                }.getType();
            }
        });
    }

    //        List<CooperaDetailsBean.CompleteListBean> completeListBean = new ArrayList<>();
//        List<CooperaDetailsBean.CommentListBean> commentListBeen = new ArrayList<>();
//
//        CooperaDetailsBean cooperaDetailsBean = new CooperaDetailsBean();
//        CooperaDetailsBean.UserInfoBean userInfoBean = new CooperaDetailsBean.UserInfoBean();
//        userInfoBean.setNickname("qqqqq");
//        userInfoBean.setSignature("wwwwww");
//        cooperaDetailsBean.setUserInfo(userInfoBean);
//        CooperaDetailsBean.DemandInfoBean demandInfoBean = new CooperaDetailsBean.DemandInfoBean();
//        demandInfoBean.setTitle("wqewqewqe");
//        demandInfoBean.setLyrics("我期待着我的梦想\n希望有一天能发出光芒\n\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
//        demandInfoBean.setCommentnum(100);
//        demandInfoBean.setCreatetime(151515115);
//        demandInfoBean.setRequirement("我期待着我的梦想\n希望有一天能发出光芒\n\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
//        demandInfoBean.setEndtime(151515115);
//        cooperaDetailsBean.setDemandInfo(demandInfoBean);
//
//        for (int i = 0; i < 3; i++) {
//            CooperaDetailsBean.CompleteListBean completeBean = new CooperaDetailsBean.CompleteListBean();
//            CooperaDetailsBean.CommentListBean commentBean = new CooperaDetailsBean.CommentListBean();
//            commentBean.setComment("qqqqqq");
//            commentBean.setNickname("ooooo");
//            commentBean.setCreatedate(121545545);
//            completeBean.setCreatetime(151515115);
//            completeBean.setTitle("cccccc");
//            completeBean.setLUsername("jjjjjjj");
//            completeBean.setWUsername("lllllll");
//            completeBean.setAccess(i);
//            completeListBean.add(completeBean);
//            commentListBeen.add(commentBean);
//        }
//        cooperaDetailsBean.setCompleteList(completeListBean);
//        cooperaDetailsBean.setCommentList(commentListBeen);
//
//        iView.showCooperaDetailsBean(cooperaDetailsBean);
//
//    }
    public void getPreinfo(int did) {
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("did", did + "");
        httpUtils.post(MyHttpClient.getPreinfo(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);

            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                PreinfoBean preinfoBean = response.getData();
                iView.showpreinfoBean(preinfoBean);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<PreinfoBean>() {
                }.getType();
            }
        });

    }

    public void collect(int did, int type) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("type", type + "");
        httpUtils.post(MyHttpClient.getFov(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.collectSuccess(type);
                }
            }
        });
    }

}
