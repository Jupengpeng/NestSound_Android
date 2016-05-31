package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.dao.DBManager;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.view.WaveSurfaceView;

import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/19.
 */
public class MakeSongActivity extends ToolbarActivity {
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.et_title)
    TextView etTitle;
    @Bind(R.id.et_word)
    TextView etWord;


    @Bind(R.id.make_sv_wave)
    WaveSurfaceView makeSvWave;
    //    @Bind(R.id.tv_time)
//    TextView tvTime;
//    @Bind(R.id.tv_alltime)
//    TextView tvAlltime;
    TemplateBean templateBean;
    WorksData worksData;
    long startRecordTime, startPlayTime, allTime;
    Timer recordTimer, playTimer;
    String RECORD_TAG;
    boolean isQc; //是不是清唱
    DBManager dbManager;

    WaveSurfaceHelper helper;
    @Bind(R.id.iv_record)
    ImageView ivRecord;


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
        initView();
        initData();
    }

    private void initView() {
//        setTitle("");
//        RecordInstance.getInstance().setSurfaceView(makeSvWave);
//        llMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        RecordInstance.getInstance().getMp3Recorder().setListenner(new MP3Recorder.OnWaveChangeListenner() {
            @Override
            public void onChange(List<Short> data) {
                makeSvWave.getWaveSurfaceHelper().onDrawWave(data,data.size());

                makeSvWave.getWaveSurfaceHelper().isrun = false;
            }
        });

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            templateBean = (TemplateBean) bundle.getSerializable("templateBean");
        }
        if (templateBean == null) isQc = true;
    }

    @OnClick({R.id.rl_import, R.id.rl_play, R.id.rl_record, R.id.rl_restart, R.id.rl_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_import:
                break;
            case R.id.rl_play:
                break;
            case R.id.rl_record:
//                startActivity(TestActivity.class);

                if (RecordInstance.getInstance().isStart()){

                    RecordInstance.getInstance().toPause();
                    showRecordPlay();
                } else {

                    RecordInstance.getInstance().toStart();
                    showRecordPause();
                }

                break;
            case R.id.rl_restart:
                break;
            case R.id.rl_edit:
                break;
        }
    }


    public void showRecordPlay(){
        ivRecord.setImageResource(R.drawable.ic_record_luyin_unstart);
    }

    public void showRecordPause(){
        ivRecord.setImageResource(R.drawable.ic_record_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
