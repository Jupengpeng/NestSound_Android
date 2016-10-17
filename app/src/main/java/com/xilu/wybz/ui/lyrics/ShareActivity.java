package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ProductInfo;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.preserve.ApplyPreserveActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.UmengShareUtil;

import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/26.
 */
public class ShareActivity extends ToolbarActivity {
    WorksData worksData;
    UmengShareUtil shareUtil;
    private Bitmap overlay = null;

    /**
     *
     * @param context
     * @param worksData
     */
    public static void toShareActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("worksData", worksData);
        context.startActivity(intent);
    }

    /**
     *
     * @return
     */
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_share;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setNavigationIcon(R.drawable.ic_share_back);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksData");
        }
        String shareTitle = worksData.title;
        String shareAuthor = worksData.author;
        String shareLink = worksData.shareurl;
        String sharePic = MyHttpClient.QINIU_URL+worksData.pic;
        String shareBody = PrefsUtil.getUserId(context)==worksData.uid ? "我用音巢app创作了一首歌词，快来看看吧!" : "我在音巢app上发现一首好歌词，太棒了~";
        String shareContent = shareBody + " 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
        String playUrl = worksData.playurl;
        ShareBean shareBean = new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playUrl, 0);
        shareUtil = new UmengShareUtil(this,shareBean);
    }

    /**
     *
     * @param view
     */
    @OnClick({R.id.tv_weixin, R.id.tv_friend, R.id.tv_weibo, R.id.tv_qq, R.id.tv_qzone,R.id.share_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_weixin:
                shareUtil.toShareWeChat();
                break;
            case R.id.tv_friend:
                shareUtil.toShareWeChatCircle();
                break;
            case R.id.tv_weibo:
                shareUtil.toShareSina();
                break;
            case R.id.tv_qq:
                shareUtil.toShareQQ();
                break;
            case R.id.tv_qzone:
                shareUtil.toShareQzone();
                break;
            case R.id.share_submit:
                startPreserve();
                break;
        }
    }

    /**
     *
     */
    private void startPreserve(){

        ProductInfo info = new ProductInfo();

        info.id = worksData.itemid;
        info.type = worksData.type;

        ApplyPreserveActivity.start(this,info);
    }
}
