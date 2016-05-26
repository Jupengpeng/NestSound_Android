package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.IFeedbackView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class FeedbackPresenter extends BasePresenter<IFeedbackView> {

    public FeedbackPresenter(Context context, IFeedbackView iView) {
        super(context, iView);
    }

    public void postData(String userId, String phone, String text, int type) {
        params = new HashMap<>();
        params.put("userid", userId);
        params.put("phone", phone);
        params.put("text", text);
        httpUtils.post(type == 0 ? MyHttpClient.getFeed() : MyHttpClient.getUserReport(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.postFail();
            }

            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                ToastUtils.toast(context,dataBean.message);
            }
        });
    }
}
