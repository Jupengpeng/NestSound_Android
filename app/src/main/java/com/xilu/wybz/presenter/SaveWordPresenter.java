package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.ISaveWordView;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

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

    public void saveLyrics(WorksData worksData, String aid) {
        Map<String, String> map = new HashMap<>();
        try {
            if(StringUtils.isNotBlank(aid)){
                map.put("aid", aid);
            }
            map.put("uid", PrefsUtil.getUserId(context)+"");
            map.put("title", worksData.title);
            map.put("lyrics", worksData.lyrics);
            map.put("pic", worksData.pic);
            map.put("detail", worksData.detail);
            if(StringUtils.isNotBlank(worksData.itemid)) {
                map.put("id", worksData.itemid + "");
            }
            map.put("status", worksData.type + "");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        httpUtils.post(StringUtils.isNotBlank(aid)?MyHttpClient.getSaveActivityLyricsUrl():MyHttpClient.getSaveLyricsUrl(), map, new AppStringCallback(context) {
                    @Override
                    public void onResponse(JsonResponse<? extends Object> response) {
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
