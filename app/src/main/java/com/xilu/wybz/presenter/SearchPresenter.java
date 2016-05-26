package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFeedbackView;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class SearchPresenter extends BasePresenter<ISearchView> {

    public SearchPresenter(Context context, ISearchView iView) {
        super(context, iView);
    }

    public void searchData(int userId, String keyWord, int type, int page) {
        params = new HashMap<>();
        params.put("userid",userId+"");;
        params.put("keyWord", keyWord);
        params.put("type", type+"");
        params.put("page", page+"");
        httpUtils.post(MyHttpClient.getHotUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {

            }
        });
    }
}
