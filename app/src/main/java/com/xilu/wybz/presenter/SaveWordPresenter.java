package com.xilu.wybz.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
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
            map.put("userid", userId);
            map.put("title", worksData.title);
            if (TextUtils.isEmpty(worksData.title)) {
                ToastUtils.toast(context, "歌词标题不能为空！");
                return;
            }
            map.put("lyrics", worksData.lyrics);
            if (TextUtils.isEmpty(worksData.lyrics)) {
                ToastUtils.toast(context, "歌词内容不能为空！");
                return;
            }
            map.put("pic", worksData.pic);
            if (TextUtils.isEmpty(worksData.pic)) {
                ToastUtils.toast(context, "歌词封面没有上传！");
                return;
            }
            map.put("detail", worksData.detail);
            if (TextUtils.isEmpty(worksData.detail)) {
                ToastUtils.toast(context, "请先添加歌词描述！");
                return;
            }
            //前几个参数都不能为空 依次校验
            map.put("itemid", worksData.itemid);
            map.put("status", worksData.isOpen + "");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        httpUtils.post(MyHttpClient.getSaveLyricsUrl(), map, new MyStringCallback() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        iView.saveWordSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        super.onError(call, e);
                        iView.saveWordFail();
                    }
                }
        );
    }

}
