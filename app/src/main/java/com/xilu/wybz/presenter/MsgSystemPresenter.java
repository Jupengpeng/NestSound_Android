package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.SystemBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICollectionView;
import com.xilu.wybz.ui.IView.ISystemMsgView;
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
public class MsgSystemPresenter extends BasePresenter<ISystemMsgView> {

    public MsgSystemPresenter(Context context, ISystemMsgView iView) {
        super(context, iView);
    }

    public void loadData(String userId, int page) {
        Map<String,String> params = new HashMap<>();
        params.put("uid", userId);
        params.put("page", page + "");
        httpUtils.post(MyHttpClient.getMsgSystemList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    try {
                        String resultlist = new JSONObject(response).getString("data");
                        List<SystemBean> mList = new Gson().fromJson(resultlist, new TypeToken<List<SystemBean>>(){}.getType());
                        if (mList.size() == 0) {
                            if (page == 1) {
                                iView.loadNoData();
                            } else {
                                iView.loadNoMore();
                            }
                        } else {
                            iView.showSystemData(mList);
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
}
