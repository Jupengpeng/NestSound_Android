package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.ImageUtils;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.StringUtil;
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
            tvTitle.setText("『" + worksData.title + "』\n作词：" + worksData.author);
            int width = DensityUtil.getScreenW(context);
            int height = width * 2 / 3;
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            ImageLoadUtil.loadImage(context, worksData.pic, ivCover, width, height);
            ivCover.setDrawingCacheEnabled(true);
            llContent.setDrawingCacheEnabled(true);
        }
    }

    @OnClick({R.id.rl_share, R.id.rl_save, R.id.iv_coice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_share:
                savePic();
                if (StringUtil.isNotBlank(savePath)) {
                    if (shareDialog == null) {
                        worksData.pic = savePath;
                        shareDialog = new ShareDialog(SharePosterActivity.this, worksData, 1);
                    }
                    if (!shareDialog.isShowing()) {
                        shareDialog.showDialog();
                    }
                }
                break;
            case R.id.iv_coice:
                SystemUtils.openGallery(this);
                break;
            case R.id.rl_save:
                savePic();
                showMsg("已成功保存到文件夹：" + savePath);
                break;
        }
    }

    public void savePic() {
        if(StringUtil.isNotBlank(savePath))return;//已经保存过了
        Bitmap bitmap1 = BitmapUtils.loadBitmapFromView(ivCover);
        Bitmap bitmap2 = BitmapUtils.loadBitmapFromView(llContent);
        Bitmap bitmap = BitmapUtils.add2Bitmap(bitmap1, bitmap2);
        if (!new File(FileDir.posterPic).exists()) {
            new File(FileDir.posterPic).mkdirs();
        }
        if (PermissionUtils.checkSdcardPermission(this)) {
            savePath = FileDir.posterPic + System.currentTimeMillis() / 1000 + ".jpg";
            FileUtils.saveBmp(savePath, bitmap);
        }
        //保存成功的时候 通知上个页面关闭
        EventBus.getDefault().post(new Event.SavePosterSuccessEvent());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyCommon.requestCode_photo && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
                if (TextUtils.isEmpty(thePath)) {
                    coverPath = ImageUtils.getPath(this, uri);
                } else {
                    coverPath = thePath;
                }
                if (TextUtils.isEmpty(coverPath)) {
                    showMsg("图片读取失败~");
                    return;
                }
                File picture = new File(coverPath);
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                //获取图片的旋转角度
                int degree = ImageUtils.readPictureDegree(picture.getAbsolutePath());
                if (degree > 0) {//大于0的时候需要调整角度
                    String imagePath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
                    Bitmap cameraBitmap = BitmapFactory.decodeFile(coverPath, bitmapOptions);
                    Bitmap bitmap = ImageUtils.rotaingImageView(degree, cameraBitmap);
                    ImageUtils.saveBitmap(bitmap, imagePath);
                    coverPath = imagePath;

                }
                int width = DensityUtil.getScreenW(context);
                int height = width * 2 / 3;
                ImageLoadUtil.loadImage(context, new File(coverPath) , ivCover, width, height);
                worksData.setPic(coverPath);
            } else {
                showMsg("图片读取失败！");
            }
        }
    }
}
