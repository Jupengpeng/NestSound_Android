package com.xilu.wybz.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.bean.Response;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IForgetPwdView;
import com.xilu.wybz.ui.IView.ISaveWordView;
import com.xilu.wybz.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class SaveWordPresenter extends BasePresenter<ISaveWordView> {
    public SaveWordPresenter(Context context, ISaveWordView iView) {
        super(context, iView);
    }

    public void saveLyrics(WorksData worksData, String userId) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("uid", userId);
            map.put("title", worksData.title);
            map.put("lyrics", worksData.lyrics);
            map.put("pic", worksData.pic);
            map.put("detail", worksData.detail);
            map.put("itemid", worksData.itemid);
            map.put("status", worksData.status + "");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        httpUtils.post(MyHttpClient.getSaveLyricsUrl(), map, new AppStringCallback(context) {
                    @Override
                    public void onResponse(Response<? extends Object> response) {
                        super.onResponse(response);
                        iView.saveWordSuccess(response.getData());
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        super.onError(call, e);
                        iView.saveWordFail();
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        iView.onFinish();
                    }
                }
        );
    }

}
