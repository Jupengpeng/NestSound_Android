package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.request.RequestCall;
import com.xilu.wybz.ui.IView.IOnlyFollowView;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/1.
 */
public class OnlyFollowPresenter extends BasePresenter<IOnlyFollowView>{

    private RequestCall followCall;

    public OnlyFollowPresenter(Context context, IOnlyFollowView iView) {
        super(context, iView);
    }

    @Override
    public void init() {
//        super.init();
    }

    public void follow(int userId) {
        params = new HashMap<>();
        params.put("userid", userId + "");
        params.put("fansid", PrefsUtil.getUserId(context) + "");
        cancel();
        followCall = httpUtils.post("",MyHttpClient.getFanFocusList(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                String data = response.getData();
                iView.followSuccess(data);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.followFailed("");
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.followFailed("");
            }
        });
    }


    public void cancel(){
        if (followCall != null){
            followCall.cancel();
            followCall = null;
        }
    }


    public static final int UNFOLLOWED = 0;
    public static final int FOLLOWED = 1;
    public static final int EACH_FOLLOWED = 2;

    public static int paraseStatuByString(String data){
        int statu = 0;
        if ("取消关注成功".equals(data)){
            return statu;
        }
        if ("关注成功".equals(data)){
            statu = 1;
            return statu;
        }
        if ("互相关注".equals(data)){
            statu = 2;
            return statu;
        }
        return statu;
    }
}
