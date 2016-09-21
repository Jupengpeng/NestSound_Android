package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserPresenter extends BasePresenter<IUserView> {
    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);
    }
    public void getUserInfo(int userId){
        if (userId <= 0) return;
        int userType = (userId == PrefsUtil.getUserId(context)) ? 1 : 2;
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");//自己的id
        if (userType == 2) {
            params.put("otherid", userId + "");
        }
        httpUtils.get(userType == 1 ? MyHttpClient.getUserCenter() : MyHttpClient.getOtherCenter(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return UserInfoBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                if(response.getData()!=null){
                    PrefsUtil.saveUserInfoBean(context,response.getData(),userId);
                    iView.setUserInfoBean(response.getData());
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }
        });
    }
}
