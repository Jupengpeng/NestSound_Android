package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.bean.Response;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/25.
 */
public class WorkCommentPresenter extends BasePresenter<ICommentView> {


    public WorkCommentPresenter(Context context, ICommentView iView) {
        super(context, iView);
    }


    public void loadData(int itemId, int type, int page) {
        params = new HashMap<>();
        params.put("itemid",""+itemId);
        params.put("type",""+type);
        params.put("page",""+page);
        httpUtils.get(MyHttpClient.getCommentList(), params, new AppStringCallback(context) {

            @Override
            public Type getDataType() {
                return new TypeToken<Response<List<InforCommentBean>>>(){}.getType();
            }

            @Override
            public void onResponse(Response<? extends Object> response) {
                super.onResponse(response);

                List<InforCommentBean> data = response.getData();
                if (data.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showCommentData(data);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
        });
    }
    public void sendComment(String c_id,String userId,String content){
        Map<String,String> params = new HashMap<>();
        params.put("id", c_id);
        params.put("userid", userId);
        params.put("comment", content);
        httpUtils.post(MyHttpClient.getAddCommentUrl(), params, new AppStringCallback(context){

            @Override
            public void onResponse(Response<? extends Object> response) {
                iView.commentSuccess();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.commentFail();
            }
        });
    }

}
