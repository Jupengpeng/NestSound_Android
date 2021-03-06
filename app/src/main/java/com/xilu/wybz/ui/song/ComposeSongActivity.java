package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.view.WaveSurfaceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 合成调节歌曲页面.
 * Created by Administrator on 2016/6/1.
 */
public class ComposeSongActivity extends ToolbarActivity {


    @Bind(R.id.et_title)
    TextView etTitle;
    @Bind(R.id.et_word)
    TextView etWord;
    @Bind(R.id.make_sv_wave)
    WaveSurfaceView waveSurface;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.tv_play)
    TextView tvPlay;
    @Bind(R.id.ll_paly)
    LinearLayout llPaly;
    @Bind(R.id.iv_prettify)
    ImageView ivPrettify;
    @Bind(R.id.tv_prettify)
    TextView tvPrettify;
    @Bind(R.id.ll_prettify)
    LinearLayout llPrettify;
    @Bind(R.id.sb_original)
    SeekBar sbOriginal;
    @Bind(R.id.sb_accompany)
    SeekBar sbAccompany;


    private WorksData worksData;
    private List<Short> waveDatas;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_composesong;
    }

    public static void toComposeSongActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, ComposeSongActivity.class);
        intent.putExtra("worksData", worksData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        EventBus.getDefault().register(this);
    }
    public void initView(){

        initSeekBar();

        showWorks();

        waveDatas = RecordInstance.getInstance().waveDatas;
        if (waveDatas != null){
            waveSurface.getWaveSurfaceHelper().onDrawWave(waveDatas,0);
            waveSurface.setEnableTouch();
        }

    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        }
    }

    private void initSeekBar(){
        sbOriginal.setMax(100);
        sbOriginal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAccompany.setMax(100);
        sbAccompany.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.ImportWordEvent event){
        this.worksData = event.getWorksData();
        showWorks();
    }


    public void showWorks() {

        if (worksData != null){
            etTitle.setText(worksData.title);
            etWord.setText(worksData.lyrics);
        }

    }

    @OnClick({R.id.iv_play, R.id.iv_prettify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                break;
            case R.id.iv_prettify:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_next) {
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
