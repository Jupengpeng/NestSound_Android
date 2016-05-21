package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void loadData(String userId, int page) {
        httpUtils.get(MyHttpClient.getInformation(userId, page), new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    try {
                        String resultlist = new JSONObject(response).getString("items");
                        List<InforCommentBean> mList = new Gson().fromJson(resultlist, new TypeToken<List<InforCommentBean>>() {
                        }.getType());
                        if (mList.size() == 0) {
                            if (page == 1) {
                                iView.loadNoData();
                            } else {
                                iView.loadNoMore();
                            }
                        } else {
                            iView.showCommentData(mList);
                        }

                    } catch (JSONException e) {
                        iView.loadNoData();
                    }

                } else {
                    iView.loadFail();
                }
            }
        });
    }
    public void sendComment(String c_id,String userId,String content){
        Map<String,String> params = new HashMap<>();
        params.put("id", c_id);
        params.put("userid", userId);
        params.put("comment", content);
        httpUtils.postUrl(MyHttpClient.getAddCommentUrl(), params, new MyStringCallback(){
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
