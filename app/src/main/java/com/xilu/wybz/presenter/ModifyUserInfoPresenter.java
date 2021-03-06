package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IModifyUserInfoView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class ModifyUserInfoPresenter extends BasePresenter<IModifyUserInfoView> {
    public ModifyUserInfoPresenter(Context context, IModifyUserInfoView iView) {
        super(context, iView);
    }
    public void modifyUserInfo(UserBean userBean) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("headurl", userBean.headurl);
        params.put("nickname",userBean.nickname);
        params.put("sex",userBean.sex+"");
        params.put("signature",userBean.signature);
        params.put("birthday",userBean.birthday);
        httpUtils.post(MyHttpClient.getModifyUserInfo(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.modifyUserInfoFail();
            }
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean.code==200)
                iView.modifyUserInfoSuccess();
            }
        });
    }
}
