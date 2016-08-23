package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.IModifyCoverView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class ModifyCoverPresenter extends BasePresenter<IModifyCoverView>{
    public ModifyCoverPresenter(Context context, IModifyCoverView iView) {
        super(context, iView);
    }
    public void modifyCover(String bgpic){
        params = new HashMap<>();
        params.put("uid",PrefsUtil.getUserId(context)+"");
        params.put("bgpic",bgpic);
        httpUtils.post(MyHttpClient.getUpdateuserbgUrl(), params, new MyStringCallback(){
            @Override
            public void onError(Call call, Exception e) {
                iView.onFail();
            }

            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean.code==200){
                    iView.onSuccess(bgpic);
                }else{
                    iView.onFail();
                }
            }
        });
    }
}
