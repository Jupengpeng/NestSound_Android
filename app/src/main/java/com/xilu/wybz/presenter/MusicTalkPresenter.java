package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.ui.IView.IMusicTalkDetailView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/7.
 */
public class MusicTalkPresenter extends BasePresenter<IMusicTalkDetailView> {
    public MusicTalkPresenter(Context context, IMusicTalkDetailView iView) {
        super(context, iView);
    }

    public void getDetail(String itemid) {
        params = new HashMap<>();
        params.put("itemid", itemid);
        params.put("uid", PrefsUtil.getUserId(context) + "");
        httpUtils.post(MyHttpClient.getMusicTalkDetail(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return MusicTalk.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                MusicTalk musicTalk = response.getData();
                if(musicTalk!=null){
                    iView.loadSuccess(musicTalk);
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }
        });
    }
    public void Zan(int itemid) {
        params = new HashMap<>();
        params.put("itemid", itemid + "");
        params.put("uid", PrefsUtil.getUserId(context) + "");
        httpUtils.post(MyHttpClient.getMusicTalkZan(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                iView.zanSuccess();
            }
            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.zanFail();
            }
        });
    }
}
