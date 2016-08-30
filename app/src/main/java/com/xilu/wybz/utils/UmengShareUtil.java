package com.xilu.wybz.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.common.MyCommon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

        if (!TextUtils.isEmpty(shareBean.pic) && shareBean.pic.contains("http://pic.yinchao.cn/")){
            shareBean.pic += "?imageView2/0/w/200/h/200/q/40";
//            shareBean.pic ="/storage/emulated/0/yinchao/image/cover/1472212194592.jpg";
        }

        if (shareBean.type == 1) {//分享图片
            image = new UMImage(act, new File(shareBean.pic));
            return;
        }
        if (!TextUtils.isEmpty(shareBean.pic)) {
            if (shareBean.pic.contains("http")) {
                image = new UMImage(act, shareBean.getPic());
            } else if (new File(shareBean.pic).exists()) {
                image = new UMImage(act, new File(shareBean.pic));
            }
        }
        if (!TextUtils.isEmpty(shareBean.playurl)) {
            music = new UMusic(shareBean.playurl);
            music.setTitle(shareBean.title);
            music.setAuthor(shareBean.author);
            music.setTargetUrl(shareBean.link);
            if (image != null)
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
        if (shareBean.type == 1) {
            List<File> images = new ArrayList<>();
            images.add(new File(shareBean.pic));
            switch (type) {
                case 1:
                    shareAll(images,"", MyCommon.SINA_PKG);
                    break;
                case 2:
                    shareWxPerson(activity,"",images);
                    break;
                case 3:
                    shareWxFriends(activity,"",images);
                    break;
                case 4:
                    shareAll(images,"",MyCommon.QQ_PKG);
                    break;
                case 5:
                    shareAll(images,"",MyCommon.QZONE_PKG);
                    break;
            }
        } else {
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
            if(type>1){
                action.withTitle(shareBean.title);
                action.withTargetUrl(shareBean.link);
            }
            if(music!=null&&type>1){
                action.withMedia(music);
                action.withText(shareBean.author);
            }else{
                if(image!=null)
                    action.withMedia(image);
                action.withText(shareBean.content);
            }
            action.share();
        }
    }

    //利用包名分享
    public void shareAll(List<File> images, String shareContent, String packageName) {
        String toastMsg = "";
        ArrayList<Uri> picUriList = new ArrayList<Uri>();
        for (File picFile : images) {
            Uri picUri = Uri.fromFile(picFile);
            picUriList.add(picUri);
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, picUriList);
        intent.putExtra("Kdescription", shareContent);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setType("image/*");
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        ResolveInfo info = null;
        for (ResolveInfo each : matches) {
            String pkgName = each.activityInfo.applicationInfo.packageName;
            if (packageName.equals(pkgName)) {
                info = each;
                break;
            }
        }
        if (info == null) {
            ToastUtils.toast(activity, toastMsg);
            return;
        } else {
            intent.setClassName(packageName, info.activityInfo.name);
        }
        activity.startActivity(intent);
    }
    public static void sharePicAll(Context context, ComponentName comp,String shareContent,List<File> images){
        ArrayList<Uri> picUriList = new ArrayList<Uri>();
        for (File file : images) {
            if(file.exists()){
                Uri picUri = Uri.fromFile(file);
                picUriList.add(picUri);
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, picUriList);
        intent.putExtra("Kdescription", shareContent);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setComponent(comp);
        context.startActivity(intent);
    }
    //微信 图+文
    public static void shareWxPerson(Context context, String shareContent, List<File> images) {
        ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
        sharePicAll(context, comp, shareContent, images);
    }
    //微信朋友圈 图+文
    public static void shareWxFriends(Context context, String shareContent, List<File> images) {
        ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
        sharePicAll(context, comp, shareContent, images);
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            ToastUtils.toast(activity, platform + " 分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.toast(activity, platform + " 分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.toast(activity, platform + " 分享取消");
        }
    };
}
