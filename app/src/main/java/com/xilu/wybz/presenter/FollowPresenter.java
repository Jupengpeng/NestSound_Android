package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FollowPresenter extends BasePresenter<IFollowAndFansView> {

    public FollowPresenter(Context context, IFollowAndFansView iView) {
        super(context, iView);
    }

    public void loadData(int userId, int type, int page) {
        params = new HashMap<>();
        params.put("userid", userId + "");
        boolean isMe = PrefsUtil.getUserId(context) == userId;
        if (!isMe) {
            params.put("uid", PrefsUtil.getUserId(context) + "");
        }
        params.put("type", type + "");
        params.put("page", page + "");

        httpUtils.get(isMe ? MyHttpClient.getFansList() : MyHttpClient.getOtherFansList(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFail();
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                List<FansBean> mList = response.getData();
                if (mList != null) {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showFansData(mList);
                    }
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }
            @Override
            public Type getDataType() {
                return new TypeToken<List<FansBean>>(){}.getType();
            }

        });
    }

//    public void follow(int userId) {
//        params = new HashMap<>();
//        params.put("userid", userId + "");
//        params.put("fansid", PrefsUtil.getUserId(content) + "");
//        httpUtils.post(MyHttpClient.getFanFocusList(), params, new MyStringCallback() {
//            @Override
//            public void onResponse(String response) {
//                super.onResponse(response);
//                DataBean dataBean = ParseUtils.getDataBean(content, response);
//                if (dataBean != null && dataBean.code == 200) {
//                    iView.followSuccess();
//                }
//            }
//
//            @Override
//            public void onError(Call call, Exception e) {
//                iView.followFail();
//            }
//        });
//    }
}
