package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.IFeedbackView;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class SearchPresenter extends com.xilu.wybz.presenter.BasePresenter<ISearchView> {

    public SearchPresenter(Context context, ISearchView iView) {
        super(context, iView);
    }

    public void searchData(String userId, String keyWord, int type, int page) {
        params = new HashMap<>();
        params.put("userid", userId);
        params.put("keyWord", keyWord);
        params.put("type", type+"");
        params.put("page", page+"");
        httpUtils.post(MyHttpClient.getHotUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                } else {
                    ToastUtils.toast(context, ParseUtils.getMsg(response));
                }
            }
        });
    }
}
