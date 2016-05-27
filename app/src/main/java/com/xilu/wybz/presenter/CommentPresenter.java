package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class CommentPresenter extends BasePresenter<ICommentView>{
    public CommentPresenter(Context context, ICommentView iView) {
        super(context, iView);
    }
    public void getCommentList(int itemid,int type,int page){
        params.put("itemid",itemid+"");
        params.put("type",type+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getCommentListUrl(), params, new MyStringCallback(){
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<CommentBean> mList = ParseUtils.getCommentsData(context,response);
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
    public void sendComment(int itemid,int comment_type,int type,int target_uid, String comment){
        Map<String,String> params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        if(itemid>0)
        params.put("itemid", itemid+"");
        params.put("comment_type", comment_type+"");
        params.put("type", type+"");
        if(target_uid>0)
        params.put("target_uid", target_uid+"");
        params.put("comment", comment);
        httpUtils.post(MyHttpClient.getSaveCommentUrl(), params, new AppJsonCalback(context){
            @Override
            public void onResponse(JsonResponse response) {
                super.onResponse(response);
                iView.commentSuccess();
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.commentFail();
            }
        });
    }
    //删除评论
    public void delComment(int id,int type){
        Map<String,String> params = new HashMap<>();
        params.put("id", id+"");
        params.put("type", type+"");
        httpUtils.post(MyHttpClient.getDelCommentUrl(), params, new AppJsonCalback(context){
            @Override
            public void onResponse(JsonResponse response) {
                super.onResponse(response);
                iView.delSuccess();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.delFail();
            }
        });
    }
}
