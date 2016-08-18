package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.IView.IJoinUserView;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.ParseUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class JoinUserPresenter extends BasePresenter<IJoinUserView> {
    public JoinUserPresenter(Context context, IJoinUserView iView) {
        super(context, iView);
    }
    /*
    * name 搜索的关键词
    * type 1=最新伴奏列表 2=最热伴奏列表 默认为最新
     */
    public void loadData(String aid, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("aid", aid + "");
        params.put("page", page + "");
        httpUtils.post(MyHttpClient.getJoinUserList(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<JoinUserBean> mList = response.getData();
                if (mList == null || mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showData(mList);
                }
            }
            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<JoinUserBean>>(){}.getType();
            }
        });
    }
}
