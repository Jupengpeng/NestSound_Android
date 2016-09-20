package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.MsgNumBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.IMsgNumView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class MsgNumPresenter extends BasePresenter<IMsgNumView> {

    public MsgNumPresenter(Context context, IMsgNumView iView) {
        super(context, iView);
    }

    public void loadData() {
        params = new HashMap<>();
        httpUtils.get(MyHttpClient.getMsgNumUrl(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return MsgNumBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                MsgNumBean msgNumBean = response.getData();
                iView.showMsgNum(msgNumBean);
            }
        });
    }
}
