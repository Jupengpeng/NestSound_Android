package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.dao.DBManager;
import com.xilu.wybz.presenter.MakeSongPresenter;
import com.xilu.wybz.ui.IView.IMakeSongView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.ImportWordActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.WaveSurfaceView;
import com.xilu.wybz.view.materialdialogs.GravityEnum;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/19.
 */
public class MakeSongActivity extends ToolbarActivity implements IMakeSongView {

    @Bind(R.id.ll_main)
    LinearLayout llMain;
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


    private TemplateBean templateBean;
    private WorksData worksData;
    private long startRecordTime, startPlayTime, allTime;
    private Timer recordTimer, playTimer;
    private String RECORD_TAG;
    private boolean isQc; //是不是清唱
    private DBManager dbManager;

    private String templateFileName;

    private WaveSurfaceHelper helper;

    private MakeSongPresenter makeSongPresenter;



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
        makeSongPresenter = new MakeSongPresenter(context,this);
        makeSongPresenter.init();

        EventBus.getDefault().register(this);

        if (PlayMediaInstance.getInstance().status == 3) {
            PlayMediaInstance.getInstance().pauseMediaPlay();
            EventBus.getDefault().post(new Event.PPStatusEvent(4));
        }

        upData();
    }

    @Override
    public void initView() {

        SystemUtils.isWiredHeadsetOn(this);

        waveSurface.setDisableTouch();

        helper = waveSurface.getWaveSurfaceHelper();

        RecordInstance.getInstance().getMp3Recorder().setListenner(new MP3Recorder.OnWaveChangeListenner() {
            @Override
            public void onChange(List<Short> data) {
                helper.onDrawWave(data, data.size());
            }

            @Override
            public void onstart() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onStop() {

            }
        });

    }

    public void upData(){

        templateFileName = FileUtils.getMusicCachePath(MyCommon.TYPE_TEMPLATE + templateBean.id);
        //if templateFileName not exists
        if (!FileUtils.fileExists(templateFileName)){

            materialDialog = new MaterialDialog.Builder(this)
                    .title("伴奏下载")
                    .content(R.string.please_wait_down)
                    .contentGravity(GravityEnum.CENTER)
                    .progress(false, 100, true)
                    .canceledOnTouchOutside(false).build();

            materialDialog.show();

            makeSongPresenter.loadFile(templateBean.mp3,templateFileName);
        }

    }


    @Override
    public void setLoadProgress(int progress) {
        if (materialDialog != null){
            materialDialog.setProgress(progress);
        }
    }

    public void showWorks(){

        etTitle.setText(worksData.title);
        etWord.setText(worksData.lyrics);

    }

    public void onEventMainThread(Event.ImportWordEvent event){
        this.worksData = event.getWorksData();
        showWorks();
    }

    public void onEventMainThread(Event.SaveLyricsSuccessEvent event){
        this.worksData = event.getLyricsdisplayBean();
        showWorks();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            templateBean = (TemplateBean) bundle.getSerializable("templateBean");
        }
        if (templateBean == null) isQc = true;
    }

    @OnClick({ R.id.iv_play, R.id.iv_record, R.id.iv_restart})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_play:


                break;
            case R.id.iv_record:

                if (RecordInstance.getInstance().isStart()) {

                    RecordInstance.getInstance().toPause();
                    showRecordStart();
                } else {

                    RecordInstance.getInstance().toStart();
                    showRecordPause();
                }

                break;
            case R.id.iv_restart:
                break;
        }
    }


    public void showRecordStart() {
        ivRecord.setImageResource(R.drawable.ic_record_unstart);
    }

    public void showRecordPause() {
        ivRecord.setImageResource(R.drawable.ic_record_start);
    }


    public void showSongPlay() {
        ivPlay.setImageResource(R.drawable.ic_replay_play);
    }

    public void showSongPause() {
        ivPlay.setImageResource(R.drawable.ic_replay_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_makesong, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_import:
                startActivity(ImportWordActivity.class);
                return true;
            case R.id.menu_next:
                SaveSongActivity.toSaveSongActivity(this,worksData);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
