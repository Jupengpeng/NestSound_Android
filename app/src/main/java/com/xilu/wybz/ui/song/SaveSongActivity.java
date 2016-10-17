package com.xilu.wybz.ui.song;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
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
import com.xilu.wybz.bean.ShareResponseBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.presenter.SaveSongPresenter;
import com.xilu.wybz.ui.IView.IDownloadMusicView;
import com.xilu.wybz.ui.IView.ISaveSongView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.ShareActivity;
import com.xilu.wybz.utils.AppConstant;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.GalleryUtils;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.UploadFileUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/1.
 */
public class SaveSongActivity extends ToolbarActivity implements ISaveSongView, IDownloadMusicView {


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

    protected MaterialDialog loadDialog;

    private String cacheFileName;

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
        EventBus.getDefault().register(this);

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

    private void initEvent() {
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
                cancelWait();
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
                cancelWait();
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
                cancelWait();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SaveSongSeccess event) {
    }


    //上传封面图片
    public void uploadCoverPic() {
        UploadFileUtil uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(worksData.pic, MyCommon.fixxs[2], new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String imageUrl) {
                worksData.setPic(imageUrl);
                if (materialDialog != null && materialDialog.isShowing())
                    saveSongPresenter.saveSong(worksData);
            }

            @Override
            public void onFail() {
                showMsg("封面上传失败！");
            }
        });
    }


    @Override
    public void downloadSuccess(String message) {

    }

    @Override
    public void downloadFailed(String message) {

    }

    @Override
    public void downloadProgress(int progress) {

    }

    @Override
    public void saveWordSuccess(ShareResponseBean response) {
        EventBus.getDefault().post(new Event.SaveSongSeccess());

        worksData.shareurl = response.getCompleteShareUrl();
        worksData.itemid = response.itemid;

        worksData.playurl = MyHttpClient.ROOT_URL + worksData.musicurl;
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 0, 0));


        cancelPd();
        worksData.type = 1;
        ShareActivity.toShareActivity(this, worksData);

        finish();
    }

    @Override
    public void saveWordFail() {
        cancelPd();
    }


    @OnClick(R.id.iv_cover)
    public void onClickCover() {
        GalleryUtils.getInstance().selectPicture(this);
    }

    @OnClick(R.id.iv_play)
    public void onClickPlay() {

        if (StringUtils.isBlank(worksData.musicurl)) {
            showMsg("没有找到合成mp3文件");
            return;
        }

        if (status == 0 || status == 6) {
//            MediaInstance.getInstance().startMediaPlayAsync(worksData.musicurl);
            MediaInstance.getInstance().startMediaPlayAsync(MyHttpClient.ROOT_URL + worksData.musicurl);

            showWait();

            showbuttonPause();
            return;
        }
        if (MediaInstance.getInstance().isPlay()) {
            MediaInstance.getInstance().pauseMediaPlay();
            showbuttonPlay();
        } else {
            MediaInstance.getInstance().resumeMediaPlay();
            showbuttonPause();
        }
    }

    MaterialDialog wait;

    private void showWait() {
        wait = new MaterialDialog.Builder(this)
                .content("正在准备歌曲")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void cancelWait() {
        if (wait != null) {
            wait.cancel();
            wait = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                needDestroy = false;
                finish();
                return true;
            case R.id.menu_publish:

                if (StringUtils.isBlank(worksData.pic)) {
                    showMsg("请先选择歌曲的封面！");
                    return true;
                }
                needDestroy = true;
                worksData.is_issue = cbIsopen.isChecked() ? 1 : 0;
                showPd("正在发布中，请稍候...");
                if (materialDialog != null) {
                    materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            saveSongPresenter.cancelRequest();
                        }
                    });
                }
                if (new File(worksData.pic).exists()) {
                    uploadCoverPic();
                } else {
                    saveSongPresenter.saveSong(worksData);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
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
                int width = DensityUtil.dip2px(context,60);
                ImageLoadUtil.loadImage(context, new File(coverPath), ivCover, width, width);
                worksData.setPic(coverPath);
            } else {
                showMsg("裁切失败");
            }
        }
    }


    public void showbuttonPlay() {

        ivPlay.setImageResource(R.drawable.ic_replay_play);
    }

    public void showbuttonPause() {
        ivPlay.setImageResource(R.drawable.ic_replay_pause);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaInstance.getInstance().pauseMediaPlay();
        showbuttonPlay();
    }

    boolean needDestroy = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (needDestroy) {
            MediaInstance.getInstance().stopMediaPlay();
            MediaInstance.getInstance().destroy();
        }
        if (saveSongPresenter != null) {
            saveSongPresenter.cancelRequest();
        }
    }


}
