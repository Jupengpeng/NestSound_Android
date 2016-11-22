package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.message.SystemMessageBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ISystemMsgView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class MsgSystemPresenter extends BasePresenter<ISystemMsgView> {

    public MsgSystemPresenter(Context context, ISystemMsgView iView) {
        super(context, iView);
    }

    public void loadData(int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");;
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getMsgSystemList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<SystemMessageBean> mList = ParseUtils.getSystemsData(context,response);
                if (mList==null||mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showSystemData(mList);
                }
            }
        });
    }
}
