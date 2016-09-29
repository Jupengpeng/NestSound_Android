package com.xilu.wybz.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.lyrics.LyricsPosterActivity;
import com.xilu.wybz.utils.AppInfoUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.utils.UmengShareUtil;


public class ShareDialog extends Dialog implements View.OnClickListener {
    Activity context;
    LinearLayout ll_wechat;
    LinearLayout ll_friend;
    LinearLayout ll_sina;
    LinearLayout ll_qq;
    LinearLayout ll_poster;
    LinearLayout ll_qzone;
    LinearLayout ll_copylink;
    UmengShareUtil shareUtil;
    ShareBean shareBean;
    WorksData mWorksData;
    int type;// 1分享歌词海报

    public ShareDialog(Activity context, WorksData worksData, int type) {
        super(context, R.style.CommentDialog);
        this.type = type;
        if (shareUtil == null) {
            String shareContent = "";
            String playurl = "";
            String shareTitle = worksData.title.replace("\n","");
            String shareAuthor = worksData.author;
            String shareLink = worksData.shareurl + "?id=" + worksData.itemid;
            String sharePic = worksData.pic;
            if (worksData.status == 1) {
                playurl = worksData.playurl;
                shareContent = "我在音巢APP淘到一首好听的歌，快来看看有没有你喜欢的原创style 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
            } else {
                String shareBody = PrefsUtil.getUserId(context) == worksData.uid ? "我用音巢app创作了一首歌词，快来看看吧!" : "我在音巢app上发现一首好歌词，太棒了~";
                shareContent = shareBody + " 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
            }
            shareBean = new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playurl, type);
            shareUtil = new UmengShareUtil(context, shareBean);
        }
        mWorksData = worksData;
        this.context = context;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }
    public ShareDialog(Activity context, MusicTalk musicTalk) {
        super(context, R.style.CommentDialog);
        if (shareUtil == null) {
            String playurl = "";
            String shareTitle = musicTalk.name.replace("\n","");
            String shareAuthor = musicTalk.detail==null?"":musicTalk.detail;
            String shareLink = musicTalk.shareurl;
            String sharePic = musicTalk.pic;
            String shareContent = "我在音巢APP淘到一首好听的歌，快来看看有没有你喜欢的原创style 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
            shareBean = new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playurl, type);
            shareUtil = new UmengShareUtil(context, shareBean);
        }
        this.context = context;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }
    public View getDialogView() {
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_share, null);
        ll_wechat = (LinearLayout) rootView.findViewById(R.id.ll_share_weixin);
        ll_copylink = (LinearLayout) rootView.findViewById(R.id.ll_share_copy);
        ll_wechat = (LinearLayout) rootView.findViewById(R.id.ll_share_weixin);
        ll_poster = (LinearLayout) rootView.findViewById(R.id.ll_poster);
        ll_friend = (LinearLayout) rootView.findViewById(R.id.ll_share_friend);
        ll_sina = (LinearLayout) rootView.findViewById(R.id.ll_share_weibo);
        ll_qzone = (LinearLayout) rootView.findViewById(R.id.ll_share_qzone);
        ll_qq = (LinearLayout) rootView.findViewById(R.id.ll_share_qq);

        ll_qq.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        ll_copylink.setOnClickListener(this);
        ll_sina.setOnClickListener(this);
        ll_qzone.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_poster.setOnClickListener(this);
        if (type == 0 || StringUtils.isBlank(mWorksData.lyrics))
            ll_poster.setVisibility(View.INVISIBLE);
        if (type == 1) {
            ll_copylink.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share_weixin:
                if(AppInfoUtil.isAppInstalled(context, MyCommon.WECHAT_PKG)) {
                    shareUtil.toShareWeChat();
                }else{
                    ToastUtils.toast(context,"您尚未安装微信！");
                }
                break;
            case R.id.ll_share_weibo:
                if(AppInfoUtil.isAppInstalled(context, MyCommon.SINA_PKG)) {
                    shareUtil.toShareSina();
                }else{
                    ToastUtils.toast(context,"您尚未安装微博！");
                }
                break;
            case R.id.ll_share_friend:
                if(AppInfoUtil.isAppInstalled(context, MyCommon.WECHAT_PKG)) {
                    shareUtil.toShareWeChatCircle();
                }else{
                    ToastUtils.toast(context,"您尚未安装微信！");
                }
                break;
            case R.id.ll_share_qq:
                if(AppInfoUtil.isAppInstalled(context, MyCommon.QQ_PKG)) {
                    shareUtil.toShareQQ();
                }else {
                    ToastUtils.toast(context,"您尚未安装QQ！");
                }
                break;
            case R.id.ll_share_qzone:
                if(AppInfoUtil.isAppInstalled(context, MyCommon.QZONE_PKG)||type==0) {
                    shareUtil.toShareQzone();
                }else {
                    ToastUtils.toast(context,"您尚未安装QQ空间！");
                }
                break;
            case R.id.ll_share_copy:
                StringUtils.copy(shareBean.link, context);
                ToastUtils.toast(context, "复制成功!");
                break;
            case R.id.ll_poster:
                LyricsPosterActivity.toLyricsPosterActivity(context, mWorksData);
                break;
        }
        dismiss();
    }

    public void showDialog() {
        show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DensityUtil.getScreenW(context);
        getWindow().setAttributes(params);
    }
}
