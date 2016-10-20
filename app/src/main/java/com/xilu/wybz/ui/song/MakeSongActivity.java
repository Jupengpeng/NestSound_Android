package com.xilu.wybz.ui.song;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.TruningMusicBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.DoubleMediaInstance;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.presenter.MakeSongPresenter;
import com.xilu.wybz.ui.IView.IMakeSongView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.ImportWordActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.WaveSurfaceView;
import com.xilu.wybz.view.dialog.CountdownDialog;
import com.xilu.wybz.view.dialog.MakeSongTipDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.GravityEnum;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/19.
 */
public class MakeSongActivity extends ToolbarActivity implements IMakeSongView {

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_word)
    EditText etWord;

    @Bind(R.id.make_sv_wave)
    WaveSurfaceView waveSurface;

    @Bind(R.id.iv_record)
    ImageView ivRecord;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_restart)
    ImageView ivRestart;

    @Bind(R.id.iv_import_banzhou)
    ImageView ivBanzhou;
    @Bind(R.id.iv_import_lrc)
    ImageView ivLrc;


    private TemplateBean templateBean;
    private WorksData worksData;
    private String RECORD_TAG;

    private boolean isQc;
    private boolean isPlay = false;

    private boolean useheadset = true;

    private String templateFileName;

    private WaveSurfaceHelper helper;

    private MakeSongPresenter makeSongPresenter;


    private int status = 0; //0:未开始  1：录音中  2：暂停  3：完成

    private int playInstance = 1;

    private int playProgress = -1;

    protected MaterialDialog loadDialog;
    protected MaterialDialog backDialog;
    protected MaterialDialog reStartDialog;
    private MakeSongTipDialog makeSongTipDialog;



    private boolean useCountDown = false;

    public static void toMakeSongActivity(Context context, TemplateBean templateBean) {
        Intent intent = new Intent(context, MakeSongActivity.class);
        intent.putExtra("templateBean", templateBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makesong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        useCountDown = PrefsUtil.getBoolean(KeySet.KEY_COUNTDOWN_OPEN,getApplicationContext());
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initData();
        if(!PrefsUtil.getBoolean("isMakeSongTip",context)){
            makeSongTipDialog = new MakeSongTipDialog(context);
            makeSongTipDialog.showDialog();
            PrefsUtil.putBoolean("isMakeSongTip",true,context);
        }
        if (MyApplication.getInstance().mMainService == null) {
            MyApplication.getInstance().bindMainService();
        }else{
            MyApplication.getInstance().mMainService.doRelease();
        }
        makeSongPresenter = new MakeSongPresenter(context, this);
        makeSongPresenter.init();

        EventBus.getDefault().register(this);

        upData();
    }

    @Override
    public void initView() {

        useheadset = SystemUtils.isWiredHeadsetOn(this);

        waveSurface.setDisableTouch();

        if (RecordInstance.getInstance().isStart()) {
            RecordInstance.getInstance().toStop();
        }

        helper = waveSurface.getWaveSurfaceHelper();

        helper.setTotalSize(templateBean.mp3times / 1000);

        RecordInstance.getInstance().getMp3Recorder().setOnWaveChangeListener(new MP3Recorder.OnWaveChangeListener() {
            @Override
            public void onChange(List<Short> data) {
                helper.onDrawWave(data, data.size());
            }

        });

//        etTitle.setEnabled(false);
//        etWord.setEnabled(false);

        registerHeadSetReceiver();
        RecordInstance.getInstance().deleteCacheFile();
        RecordInstance.getInstance().setOnRecordStatuListener(new RecordInstance.OnRecordStatuListener() {
            @Override
            public void onRecordStart() {
                int duration = RecordInstance.getInstance().getMediaPlayer().getDuration();
                helper.setTotalSize((duration +990)/ 1000);
            }

            @Override
            public void onRecordPause() {

            }

            @Override
            public void onRecordStop() {
                showRecordPause();
            }

            @Override
            public void onRecordRestart() {

            }

            @Override
            public void onRecordReplay() {

            }

            @Override
            public void onRecordError() {
                showRecordPause();
            }

            @Override
            public void onRecordComplete() {
                status = 3;
                showRecordPause();
            }
        });

    }

    public void upData() {
        if(!new File(FileDir.hotDir).exists())new File(FileDir.hotDir).mkdirs();
        templateFileName = FileDir.hotDir+ MD5Util.getMD5String(templateBean.mp3);
        //if templateFileName not exists
        if (!FileUtils.fileExists(templateFileName)) {
            if(makeSongTipDialog==null) {
                if (loadDialog == null) {
                    loadDialog = new MaterialDialog.Builder(this)
                            .title(R.string.progress_dialog)
                            .content(R.string.please_wait_init)
                            .contentGravity(GravityEnum.CENTER)
                            .progress(false, 100, true)
                            .canceledOnTouchOutside(false).build();
                }
                loadDialog.show();
            }
            if(PermissionUtils.checkSdcardPermission(this)) {
                makeSongPresenter.loadFile(templateBean.mp3, templateFileName);
            }
        }
    }


    @Override
    public void setLoadProgress(int progress) {
        if (loadDialog != null) {
            if (progress == 100) {
                loadDialog.cancel();
                showMsg(getString(R.string.init_ok));
            } else {
                loadDialog.setProgress(progress);
            }
        }
    }

    @Override
    public void setLoadFailed() {
        if (loadDialog != null) {
            loadDialog.cancel();
            showMsg("初始化失败");
        }
    }

    @Override
    public void uploadSuccess(String musicurl) {
        worksData.musicurl = musicurl;

        if ("1".equals(worksData.useheadset)){
            SongTuningActivity.toSongTuningActivity(context,worksData);
            cancelPd();
        } else {
            makeSongPresenter.tuningMusic(worksData);
        }
    }

    @Override
    public void uploadFailed(String message) {
        cancelPd();
        showMsg("上传失败");
    }

    @Override
    public void tuningMusicSuccess(TruningMusicBean bean) {

        worksData.recordurl = bean.oldPath;
        worksData.musicurl = bean.newPath;
        cancelPd();
        SaveSongActivity.toSaveSongActivity(this, worksData);
    }

    @Override
    public void tuningMusicFailed() {
        worksData.musicurl = templateBean.mp3;
        cancelPd();
//        SaveSongActivity.toSaveSongActivity(this, worksData);
    }

    public void showWorks() {

        if (etTitle == null){
            return;
        }
        etTitle.setText(worksData.title);
        etWord.setText(worksData.lyrics);

    }

    public void reSetActivity(){

        useheadset = true;
        status = 0; //0:未开始  1：录音中  2：暂停  3：完成
        playInstance = 1;
        playProgress = -1;
        helper.data = null;

        DoubleMediaInstance.getInstance().stopMediaPlay();
        MediaInstance.getInstance().stopMediaPlay();
        RecordInstance.getInstance().toStop();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ImportHotEvent event) {
        this.templateBean = event.getWorksData();

        reSetActivity();

        upData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ImportWordEvent event) {
        this.worksData = new WorksData();
        this.worksData.title = event.getWorksData().title;
        this.worksData.lyrics = event.getWorksData().lyrics;
        showWorks();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SaveSongSeccess event) {
        finish();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            templateBean = (TemplateBean) bundle.getSerializable("templateBean");
        }
        if (templateBean == null) isQc = true;
    }

//    @OnClick({R.id.iv_play, R.id.iv_record, R.id.iv_restart, R.id.iv_import, R.id.iv_edit})
    @OnClick({R.id.iv_play, R.id.iv_record, R.id.iv_restart, R.id.iv_import_banzhou, R.id.iv_import_lrc})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_play:

                if (status == 0){
                    showMsg("还未开始录音");
                    return;
                }
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }

                if (isPlay) {
                    pausePlay();
                    showSongPlay();
                    helper.scroll = false;
                } else {

                    if (helper.scroll){
                        playProgress = helper.getCurrentPosition()*50;
                        MediaInstance.getInstance().seek = playProgress;
                        DoubleMediaInstance.getInstance().seek = playProgress;
                        Log.d("play","playProgress="+playProgress);
                    } else {
                        MediaInstance.getInstance().seek = -1;
                        DoubleMediaInstance.getInstance().seek = -1;
                    }

                    startPlay();
                    showSongPause();
                }

                break;

            case R.id.iv_restart:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }

                if (isPlay) {
                    showMsg("请先停止播放");
                    return;
                }

                if (status == 0){
                    showMsg("还未开始录音");
                    return;
                }

                reStartDialog = new MaterialDialog.Builder(this)
                        .title("请确认操作")
                        .content("请确认是否重新录音？")
                        .negativeText("取消")
                        .positiveText("重新录音")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (helper != null){
                                    helper.onDrawWave(new ArrayList<>(), 0);
                                }
                                /**
                                 * reSetPlayer.
                                 */
                                reSetPlayer();
                                status = 0;

                                try {
                                    RecordInstance.getInstance().toStop();
                                    RecordInstance.getInstance().deleteCacheFile();
                                } catch (RuntimeException e){
                                }

//                                if (useCountDown){
//                                    countDownRestart();
//                                } else {
//                                    functionReStart();
//                                }
                            }
                        })
                        .canceledOnTouchOutside(true).build();
                reStartDialog.show();
                break;
            case R.id.iv_record:

                if(!PermissionUtils.checkSdcardPermission(this)){
                    return;
                }
                if(!PermissionUtils.checkRecordAudioPermission(this)){
                    return;
                }
                if (isPlay) {
                    showMsg("请先停止播放");
                    return;
                }
                if (status == 3){
                    showMsg("已经录制完成");
                    return;
                }

                reSetPlayer();

                if (useheadset) {
                    useheadset = SystemUtils.isWiredHeadsetOn(this);
                }

                if (RecordInstance.getInstance().isStart()) {
                    stopRecord();
                } else {

                    if (!FileUtils.fileExists(templateFileName)) {
                        ToastUtils.toast(context, "等待初始化");
                        upData();
                        return;
                    }
                    showRecordStart();
                    if (useCountDown){
                        countDownStartRecord();
                    } else {
                        startRecord();
                    }
                }
                break;

            case R.id.iv_import_banzhou:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }
                HotCatalogActivity.toHotCatalogActivity(this,true);
                break;

            case R.id.iv_import_lrc:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }
                startActivity(ImportWordActivity.class);
                break;
        }
    }

    /**
     * 倒计时，重新开始.
     */
    private void countDownRestart(){

        if (dialog == null){
            dialog = new CountdownDialog(context);
        }

        dialog.setListener(new CountdownDialog.OnOkListener() {
            @Override
            public void onOk() {
                functionReStart();
            }
        });
        dialog.startCountDown();
    }

    private void functionReStart(){

        if (RecordInstance.getInstance().toRestart()) {
            status = 1;
            showRecordStart();
        } else {
            showMsg("录音出错");
        }
    }


    private void reSetPlayer(){
        if (isPause){
            stopPlay();
            isPause =  false;
        }
    }

    private void startPlay() {
        if (isPause){
            if (playInstance == 1) {
                MediaInstance.getInstance().resumeMediaPlay();
            } else {
                DoubleMediaInstance.getInstance().resumeMediaPlay();
            }

            isPause = false;
            isPlay = true;
            return;
        }

        if (!useheadset) {

            MediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
                @Override
                public void onStart() {
                    MediaInstance.getInstance().startTimerTask();
                    waveSurface.setDisableTouch();
                }


                @Override
                public void onPlay() {
                    MediaInstance.getInstance().startTimerTask();
                    waveSurface.setDisableTouch();
                }

                @Override
                public void onPause() {
                    MediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    helper.scroll = false;
                }


                @Override
                public void onStop() {
                    MediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    showSongPlay();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }

                @Override
                public void onOver() {
                    MediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    showSongPlay();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }

                @Override
                public void onError() {
                    MediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    showSongPlay();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }
            });

            MediaInstance.getInstance().setOnProgressLitsener(new MediaInstance.OnProgressLitsener() {
                @Override
                public void progress(int progress) {
                    helper.onDrawWave(progress / 50);
                }
            });

            MediaInstance.getInstance().startMediaPlay(FileUtils.getTempRecordPath());

            playInstance = 1;

        } else {

            DoubleMediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
                @Override
                public void onStart() {
                    DoubleMediaInstance.getInstance().startTimerTask();
                    waveSurface.setDisableTouch();
                }

                @Override
                public void onPlay() {
                    DoubleMediaInstance.getInstance().startTimerTask();
                    waveSurface.setDisableTouch();
                }

                @Override
                public void onPause() {
                    DoubleMediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    helper.scroll = false;
                }

                @Override
                public void onStop() {
                    DoubleMediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    showSongPlay();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }

                @Override
                public void onOver() {
                    DoubleMediaInstance.getInstance().stopTimerTask();
                    showSongPlay();
                    waveSurface.setEnableTouch();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }

                @Override
                public void onError() {
                    DoubleMediaInstance.getInstance().stopTimerTask();
                    waveSurface.setEnableTouch();
                    showSongPlay();
                    isPlay = false;
                    isPause = false;
                    helper.scroll = false;
                }
            });

            DoubleMediaInstance.getInstance().setOnProgressLitsener(new DoubleMediaInstance.OnProgressLitsener() {
                @Override
                public void progress(int progress) {
                    helper.onDrawWave(progress / 50);
                }
            });

            DoubleMediaInstance.getInstance().startMediaPlay(FileUtils.getTempRecordPath(), templateFileName);

            playInstance = 2;
        }
        isPlay = true;
    }

    boolean isPause = false;

    private void pausePlay() {

        if (playInstance == 1) {
            MediaInstance.getInstance().pauseMediaPlay();
        } else {
            DoubleMediaInstance.getInstance().pauseMediaPlay();
        }
        isPause = true;
        isPlay = false;

    }

    private void stopPlay() {

        if (playInstance == 1) {
            MediaInstance.getInstance().destroy();
        } else {
            DoubleMediaInstance.getInstance().destroy();
        }
        isPause = false;
        isPlay = false;

    }


    public void showRecordStart() {
        ivRecord.setImageResource(R.drawable.ic_record_start);
        waveSurface.setDisableTouch();
    }

    public void showRecordPause() {
        ivRecord.setImageResource(R.drawable.ic_record_unstart);
        waveSurface.setEnableTouch();
    }



    public void showSongPlay() {
        ivPlay.setImageResource(R.drawable.ic_replay_play);
    }

    public void showSongPause() {
        ivPlay.setImageResource(R.drawable.ic_replay_pause);
    }

    private CountdownDialog dialog;

    /**
     *  倒计时开始录音.
     */
    private void countDownStartRecord(){

        if (dialog == null){
            dialog = new CountdownDialog(context);
        }

        dialog.setListener(new CountdownDialog.OnOkListener() {
            @Override
            public void onOk() {
                startRecord();
            }
        });
        dialog.startCountDown();
    }

    public void startRecord() {
        if (status == 2) {
            RecordInstance.getInstance().toReplay();
            status = 1;
        } else {
            RecordInstance.getInstance().setDataSource(templateFileName);
            RecordInstance.getInstance().toStart();
            status = 1;
        }
    }

    public void stopRecord() {

        RecordInstance.getInstance().toPause();
        status = 2;

        showRecordPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_makesong, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_next:

                if (MediaInstance.getInstance().isPlay()){
                    MediaInstance.getInstance().pauseMediaPlay();
                }

                if (status == 1) {
                    showMsg("请先停止录音");
                    return true;
                }
                if (status == 0){
                    showMsg("还未开始录音");
                    return true;
                }
                if (StringUtils.isBlank(etTitle.getText().toString())) {
                    showMsg("请填写标题");
                    return true;
                }

                WaveSurfaceHelper.dataCache = helper.data;

                if (worksData == null) {
                    worksData = new WorksData();
                }

                worksData.hotid = Integer.valueOf(templateBean.id);
                worksData.title = etTitle.getText().toString();
                worksData.lyrics = etWord.getText().toString();

                worksData.useheadset = useheadset == true ? "1" : "0";
                UserBean user = PrefsUtil.getUserInfo(this);

                worksData.effect = 0;
                worksData.uid = user.userid;
                worksData.author = user.name;

//                RecordInstance.getInstance().toStop();
//                RecordInstance.getInstance().saveWaveDatas();

                boolean copyok = RecordInstance.getInstance().saveRecorderFileTo(FileUtils.getLocalRecordPath(MyCommon.TYPE_MAKE + templateBean.id));
                if (copyok) {
                    worksData.recordmp3 = FileUtils.getLocalRecordPath(MyCommon.TYPE_MAKE + templateBean.id);
                } else {
                    showMsg("保存录音文件失败");
                    return true;
                }

                if (useheadset){
//                    startActivity(SongTuningActivity.class);
//                    return true;
                    showPd("歌曲正在上传，请稍候...");
                } else {
                    showPd("歌曲正在美化，请稍候...");

                }

                makeSongPresenter.uploadmp3File(FileUtils.getLocalRecordPath(MyCommon.TYPE_MAKE + templateBean.id));

                return true;
            case android.R.id.home:
                onKeyBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (status == 1) {
            RecordInstance.getInstance().toPause();
            status = 2;
            showRecordPause();
        }
    }


    @Override
    public void onBackPressed() {
        onKeyBack();
    }

    private void onKeyBack() {

        if (status == 0) {
            RecordInstance.getInstance().destroy();
            finish();
            return;
        }
        if (status == 1) {
            stopRecord();
        }
        if (backDialog == null) {

            backDialog = new MaterialDialog.Builder(this)
                    .title("请确认操作")
                    .content("是否放弃当前录音？")
                    .negativeText("取消")
                    .positiveText("确认放弃")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RecordInstance.getInstance().destroy();
                            finish();
                        }
                    })
                    .canceledOnTouchOutside(true).build();
        }

        backDialog.show();

    }


    public class HeadSetPlugListenner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 2) == 0) {
                    //拔出
                    if (status == 1){
                        useheadset = false;
                    }
                } else if (intent.getIntExtra("state", 2) == 1) {
                    //插入
                    if (status == 0){
                        useheadset = true;
                    }
                }
            }
        }
    }

    BroadcastReceiver INSTANCE;

    private void registerHeadSetReceiver() {
        if (INSTANCE == null) {
            INSTANCE = new HeadSetPlugListenner();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        context.registerReceiver(INSTANCE, filter);
    }

    private void unregisterHeadSetReceiver() {
        if (INSTANCE != null) {
            context.unregisterReceiver(INSTANCE);
            INSTANCE = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        //关闭及清理录音
        RecordInstance.getInstance().destroy();

        MediaInstance.getInstance().destroy();

        DoubleMediaInstance.getInstance().destroy();

        unregisterHeadSetReceiver();

        if (backDialog != null) {
            backDialog.cancel();
            backDialog = null;
        }
        if (loadDialog != null) {
            loadDialog.cancel();
            loadDialog = null;
        }
        if (makeSongPresenter != null){
            makeSongPresenter.cancelRequest();
            makeSongPresenter= null;
        }
        super.onDestroy();
    }

}
