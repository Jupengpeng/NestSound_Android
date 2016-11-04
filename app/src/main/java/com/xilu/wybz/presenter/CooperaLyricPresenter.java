package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICooperaLyricView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CooperaLyricPresenter extends BasePresenter<ICooperaLyricView> {


    public CooperaLyricPresenter(Context context, ICooperaLyricView iView) {
        super(context, iView);
    }

    public void getCooperaLyricList(int page) {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getLyricslist(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<CooperaLyricBean> cooperaLyricBeanList = response.getData();
                iView.showCooperaLyricList(cooperaLyricBeanList);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<CooperaLyricBean>>() {
                }.getType();
            }
        });
    }

    public void updateLyricStatu(long id, int status) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("status", status + "");
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getUpdateLyricsUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.updatesuccess();
                }
            }
        });
    }

}
