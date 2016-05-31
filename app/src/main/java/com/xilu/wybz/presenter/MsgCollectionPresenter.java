package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICollectionView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class MsgCollectionPresenter extends BasePresenter<ICollectionView> {

    public MsgCollectionPresenter(Context context, ICollectionView iView) {
        super(context, iView);
    }

    public void loadData(int page) {
        Map<String,String> params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getMsgFovList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<CollectionBean> mList = ParseUtils.getFavsData(context,response);
                if (mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showCollectionData(mList);
                }
            }
        });
    }
}
