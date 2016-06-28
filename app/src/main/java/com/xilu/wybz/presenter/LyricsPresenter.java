package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILyricsView;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/4/5.
 */
public class LyricsPresenter extends BasePresenter<ILyricsView> {
    public LyricsPresenter(Context context, ILyricsView iView) {
        super(context, iView);
    }

    public void getLyric(int id) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("id",id+"");
        httpUtils.get(MyHttpClient.getLyricsdisplay(), params, new MyStringCallback() {
            @Override
            public void onResponse(String result) {
                try {
                    if (ParseUtils.checkCode(result)) {
                        String dataJson = new JSONObject(result).getString("data");
                        WorksData lyricsdisplayBean = new Gson().fromJson(dataJson, WorksData.class);
                        iView.loadLyrics(lyricsdisplayBean);
                    } else {
                        ParseUtils.showMsg(context, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    iView.showErrorView();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                iView.showProgressBar();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                iView.hideProgressBar();
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                iView.showErrorView();
            }
        });
    }
    public void setCollectionState(int music_id, int target_uid) {
        params = new HashMap<>();
        params.put("user_id",PrefsUtil.getUserId(context)+"");;
        params.put("work_id", music_id+"");
        params.put("target_uid", target_uid+"");
        params.put("wtype", "2");
        httpUtils.post(MyHttpClient.getWorkFovUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean!=null&&dataBean.code==200)
                    iView.favSuccess();
                else
                    iView.favFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.favFail();
            }
        });
    }
    public void zan(int id, int target_uid) {
        params = new HashMap<>();
        params.put("target_uid", target_uid+"");//作者id
        params.put("user_id", PrefsUtil.getUserId(context)+"");
        params.put("work_id", id+"");
        params.put("wtype", "2");//2歌词 1歌曲
        httpUtils.post(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean!=null&&dataBean.code==200)
                    iView.zanSuccess();
                else
                    iView.zanFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.zanFail();
            }
        });
    }
    public void loadHead(String headUrl){
        if(StringUtil.isBlank(headUrl))return;
        if(!new File(FileDir.headPic).exists())new File(FileDir.headPic).mkdirs();
        String fileName = MD5Util.getMD5String(headUrl);
        httpUtils.getFile(MyCommon.getImageUrl(headUrl, 100, 100), new FileCallBack(FileDir.headPic,fileName+".cache") {
            @Override
            public void inProgress(float progress, long total) {

            }
            @Override
            public void onError(Call call, Exception e) {

            }
            @Override
            public void onResponse(File response) {
                FileUtils.renameFile(FileDir.headPic+fileName+".cache",FileDir.headPic+fileName);
                iView.loadPicSuccess(FileDir.headPic+fileName);
            }
        });
    }
}
