package com.xilu.wybz.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IPlayView;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class PlayPresenter extends BasePresenter<IPlayView> {

    String imageUrl;
    public PlayPresenter(Context context, IPlayView iView) {
        super(context, iView);
    }

    public void setCollectionState(int music_id, int target_uid) {
        params = new HashMap<>();
        params.put("user_id",PrefsUtil.getUserId(context)+"");;
        params.put("work_id", music_id+"");
        params.put("target_uid", target_uid+"");
        params.put("wtype", "1");
        httpUtils.post(MyHttpClient.getWorkFovUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean!=null&&dataBean.code==200)
                    iView.collectionMusicSuccess();
                else
                    iView.collectionMusicFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.collectionMusicFail();
            }
        });
    }
    public void setZambiaState(int music_id, int target_uid) {
        params = new HashMap<>();
        params.put("user_id",PrefsUtil.getUserId(context)+"");;
        params.put("work_id", music_id+"");
        params.put("target_uid", target_uid+"");
        params.put("wtype", "1");
        httpUtils.post(MyHttpClient.getUpvoteUrl(), params, new MyStringCallback() {
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean!=null&&dataBean.code==200)
                    iView.zambiaMusicSuccess();
                else
                    iView.zambiaMusicFail();
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.zambiaMusicFail();
            }
        });
    }
    public void downLoadPic(String imageUrl, String path){
        this.imageUrl = imageUrl;
        httpUtils.getImage(imageUrl, new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Bitmap response) {
                Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(response, 200), 30);
                FileUtils.saveBmp(path, bmp);
                iView.setPic(bmp);
            }
        });
    }
    //删除作品
    public void delete(int id) {
        params = new HashMap<>();
        params.put("id",id+"");
        params.put("type","1");//歌曲
        httpUtils.post(MyHttpClient.getDeleteWorksUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean!=null&&dataBean.code==200){
                    iView.deleteSuccess();
                }else{
                    iView.deleteFail();
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.deleteFail();
            }
        });
    }
    public void cancleRequest(){
        if(StringUtil.isNotBlank(imageUrl))
        httpUtils.cancelHttpByTag(imageUrl);
        httpUtils.cancelHttpByTag(MyHttpClient.getUpvoteUrl());
        httpUtils.cancelHttpByTag(MyHttpClient.getUpvoteUrl());
        httpUtils.cancelHttpByTag(MyHttpClient.getDeleteWorksUrl());
    }
}
