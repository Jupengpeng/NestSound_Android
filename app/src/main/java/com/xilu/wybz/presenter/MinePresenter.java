package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IMineView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/24.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    public MinePresenter(Context context, IMineView iView) {
        super(context, iView);
    }

    public void getMineList(int page) {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getMylist(), params, new AppJsonCalback(context) {

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<MineBean> mineBeanList = response.getData();
                if(page==1){
                    if(mineBeanList.size()==0){
                        iView.noData();
                    }
                }else{
                    if(mineBeanList.size()==0 ){

                        iView.noMoreData();
                    }
                }
                iView.showMineList(mineBeanList);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<MineBean>>() {
                }.getType();
            }
        });

    }

    public void deleteCoopera(int did,int pos) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getDelete(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.deleteSuccess(pos);
                }

            }
        });
    }

}
