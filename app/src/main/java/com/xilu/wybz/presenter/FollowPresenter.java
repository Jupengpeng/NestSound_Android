package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

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
        if (!isMe)
            params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get(isMe ? MyHttpClient.getFansList() : MyHttpClient.getOtherFansList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<FansBean> mList = ParseUtils.getFansData(context, response);
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

        });
    }

    public void follow(int userId) {
        params = new HashMap<>();
        params.put("userid", userId + "");
        params.put("fansid", PrefsUtil.getUserId(context) + "");
        httpUtils.post(MyHttpClient.getFanFocusList(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context, response);
                if (dataBean != null && dataBean.code == 200) {
                    iView.followSuccess();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.followFail();
            }
        });
    }
}
