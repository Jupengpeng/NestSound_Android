package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MatchWorkBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IMatchWorkView;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hujunwei on 16/4/5.
 */
public class MatchWorkPresenter extends BasePresenter<IMatchWorkView> {
    public MatchWorkPresenter(Context context, IMatchWorkView iView) {
        super(context, iView);
    }

    public void loadMatchWorkData(String aid, int type, String sort, int page) {
        params = new HashMap<>();
        params.put("aid", aid);
        if(type!=-1) {
            params.put("type", (type + 1) + "");
            params.put("sort", sort);
        }
        params.put("page", page + "");
        httpUtils.post(MyHttpClient.getMatchWorkList(), params, new AppJsonCalback(context) {
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
