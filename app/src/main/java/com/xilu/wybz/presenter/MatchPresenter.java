package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MainBean;
import com.xilu.wybz.bean.MatchBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.ui.IView.IMatchView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/7.
 */
public class MatchPresenter extends BasePresenter<IMatchView> {
    public MatchPresenter(Context context, IMatchView iView) {
        super(context, iView);
    }

    public void getMatchInfo(String aid) {
        params = new HashMap<>();
        params.put("aid",aid+"");
        params.put("uid", PrefsUtil.getInt("uid",context)+"");
        httpUtils.post(MyHttpClient.getMatchInfo(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return MatchBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                MatchBean matchBean = response.getData();
                if(matchBean!=null)
                    iView.showMatchData(matchBean);
                else
                    iView.loadFail();
            }

        });
    }
    public void getUserList(String aid, int page) {
        params = new HashMap<>();
        params.put("aid",aid+"");
        params.put("page", page+"");
        httpUtils.post(MyHttpClient.getJoinUserList(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return new TypeToken<List<JoinUserBean>>(){}.getType();
            }
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<JoinUserBean> joinUserBeens = response.getData();
                if(joinUserBeens!=null)
                    iView.showJoinData(joinUserBeens);
                else
                    iView.loadFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }


        });
    }
}
