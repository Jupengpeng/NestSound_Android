package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.TuningEffectAdapter;
import com.xilu.wybz.bean.TruningMusicBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.presenter.MakeSongPresenter;
import com.xilu.wybz.ui.IView.IMakeSongView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.WaveSurfaceView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SongTuningActivity extends ToolbarActivity implements IMakeSongView {


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

    MakeSongPresenter makeSongPresenter;

    WorksData worksData;
    //0:未开始  1：播放中  2：暂停  3：完成
    private int status = 0;

    private String[] musicurls = new String[5];
    private String recordurl;

    boolean tuning = false;
    boolean needPlay = false;
    boolean needSave = false;


    public static void toSongTuningActivity(Context context, WorksData worksData){
        Intent intent = new Intent(context, SongTuningActivity.class);
        intent.putExtra("worksData", worksData);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper = wave.getWaveSurfaceHelper();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper = null;
    }


    @Override
    public void finish() {
        super.finish();
        helper = null;
        MediaInstance.getInstance().stopMediaPlay();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_song_tuning;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initData();
        makeSongPresenter = new MakeSongPresenter(context,this);
        initView();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        List<Short> data = WaveSurfaceHelper.dataCache;
        if (data != null){
            helper.onDrawWave(data, 0);
            helper.caculateTotalSize();
        }
    }

    public void initView(){

        wave.setEnableTouch();
        helper = wave.getWaveSurfaceHelper();

        tuningEffect.setNestedScrollingEnabled(true);
        tuningEffect.setLayoutManager(new GridLayoutManager(context, 4));
        tuningEffect.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));

        List<TuningEffectAdapter.EffectBean> list = new ArrayList<>(10);
        list.add(new TuningEffectAdapter.EffectBean("原声",R.drawable.song_tuning_effect1,true));
        list.add(new TuningEffectAdapter.EffectBean("唱将",R.drawable.song_tuning_effect2));
        list.add(new TuningEffectAdapter.EffectBean("卡拉OK",R.drawable.song_tuning_effect3));
        list.add(new TuningEffectAdapter.EffectBean("魔声",R.drawable.song_tuning_effect4));
        list.add(new TuningEffectAdapter.EffectBean("专业",R.drawable.song_tuning_effect5));

        adapter = new TuningEffectAdapter(this,list);

        adapter.setOnItemClickListener(new TuningEffectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (tuning){
                    ToastUtils.toast(context,"正在合成中");
                    return;
                }

                if (StringUtils.isNotBlank(musicurls[position])){
                    playMusic();
                    adapter.setSelected(position);
                    return;
                }

                if (status == 2){
                    MediaInstance.getInstance().stopMediaPlay();
                    needPlay = true;
                }else {
                    needPlay = false;
                }
                adapter.setSelected(position);
                worksData.effect = position;
                worksData.musicurl = recordurl;
                makeSongPresenter.tuningMusic(worksData);
            }
        });

        tuningEffect.setAdapter(adapter);
        initMediaPlayer();


        setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                makeSongPresenter.cancelTuning();
                ToastUtils.toast(context,"取消音效处理");
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksData");

            recordurl = worksData.musicurl;
        }
    }



    @Override
    public void setLoadProgress(int progress) {

    }

    @Override
    public void setLoadFailed() {

    }

    @Override
    public void uploadSuccess(String message) {

    }

    @Override
    public void uploadFailed(String message) {

    }

    @Override
    public void tuningMusicSuccess(TruningMusicBean bean) {
        cancelPd();
        tuning = false;
        worksData.musicurl = bean.newPath;
        if (bean.effect>=0 && bean.effect < musicurls.length){
            musicurls[bean.effect] = bean.newPath;
        }

        if (needPlay){
            playMusic();
        } else if (needSave){
            worksData.recordurl = recordurl;
            SaveSongActivity.toSaveSongActivity(context,worksData);
        }
    }

    @Override
    public void tuningMusicFailed() {
        tuning = false;
        cancelPd();
    }

    public void tuningMusic(){

        tuning = true;
        int effect;
        int position = adapter.getSelectedPosition();
        if (position < 0 || position > musicurls.length){
            ToastUtils.toast(this,"处理效果错误");
            return;
        } else {
            effect = position;
        }

        worksData.musicurl = recordurl;
        worksData.effect = effect;
        makeSongPresenter.tuningMusic(worksData);

    }

    public void playMusic(){

        if (helper != null && helper.scroll){
            MediaInstance.getInstance().seek = helper.getCurrentPosition()*50;
        }else {
            MediaInstance.getInstance().seek = -1;
        }

        if (status == 2){
            MediaInstance.getInstance().resumeMediaPlay();
        }else {
            String url = MyHttpClient.PRE_ROOT+worksData.musicurl;
//            String url = MyHttpClient.PRE_ROOT+recordurl;
            MediaInstance.getInstance().startMediaPlayAsync(url);
//            MediaInstance.getInstance().startMediaPlay(FileUtils.getTempRecordPath());
        }
    }

    @OnClick(R.id.tuning_control)
    public void onClickControl(){

        int position = adapter.getSelectedPosition();

        if (musicurls[position] == null){
            showPd("处理中...");
            needPlay = true;
            tuningMusic();
        } else {
            playMusic();
        }
    }

    private void setScorll(boolean scorll){
        if (wave != null){
            wave.getWaveSurfaceHelper().scroll = scorll;
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
                setScorll(false);
            }


            @Override
            public void onStop() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                setScorll(false);
            }

            @Override
            public void onOver() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                setScorll(false);
            }

            @Override
            public void onError() {
                MediaInstance.getInstance().stopTimerTask();
                wave.setEnableTouch();
                status = 3;
                tuningControl.setSelected(false);
                setScorll(false);
            }
        });

        MediaInstance.getInstance().setOnProgressLitsener(new MediaInstance.OnProgressLitsener() {
            @Override
            public void progress(int progress) {
                if (helper != null){
                    helper.onDrawWave(progress / 50);
                }
            }
        });
    }

    private String getSelectedMusicUrl(){

        int poseition = adapter.getSelectedPosition();
        if (poseition < 0 || poseition >= musicurls.length){
            ToastUtils.toast(this,"音效选择错误");
            return null;
        }
        if (StringUtils.isBlank(musicurls[poseition])){
            return null;
        }
        return new String(musicurls[poseition]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_next){
            String musicUrl = getSelectedMusicUrl();
            if (musicUrl == null){
                needPlay = false;
                needSave = true;
                tuningMusic();
            } else {
                worksData.recordurl = recordurl;
                worksData.musicurl = musicUrl;
                SaveSongActivity.toSaveSongActivity(context,worksData);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeSongPresenter.cancelTuning();
    }
}
