package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICollectView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/24.
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    public CollectPresenter(Context context, ICollectView iView) {
        super(context, iView);
    }

    public void getCollectList(int page) {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getFovlist(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<CollectBean> collectBeanList = response.getData();
                if (page == 1) {
                    if (collectBeanList.size() == 0) {
                        iView.noData();
                    }
                } else {
                    if (collectBeanList.size() == 0 || collectBeanList.size()<=10) {
                        iView.noMoreData();
                    }
                }
                iView.showCollectList(collectBeanList);

            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<CollectBean>>() {
                }.getType();
            }
        });
    }
    public void collect(int did, int type,int pos) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("type", type + "");
        httpUtils.post(MyHttpClient.getFov(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                   iView.cancleCollectSuccess(pos);
                }
            }
        });
    }
}
