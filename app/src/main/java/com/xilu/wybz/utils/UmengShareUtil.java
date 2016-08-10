package com.xilu.wybz.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;
import com.xilu.wybz.bean.ShareBean;

import java.io.File;

/**
 * Created by June on 16/4/22.
 */
public class UmengShareUtil {
    public Activity activity;
    public UMImage image;
    public UMusic music;
    public ShareBean shareBean;
    public UmengShareUtil(Activity act, ShareBean shareBean) {
        activity = act;
        this.shareBean = shareBean;
        if(shareBean.type==1){//分享图片
            image = new UMImage(act, new File(shareBean.pic));
            return;
        }
        if(!TextUtils.isEmpty(shareBean.pic)){
            if(shareBean.pic.contains("http")){
                image = new UMImage(act, shareBean.getPic());
            }else if(new File(shareBean.pic).exists()){
                image = new UMImage(act, new File(shareBean.pic));
            }
        }
        if(!TextUtils.isEmpty(shareBean.playurl)){
            music = new UMusic(shareBean.playurl);
            music.setTitle(shareBean.title);
            music.setAuthor(shareBean.author);
            music.setTargetUrl(shareBean.link);
            if(image!=null)
            music.setThumb(image);
        }
    }

    public void toShareSina() {
        toShare(1);
    }

    public void toShareWeChat() {
        toShare(2);
    }

    public void toShareWeChatCircle() {
        toShare(3);
    }

    public void toShareQQ() {
        toShare(4);
    }

    public void toShareQzone() {
        toShare(5);
    }

    public void toShare(int type) {
        SHARE_MEDIA share_media=null;
        switch (type){
            case 1:share_media = SHARE_MEDIA.SINA;break;
            case 2:share_media = SHARE_MEDIA.WEIXIN;break;
            case 3:share_media = SHARE_MEDIA.WEIXIN_CIRCLE;break;
            case 4:share_media = SHARE_MEDIA.QQ;break;
            case 5:share_media = SHARE_MEDIA.QZONE;break;
        }
        ShareAction action =  new ShareAction(activity);
        action.setPlatform(share_media).setCallback(umShareListener);
        if(shareBean.type==1){
            action.withMedia(image);
            action.withTitle("");
            action.withTargetUrl("");
            action.withText("");
        }else {
            if (type > 1) {
                action.withTitle(shareBean.title);
                action.withTargetUrl(shareBean.link);
            }
            if (music != null && type > 1) {
                action.withMedia(music);
                action.withText(shareBean.author);
            } else {
                if (image != null)
                    action.withMedia(image);
                action.withText(shareBean.content);
            }
        }
        action.share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
//            ToastUtils.toast(activity, platform + " 分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            ToastUtils.toast(activity, platform + " 分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            ToastUtils.toast(activity, platform + " 分享取消");
        }
    };
}
