package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.StartBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.IStarView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class StarPresenter extends BasePresenter<IStarView>{
    public StarPresenter(Context context, IStarView iView) {
        super(context, iView);
    }
    public void getStarList(int page){
        params = new HashMap<>();
        params.put("page",page+"");
        httpUtils.post(MyHttpClient.getMusicianListUrl(), params, new AppJsonCalback(context){
            @Override
            public Type getDataType() {
                return new TypeToken<List< StartBean>>(){}.getType();
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<StartBean> mList = response.getData();
                if(mList==null)
                    iView.loadFail();
                else {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showData(mList);
                    }
                }
            }
        });
    }
}
