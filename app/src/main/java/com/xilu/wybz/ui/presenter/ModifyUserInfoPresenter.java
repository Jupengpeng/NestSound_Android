package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.IForgetPwdView;
import com.xilu.wybz.ui.IView.IModifyUserInfoView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class ModifyUserInfoPresenter extends com.xilu.wybz.presenter.BasePresenter<IModifyUserInfoView> {
    public ModifyUserInfoPresenter(Context context, IModifyUserInfoView iView) {
        super(context, iView);
    }
    public void modifyUserInfo(String userId, UserBean userBean) {
        params = new HashMap<>();
        params.put("uid",userId);
        params.put("headurl",userBean.headurl);
        params.put("nickname",userBean.name);
        params.put("sex",userBean.sex+"");
        params.put("signature",userBean.signature);
        params.put("birthday",userBean.birthday);
        httpUtils.get(MyHttpClient.getModifyUserInfo(), new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.modifyUserInfoFail();
            }

            @Override
            public void onResponse(String response) {
                iView.modifyUserInfoSuccess(response);
            }

        });
    }

}
