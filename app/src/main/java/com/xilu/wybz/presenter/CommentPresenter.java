package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class CommentPresenter extends BasePresenter<ICommentView> {
    public CommentPresenter(Context context, ICommentView iView) {
        super(context, iView);
    }

    public void getCommentList(String itemid, int type, int page) {
        params = new HashMap<>();
        params.put("itemid", itemid);
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get(type == 3 ? MyHttpClient.getCooperaCommentList() : MyHttpClient.getCommentListUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<CommentBean> mList = ParseUtils.getCommentsData(context, response);
                if (mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showCommentData(mList);
                }
            }
        });
    }

    /*
    * uid 我的id
    * target_uid 被评论者的id comment_type=2时必填
    * itemid 作品id
    * type 1=歌曲，2=歌词
    * comment_type 1=默认，发帖，2=跟帖，回复
     */
    public void sendComment(String itemid, int comment_type, int type, int target_uid, String comment) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("itemid", itemid);
        params.put("comment_type", comment_type + "");
        params.put("type", type + "");
        params.put("comment", comment);
        if (comment_type == 2) {
            params.put("target_uid", target_uid + "");
        }
        httpUtils.post(type == 3 ? MyHttpClient.publishComment() : MyHttpClient.getSaveCommentUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.commentSuccess(commentId);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.commentFail();
            }

        });
    }

    //删除评论
    public void delComment(int id, String itemid, int pos, int type) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("itemid", itemid);
        params.put("type", type + "");
        httpUtils.post(type==3?MyHttpClient.deletecooperaComment():MyHttpClient.getDelCommentUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.delSuccess(pos);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.delFail();
            }
        });
    }
}
