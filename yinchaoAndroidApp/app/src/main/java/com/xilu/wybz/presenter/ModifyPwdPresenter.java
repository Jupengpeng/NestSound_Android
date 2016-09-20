package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IModifyPwdView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class ModifyPwdPresenter extends BasePresenter<IModifyPwdView>{
    public ModifyPwdPresenter(Context context, IModifyPwdView iView) {
        super(context, iView);
    }
    public void modifyPwd(String opwd,String newpwd,String comfirmpwd){
        params = new HashMap<>();
        params.put("id", PrefsUtil.getUserId(context)+"");
        params.put("opwd",opwd);
        params.put("newpwd",newpwd);
        params.put("comfirmpwd",comfirmpwd);
        httpUtils.post(MyHttpClient.getPasswordUrl(), params, new MyStringCallback(){
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.modifyPwdStart();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context, response);
                if(dataBean!=null&&dataBean.code==200)
                    iView.modifyPwdSuccess();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.modifyPwdFail();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.modifyPwdFinish();
            }
        });
    }

}
