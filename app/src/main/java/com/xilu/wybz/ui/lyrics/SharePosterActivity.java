package com.xilu.wybz.ui.lyrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.AppConstant;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.GalleryUtils;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.ImageUtils;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.dialog.ShareDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/8/1.
 */
public class SharePosterActivity extends ToolbarActivity {
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.rl_cover)
    RelativeLayout rlCover;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    private WorksData worksData;
    private String content;
    private String coverPath;
    private String savePath;
    private ShareDialog shareDialog;

    public static void toSharePosterActivity(Context context, WorksData worksData, String content) {
        Intent intent = new Intent(context, SharePosterActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        intent.putExtra(KeySet.CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_share_poster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        setTitle("歌词分享");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
            content = bundle.getString(KeySet.CONTENT);
            tvContent.setText(content);
            tvTitle.setText("『" + worksData.title.replace("\n", "") + "』\n作词：" + worksData.author);
            int width = DensityUtil.getScreenW(context);
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            if (StringUtils.isNotBlank(worksData.pic))
                ImageLoadUtil.loadImage(context, worksData.pic, ivCover, width, width);
            ivCover.setDrawingCacheEnabled(true);
            llContent.setDrawingCacheEnabled(true);
        }
    }

    @OnClick({R.id.rl_share, R.id.rl_save, R.id.iv_choice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_share:
                savePic();
                if (StringUtils.isNotBlank(savePath)) {
                    if (shareDialog == null) {
                        worksData.pic = savePath;
                        shareDialog = new ShareDialog(SharePosterActivity.this, worksData, 1);
                    }
                    if (!shareDialog.isShowing()) {
                        shareDialog.showDialog();
                    }
                }
                break;
            case R.id.iv_choice:
                GalleryUtils.getInstance().selectPicture(this);
                break;
            case R.id.rl_save:
                savePic();
                showMsg("已成功保存到文件夹：" + savePath);
                break;
        }
    }

    public void savePic() {
        Bitmap bitmap1 = BitmapUtils.loadBitmapFromView(ivCover);
        Bitmap bitmap2 = BitmapUtils.loadBitmapFromView(llContent);
        Bitmap bitmap = BitmapUtils.add2Bitmap(bitmap1, bitmap2);
        if (!new File(FileDir.posterPic).exists()) {
            new File(FileDir.posterPic).mkdirs();
        }
        if (PermissionUtils.checkSdcardPermission(this)) {
            savePath = FileDir.posterPic + System.currentTimeMillis() / 1000 + ".jpg";
            BitmapUtils.toSaveFile(savePath, bitmap);
        }
        bitmap1.recycle();
        bitmap2.recycle();
        bitmap.recycle();
        //保存成功的时候 通知上个页面关闭
        EventBus.getDefault().post(new Event.SavePosterSuccessEvent());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (null == data) {
            return;
        }
        Uri uri = null;
        if (requestCode == AppConstant.KITKAT_LESS) {
            uri = data.getData();
            Log.d("tag", "uri=" + uri);
            // 调用裁剪方法
            if (!new File(FileDir.coverPic).exists()) new File(FileDir.coverPic).mkdirs();
            coverPath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(coverPath));
            GalleryUtils.getInstance().cropPicture(this, uri, imgUri, 1, 1, 750, 750);
        } else if (requestCode == AppConstant.KITKAT_ABOVE) {
            uri = data.getData();
            Log.d("tag", "uri=" + uri);
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            if (!new File(FileDir.coverPic).exists()) new File(FileDir.coverPic).mkdirs();
            coverPath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(coverPath));
            GalleryUtils.getInstance().cropPicture(this,
                    Uri.fromFile(new File(thePath)), imgUri, 1, 1, 750, 750);
        } else if (requestCode == AppConstant.INTENT_CROP) {
            if (new File(coverPath).exists()) {
                int width = DensityUtil.getScreenW(context);
                ImageLoadUtil.loadImage(context, new File(coverPath), ivCover, width, width);
                worksData.setPic(coverPath);
            } else {
                showMsg("裁切失败");
            }
        }
    }
}
