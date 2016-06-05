package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.presenter.SaveSongPresenter;
import com.xilu.wybz.ui.IView.ISaveSongView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.ImageUploader;
import com.xilu.wybz.utils.ImageUtils;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadFileUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/1.
 */
public class SaveSongActivity extends ToolbarActivity implements ISaveSongView {


    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.cb_isopen)
    CheckBox cbIsopen;

    private WorksData worksData;

    private String coverPath;

    private int status = 0;

    SaveSongPresenter saveSongPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_savesong;
    }

    public static void toSaveSongActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, SaveSongActivity.class);
        intent.putExtra("worksData", worksData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        saveSongPresenter = new SaveSongPresenter(context, this);
        saveSongPresenter.init();
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksData");
        }
    }

    @Override
    public void initView() {

        initEvent();
    }

    private void initEvent(){
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                worksData.setDetail(s.toString().trim());
                if (s.length() == 140) {
                    showMsg("最多可输入140个字");
                }
            }
        });
        cbIsopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                worksData.is_issue = cbIsopen.isChecked() ? 1 : 0;
            }
        });


        MediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                status = 1;
                showMsg("播放开始");
            }

            @Override
            public void onStop() {
                status = 2;
                showMsg("播放停止");
                showbuttonPlay();
            }

            @Override
            public void onPlay() {
                status = 3;
                showMsg("继续播放");
            }

            @Override
            public void onPause() {
                status = 4;
                showMsg("播放暂停");
            }

            @Override
            public void onOver() {
                status = 5;
                showbuttonPlay();
                showMsg("播放结束");
            }

            @Override
            public void onError() {
                status = 6;
                showbuttonPlay();
                showMsg("播放错误");
            }
        });
    }


    //上传封面图片
    public void uploadCoverPic() {
        showPd("正在保存中，请稍候...");
        UploadFileUtil uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(worksData.pic, ImageUploader.fixxs[1], new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String imageUrl) {
                worksData.setPic(imageUrl);
//                saveSongPresenter.saveSong(worksData);
            }

            @Override
            public void onFail() {
                Log.e("onFail", "onFail");
                showMsg("上传封面失败！");
            }
        });
    }


    @Override
    public void saveWordSuccess(String response) {

    }

    @Override
    public void saveWordFail() {

    }

    @Override
    public void onFinish() {

    }

    @OnClick(R.id.cb_isopen)
    public void onClickOpen() {

    }

    @OnClick(R.id.iv_cover)
    public void onClickCover() {
        SystemUtils.openGallery(this);
    }

    @OnClick(R.id.iv_play)
    public void onClickPlay() {

        if (StringUtil.isBlank(worksData.recordmp3) || !FileUtils.fileExists(worksData.recordmp3)){
            showMsg("没有找到录音文件");
            return;
        }

        if(status == 0 || status == 6){
            MediaInstance.getInstance().startMediaPlay(worksData.recordmp3);
            showbuttonPause();
            return;
        }
        if ( MediaInstance.getInstance().isPlay()){
            MediaInstance.getInstance().pauseMediaPlay();
            showbuttonPlay();
        } else {
            MediaInstance.getInstance().resumeMediaPlay();
            showbuttonPause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_publish) {
            //先检查歌词的描述
            if(StringUtil.isBlank(worksData.detail)){
                showMsg("请先添加歌曲描述！");
                return true;
            }
            if(StringUtil.isBlank(worksData.pic)){
                showMsg("请先选择歌曲的封面！");
                return true;
            }
            if(new File(worksData.pic).exists()){
                uploadCoverPic();
            }else{
                saveSongPresenter.saveSong(worksData);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    String imagePath = DownLoaderDir.coverPic + System.currentTimeMillis() + ".jpg";
                    Bitmap cameraBitmap = BitmapFactory.decodeFile(coverPath, bitmapOptions);
                    Bitmap bitmap = ImageUtils.rotaingImageView(degree, cameraBitmap);
                    ImageUtils.saveBitmap(bitmap, imagePath);
                    coverPath = imagePath;

                }
                loadImage("file://" + coverPath, ivCover);
                worksData.setPic(coverPath);
            } else {
                showMsg("图片读取失败！");
            }
        }
    }


    public void showbuttonPlay(){

        ivPlay.setImageResource(R.drawable.ic_replay_play);
    }

    public void showbuttonPause(){
        ivPlay.setImageResource(R.drawable.ic_replay_pause);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MediaInstance.getInstance().pauseMediaPlay();
        showbuttonPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaInstance.getInstance().stopMediaPlay();
    }


}
