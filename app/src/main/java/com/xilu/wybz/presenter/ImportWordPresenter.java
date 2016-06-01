package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHomeView;
import com.xilu.wybz.ui.IView.IImportWordView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 2016/4/28.
 */
public class ImportWordPresenter extends BasePresenter<IImportWordView> {
    public ImportWordPresenter(Context context, IImportWordView iView) {
        super(context, iView);
    }

    public void loadData(int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getUserLyricsListUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
            @Override
            public void onResponse(String response) {
                List<WorksData> mList = ParseUtils.getWorksData(context, response);
                if (mList != null) {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showLyricsData(mList);
                    }
                }
            }
        });
    }
}
