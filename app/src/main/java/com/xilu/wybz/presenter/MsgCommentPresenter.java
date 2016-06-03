package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class MsgCommentPresenter extends BasePresenter<ICommentView> {

    public MsgCommentPresenter(Context context, ICommentView iView) {
        super(context, iView);
    }

    public void loadData(int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getMsgCommentList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<MsgCommentBean> mList = ParseUtils.getMsgCommentsData(context,response);
                if(mList!=null) {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showMsgCommentData(mList);
                    }
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
        params.put("itemid", itemid+"");
        params.put("comment_type", comment_type+"");
        params.put("type", type+"");
        params.put("target_uid", target_uid+"");
        params.put("comment", comment);
        httpUtils.post(MyHttpClient.getSaveCommentUrl(), params, new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int id = ParseUtils.getCommentId(context,response);
                if(id>0) {
                    iView.commentSuccess(id);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.commentFail();
            }

        });
    }
}
