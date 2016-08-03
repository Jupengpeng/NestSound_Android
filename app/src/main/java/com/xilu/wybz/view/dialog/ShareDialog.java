package com.xilu.wybz.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.lyrics.LyricsPosterActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
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
    WorksData mWorksData;
    int type;
    public ShareDialog(Activity context, WorksData worksData) {
        super(context, R.style.CommentDialog);
        if (shareUtil == null) {
            String shareContent = "";
            String playurl = "";
            String shareTitle = worksData.title;
            String shareAuthor = worksData.author;
            String shareLink = worksData.shareurl + "?id=" + worksData.itemid;
            String sharePic = worksData.pic;
            if(worksData.status==1){
                playurl = worksData.playurl;
                shareContent = "我在音巢APP淘到一首好听的歌，快来看看有没有你喜欢的原创style 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
            }else{
                String shareBody = PrefsUtil.getUserId(context) == worksData.uid ? "我用音巢app创作了一首歌词，快来看看吧!" : "我在音巢app上发现一首好歌词，太棒了~";
                shareContent = shareBody + " 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
            }
            shareUtil = new UmengShareUtil(context, new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playurl));
        }
        mWorksData = worksData;
        this.context = context;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }
    public void setType(int type){
        this.type=type;
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
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share_weixin:
                shareUtil.toShareWeChat();
                break;
            case R.id.ll_share_weibo:
                shareUtil.toShareSina();
                break;
            case R.id.ll_share_friend:
                shareUtil.toShareWeChatCircle();
                break;
            case R.id.ll_share_qq:
                shareUtil.toShareQQ();
                break;
            case R.id.ll_share_qzone:
                shareUtil.toShareQzone();
                break;
            case R.id.ll_share_copy:
                StringUtil.copy(mWorksData.shareurl, context);
                ToastUtils.toast(context, "复制成功!");
                break;
            case R.id.ll_poster:
                LyricsPosterActivity.toLyricsPosterActivity(context,mWorksData);
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
