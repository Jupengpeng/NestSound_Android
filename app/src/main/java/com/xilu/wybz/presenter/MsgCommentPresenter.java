package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.utils.ParseUtils;
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

    public void loadData(int userId, int page) {
        params = new HashMap<>();
        params.put("uid",userId+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getMsgCommentList(), params, new MyStringCallback() {
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
    public void sendComment(String c_id,int userId,String content){
        Map<String,String> params = new HashMap<>();
        params.put("id", c_id);
        params.put("userid",userId+"");;
        params.put("comment", content);
        httpUtils.post(MyHttpClient.getSaveCommentUrl(), params, new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    iView.commentSuccess();
                }else{
                    iView.commentFail();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.commentFail();
            }
        });
    }
}
