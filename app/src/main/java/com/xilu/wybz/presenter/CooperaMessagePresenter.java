package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CooperaMessageBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICooperaMessageView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/28.
 */

public class CooperaMessagePresenter extends BasePresenter<ICooperaMessageView> {
    public CooperaMessagePresenter(Context context, ICooperaMessageView iView) {
        super(context, iView);
    }

    public void getCooperaMessageList() {
        List<CooperaMessageBean> cooperaMessageBeanList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CooperaMessageBean cooperaMessageBean = new CooperaMessageBean();
            cooperaMessageBean.setNickname("qqqqq" + i);
            cooperaMessageBean.setTarget_nickname("疯子");
            cooperaMessageBean.setUid(i);
            cooperaMessageBean.setComment_type(i);
            cooperaMessageBean.setComment("sdsadsasdsadsadasdsadsadsad" + i);
            cooperaMessageBean.setCreatedate(454654564);

            cooperaMessageBeanList.add(cooperaMessageBean);
        }
        iView.showCooperaMessageList(cooperaMessageBeanList);
    }

    public void sendComment(long itemid, int comment_type, int type, long target_uid, String comment) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("itemid", itemid + "");
        params.put("comment_type", comment_type + "");
        params.put("type", type + "");
        params.put("comment", comment);
        if (comment_type == 2) {
            params.put("target_uid", target_uid + "");
        }
        httpUtils.post(MyHttpClient.getSaveCommentUrl(), params, new MyStringCallback() {
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

    public void delComment(int id, String itemid, int pos, int type) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("itemid", itemid);
        params.put("type", type + "");
        httpUtils.post(MyHttpClient.getDelCommentUrl(), params, new MyStringCallback() {
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
