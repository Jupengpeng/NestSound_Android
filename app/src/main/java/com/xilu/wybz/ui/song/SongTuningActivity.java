package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.TuningEffectAdapter;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.WaveSurfaceView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SongTuningActivity extends ToolbarActivity {


    @Bind(R.id.tuning_header)
    LinearLayout tuningHeader;
    @Bind(R.id.tuning_text)
    TextView tuningText;
    @Bind(R.id.tuning_effect)
    RecyclerView tuningEffect;
    @Bind(R.id.tuning_control)
    ImageView tuningControl;
    @Bind(R.id.tuning_wave)
    WaveSurfaceView wave;

    TuningEffectAdapter adapter;
    WaveSurfaceHelper helper;


    WorksData worksData;
    //0:未开始  1：录音中  2：暂停  3：完成
    private int status = 0;


    public static void toSongTuningActivity(Context context, WorksData worksData){
        Intent intent = new Intent(context, SongTuningActivity.class);
        intent.putExtra("worksData", worksData);
        context.startActivity(intent);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksData");
        }
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_song_tuning;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();

    }





    public void initView(){

        wave.setEnableTouch();
        helper = wave.getWaveSurfaceHelper();
//        helper.setTotalSize(1000);

//        List<Short> data = new ArrayList<>();
//        data.add((short)12);
//        data.add((short)120);
//        data.add((short)612);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Short> data = WaveSurfaceHelper.dataCache;
                if (data != null){
                    helper.onDrawWave(data, 0);
                    helper.caculateTotalSize();
                }

            }
        },100);

        tuningEffect.setNestedScrollingEnabled(true);
        tuningEffect.setLayoutManager(new GridLayoutManager(context, 4));
        tuningEffect.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));

        List<TuningEffectAdapter.EffectBean> list = new ArrayList<>(10);
        list.add(new TuningEffectAdapter.EffectBean("原声",R.drawable.song_tuning_effect1,true));
        list.add(new TuningEffectAdapter.EffectBean("唱将",R.drawable.song_tuning_effect2));
        list.add(new TuningEffectAdapter.EffectBean("卡拉OK",R.drawable.song_tuning_effect3));
        list.add(new TuningEffectAdapter.EffectBean("魔声",R.drawable.song_tuning_effect4));
        list.add(new TuningEffectAdapter.EffectBean("剧场",R.drawable.song_tuning_effect5));

        adapter = new TuningEffectAdapter(this,list);

        tuningEffect.setAdapter(adapter);
        initMediaPlayer();
    }

    @OnClick(R.id.tuning_control)
    public void onClickControl(){

        if (tuningControl.isSelected()){
            MediaInstance.getInstance().pauseMediaPlay();

        } else {

            if (helper.scroll){

                MediaInstance.getInstance().seek = helper.getCurrentPosition()*50;
            }else {
                MediaInstance.getInstance().seek = -1;
            }

            if (status == 2){
                MediaInstance.getInstance().resumeMediaPlay();
            }else {
                MediaInstance.getInstance().startMediaPlay(FileUtils.getTempRecordPath());
            }
        }

    }

    public void initMediaPlayer(){

        MediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                MediaInstance.getInstance().startTimerTask();
                tuningControl.setSelected(true);
                wave.setDisableTouch();
            }


            @Override
            public void onPlay() {
                MediaInstance.getInstance().startTimerTask();
                tuningControl.setSelected(true);
                wave.setDisableTouch();
            }

            @Override
            public void onPause() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                tuningControl.setSelected(false);
                status = 2;
                helper.scroll = false;
            }


            @Override
            public void onStop() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                helper.scroll = false;
            }

            @Override
            public void onOver() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                helper.scroll = false;
            }

            @Override
            public void onError() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                helper.scroll = false;
            }
        });

        MediaInstance.getInstance().setOnProgressLitsener(new MediaInstance.OnProgressLitsener() {
            @Override
            public void progress(int progress) {
                helper.onDrawWave(progress / 50);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.menu.menu_next){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
