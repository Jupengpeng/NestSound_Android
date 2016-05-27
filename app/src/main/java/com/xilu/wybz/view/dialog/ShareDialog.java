package com.xilu.wybz.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.utils.UmengShareUtil;


public class ShareDialog extends Dialog implements View.OnClickListener {
    Activity context;
    LinearLayout ll_wechat;
    LinearLayout ll_friend;
    LinearLayout ll_sina;
    LinearLayout ll_qq;
    LinearLayout ll_qzone;
    LinearLayout ll_copylink;
    UmengShareUtil shareUtil;
    ShareBean mShareBean;
    public ShareDialog(Activity context, ShareBean shareBean) {
        super(context, R.style.CommentDialog);
        if (shareUtil == null) {
            shareUtil = new UmengShareUtil(context, shareBean);
        }
        mShareBean = shareBean;
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
                StringUtil.copy(mShareBean.link, context);
                ToastUtils.toast(context, "复制成功!");
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
