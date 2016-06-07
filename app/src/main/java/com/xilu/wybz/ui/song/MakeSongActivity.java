package com.xilu.wybz.ui.song;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.TruningMusicBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.presenter.MakeSongPresenter;
import com.xilu.wybz.ui.IView.IMakeSongView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.ImportWordActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.WaveSurfaceView;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.GravityEnum;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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
    @Bind(R.id.iv_import)
    ImageView ivImport;
    @Bind(R.id.iv_edit)
    ImageView ivEdit;


    private TemplateBean templateBean;
    private WorksData worksData;
    private String RECORD_TAG;

    private boolean isQc;
    private boolean isPlay = false;

    private boolean useheadset = true;

    private String templateFileName;
    private String cacheFileName;

    private WaveSurfaceHelper helper;

    private MakeSongPresenter makeSongPresenter;


    private int status = 0; //0:未开始:1：录音中2：暂停3：完成


    protected MaterialDialog loadDialog;
    protected MaterialDialog backDialog;

    public static void ToMakeSongActivity(Context context, TemplateBean templateBean) {
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
        super.onCreate(savedInstanceState);
        initData();
        makeSongPresenter = new MakeSongPresenter(context, this);
        makeSongPresenter.init();

        EventBus.getDefault().register(this);

        if (PlayMediaInstance.getInstance().status == 3) {
            PlayMediaInstance.getInstance().pauseMediaPlay();
            PlayMediaInstance.getInstance().mediaPlayer.release();
            EventBus.getDefault().post(new Event.PPStatusEvent(4));
        }

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

        helper.setTotalSize(templateBean.mp3times/1000);

        RecordInstance.getInstance().getMp3Recorder().setOnWaveChangeListener(new MP3Recorder.OnWaveChangeListener() {
            @Override
            public void onChange(List<Short> data) {
                helper.onDrawWave(data, data.size());
            }

        });

//        etTitle.setEnabled(false);
//        etWord.setEnabled(false);

        registerHeadSetReceiver();

        RecordInstance.getInstance().setOnRecordStatuListener(new RecordInstance.OnRecordStatuListener() {
            @Override
            public void onRecordStart() {
                int duration = RecordInstance.getInstance().getMediaPlayer().getDuration();
                helper.setTotalSize(duration/1000);
            }

            @Override
            public void onRecordPause() {

            }

            @Override
            public void onRecordStop() {

            }

            @Override
            public void onRecordRestart() {

            }

            @Override
            public void onRecordReplay() {

            }

            @Override
            public void onRecordError() {

            }

            @Override
            public void onRecordComplete() {
                status = 3;
                showRecordStart();
            }
        });

    }

    public void upData() {

        cacheFileName = FileUtils.getMusicCachePath(MyCommon.TYPE_TEMPLATE + templateBean.id);
        templateFileName = cacheFileName + ".mp3";
        //if templateFileName not exists
        if (!FileUtils.fileExists(templateFileName)) {

            if (loadDialog == null) {

                loadDialog = new MaterialDialog.Builder(this)
                        .title("伴奏下载")
                        .content(R.string.please_wait_down)
                        .contentGravity(GravityEnum.CENTER)
                        .progress(false, 100, true)
                        .canceledOnTouchOutside(false).build();
            }

            loadDialog.show();

            makeSongPresenter.loadFile(templateBean.mp3, cacheFileName);
        }

    }


    @Override
    public void setLoadProgress(int progress) {
        if (loadDialog != null) {
            if (progress == 100) {
                loadDialog.cancel();
                showMsg("下载完成");
            } else {
                loadDialog.setProgress(progress);
            }
        }
    }

    @Override
    public void uploadSuccess(String musicurl) {
        worksData.musicurl = musicurl;
        makeSongPresenter.tuningMusic("" + worksData.uid, worksData);

        cancelPd();
    }

    @Override
    public void uploadFailed(String message) {
        cancelPd();
        showMsg("uploadFailed");
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

        etTitle.setText(worksData.title);
        etWord.setText(worksData.lyrics);

    }

    public void onEventMainThread(Event.ImportWordEvent event) {
        this.worksData = event.getWorksData();
        showWorks();
    }

    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
//        this.worksData = event.getLyricsdisplayBean();
//        showWorks();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            templateBean = (TemplateBean) bundle.getSerializable("templateBean");
        }
        if (templateBean == null) isQc = true;
    }

    @OnClick({R.id.iv_play, R.id.iv_record, R.id.iv_restart, R.id.iv_import, R.id.iv_edit})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_play:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }
                if (isPlay) {
                    showSongPlay();
                } else {
                    showSongPause();
                }

                break;

            case R.id.iv_restart:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }

                if (RecordInstance.getInstance().toRestart()) {
                    status = 1;
                    showRecordStart();
                } else {
                    showMsg("录音出错");
                }

                break;
            case R.id.iv_record:

                useheadset = SystemUtils.isWiredHeadsetOn(this);
                if (RecordInstance.getInstance().isStart()) {
                    stopRecord();
                } else {
                    startRecord();
                }
                break;

            case R.id.iv_import:
                if (status == 1) {
                    showMsg("请先停止录音");
                    return;
                }
                startActivity(ImportWordActivity.class);
                break;

            case R.id.iv_edit:
                if (etTitle.isEnabled()) {
                    etTitle.setEnabled(false);
                    etWord.setEnabled(false);
                    showMsg("关闭歌词编辑");
                } else {
                    etTitle.setEnabled(true);
                    etWord.setEnabled(true);
                    showMsg("开启歌词编辑");
                }
                break;
        }
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


    public void startRecord() {

        if (!FileUtils.fileExists(templateFileName)) {
            ToastUtils.toast(this, "请先下载伴奏");
            upData();
            return;
        }
        if (status == 2) {
            RecordInstance.getInstance().toReplay();
            status = 1;
        } else {
            RecordInstance.getInstance().setDataSource(templateFileName);
            RecordInstance.getInstance().toStart();
            status = 1;
        }

        showRecordStart();
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

//                if (worksData == null) {
//                    worksData = new WorksData();
//                }
//
//                worksData.hotid = 1;
//                worksData.musicurl = "Zero.mp3";
//
//                worksData.useheadset= "1";
//
//                makeSongPresenter.tuningMusic("1234",worksData);


                if (status == 1) {
                    showMsg("请先停止录音");
                    return true;
                }
                if (StringUtil.isBlank(etTitle.getText().toString())) {
                    showMsg("请填写标题");
                    return true;

                }
                if (worksData == null) {
                    worksData = new WorksData();
                }

                worksData.hotid = Integer.valueOf(templateBean.id);
                worksData.title = etTitle.getText().toString();
                worksData.lyrics = etWord.getText().toString();

                worksData.useheadset = useheadset == true ? "1":"0";
                UserBean user = PrefsUtil.getUserInfo(this);

                worksData.uid = user.userid;
                worksData.author = user.name;

                RecordInstance.getInstance().toStop();
                RecordInstance.getInstance().saveWaveDatas();
                boolean copyok = RecordInstance.getInstance().saveRecorderFileTo(FileUtils.getLocalRecordPath(MyCommon.TYPE_MAKE + templateBean.id));
                if (copyok){
                    worksData.recordmp3 = FileUtils.getLocalRecordPath(MyCommon.TYPE_MAKE + templateBean.id);
                } else {
                    showMsg("保存录音文件失败");
                    return true;
                }
//
                showPd("正在合成歌曲中");
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
            RecordInstance.getInstance().toPause();
        }
        if (backDialog == null) {

            backDialog = new MaterialDialog.Builder(this)
                    .title("请确认操作")
                    .content("如果你确认放弃录音，请点击左边“确认放弃”按钮。")
                    .positiveText("取消")
                    .neutralText("确认放弃")
                    .negativeColor(Color.parseColor("#ff0000"))
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RecordInstance.getInstance().destroy();
                            finish();
                        }
                    })
                    .contentGravity(GravityEnum.CENTER)
                    .canceledOnTouchOutside(true).build();
        }

        backDialog.show();

    }




    public class HeadSetPlugListenner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 2) == 0){
                    //拔出
                    useheadset = false;
                }else if (intent.getIntExtra("state", 2) == 1) {
                    //插入
//                    useheadset = true;
                }
            }
        }
    }

    BroadcastReceiver INSTANCE;

    private void registerHeadSetReceiver(){
        if (INSTANCE == null){
            INSTANCE = new HeadSetPlugListenner();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        context.registerReceiver(INSTANCE, filter);
    }
    private void unregisterHeadSetReceiver(){
        if (INSTANCE != null){
            context.unregisterReceiver(INSTANCE);
            INSTANCE = null;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //关闭及清理录音
        RecordInstance.getInstance().destroy();

        unregisterHeadSetReceiver();

        if (backDialog != null) {
            backDialog.cancel();
            backDialog = null;
        }
        if (loadDialog != null) {
            loadDialog.cancel();
            loadDialog = null;
        }
    }


}
