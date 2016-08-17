package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MatchWorkBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.ui.IView.IMatchView;
import com.xilu.wybz.ui.IView.IMatchWorkView;
import com.xilu.wybz.utils.ParseUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class MatchWorkPresenter extends BasePresenter<IMatchWorkView> {
    String url;

    public MatchWorkPresenter(Context context, IMatchWorkView iView) {
        super(context, iView);
    }

    public void loadMatchWorkData(String aid, int type, String sort, int page) {
        params = new HashMap<>();
        params.put("aid", aid);
        params.put("type", type+"");
        params.put("sort", sort);
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getMatchWorkList(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<MatchWorkBean> matchWorkBeenList = response.getData();
                if (matchWorkBeenList != null) {
                    if (matchWorkBeenList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showWorkData(matchWorkBeenList);
                    }
                } else {
                    iView.loadFail();
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<MatchWorkBean>>(){}.getType();
            }
        });
    }
}
