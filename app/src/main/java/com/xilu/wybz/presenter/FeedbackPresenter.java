package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFeedbackView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class FeedbackPresenter extends BasePresenter<IFeedbackView> {

    public FeedbackPresenter(Context context, IFeedbackView iView) {
        super(context, iView);
    }

    public void postData(String phone, String text, int type) {
        params = new HashMap<>();
        params.put("userid", PrefsUtil.getUserId(context)+"");
        params.put("phone", phone);
        params.put("text", text);
        httpUtils.post(type == 0 ? MyHttpClient.getFeed() : MyHttpClient.getUserReport(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.postFail();
            }
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean.code==200){
                    iView.postSuccess();
                }

            }
        });
    }
}
