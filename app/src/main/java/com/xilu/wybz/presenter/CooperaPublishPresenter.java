package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICooperaPublishView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/5/7.
 */
public class CooperaPublishPresenter extends BasePresenter<ICooperaPublishView> {
    public CooperaPublishPresenter(Context context, ICooperaPublishView iView) {
        super(context, iView);
    }

    public void publishDemand(String requirement, long itemid) {
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("requirement", requirement);
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("itemid", itemid + "");
        httpUtils.post(MyHttpClient.getDemand(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.fail();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.success();
                }

            }

        });

    }
}
