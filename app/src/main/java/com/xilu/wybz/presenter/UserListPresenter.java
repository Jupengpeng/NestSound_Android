package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.IUserListView;
import com.xilu.wybz.ui.IView.IUserView;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/25.
 */
public class UserListPresenter extends UserPresenter {

    IUserListView iListView;
    int type;

    public UserListPresenter(Context context, IUserView iView, IUserListView iListView, int userType, int type) {
        super(context, iView, userType);

        this.iListView = iListView;
        this.type = type;
    }

    @Override
    public void init() {
        iListView.initView();
    }

    public void requestListData(int userId, int page){
        request(userId,type,page,new AppJsonCalback(context){
            @Override
            public Type getDataType() {
                return new TypeToken<JsonResponse<MineBean>>(){}.getType();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iListView.showLoadMore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iListView.cancelLoadMore();
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                iListView.showContent();
                MineBean data = response.getData();
                if (data.getUser() != null){
                    iView.setUserInfo(data.getUser());
                }
                if (data.getFansnum() > 0){
                    iView.setFansNumber(data.getFansnum());
                }
                if (data.getGznum() > 0){
                    iView.setFansNumber(data.getGznum());
                }
                if (data.getList() != null){
                    iListView.addListDatas(data.getList());
                } else {
                    if (page == 1){
                        iListView.showNoData();
                    } else {
                        showMessage("没有更多数据");
                    }
                }
            }
            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iListView.showNoNet();
            }
        });

    }

}
