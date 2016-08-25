package com.xilu.wybz.ui.lyrics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.bean.TemplateLrcBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.presenter.DraftPresenter;
import com.xilu.wybz.ui.IView.ISimpleView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MakeWordByTempleateActivity extends BaseListActivity<TemplateLrcBean> implements ISimpleView {

    DraftPresenter draftPresenter;
    LyricsDraftBean lyricsDraftBean;


    //0:未开始  1：播放中  2：暂停  3：完成 4：准备中
    private int status = 0;

    private SeekBar seekBar;
    private ImageView playControl;
    private TextView curentTime;
    private TextView totalTime;

    String playurl = "http://audio.yinchao.cn/uploadfiles/2016/03/10/201603101650181457599818.mp3";
    int mp3time = 0;

    public static void toMakeWordByTempleateActivity(Activity context,LyricsDraftBean bean){
        Intent intent = new Intent(context,MakeWordByTempleateActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, bean);
        context.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        draftPresenter = new DraftPresenter(context,this);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyApplication.getInstance().mMainService != null) {
            MyApplication.getInstance().mMainService.doRelease();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setTitle("模板");
        hideRight();
        tvNoData.setText("没有数据");

        recycler.enablePullToRefresh(false);
        recycler.enableLoadMore(false);
//        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#fff8f8f8"));

    }

    @Override
    protected void setUpData() {
        super.setUpData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lyricsDraftBean = (LyricsDraftBean) bundle.getSerializable(KeySet.WORKS_DATA);
        }

        if (StringUtils.isNotBlank(lyricsDraftBean.mp3)){
            playurl = lyricsDraftBean.mp3;
            mp3time = lyricsDraftBean.mp3time;
            loadFootBar();
        }
//        loadFootBar();
        mDataList = new ArrayList<>();
        mDataList.add(new TemplateLrcBean("歌名："+lyricsDraftBean.title,""));
        String content = lyricsDraftBean.content;
        content = SystemUtils.formatByEnter(content);
        String[] items = content.split("#");
        for (String item:items){
            mDataList.add(new TemplateLrcBean(item,""));
        }

    }

    public void loadFootBar() {

        ViewStub stub = (ViewStub) findViewById(R.id.view_footbar_send);
        stub.setLayoutResource(R.layout.view_footbar_player);
        LinearLayout llFootBar = (LinearLayout) stub.inflate();

        seekBar = (SeekBar) llFootBar.findViewById(R.id.playSeekBar);
        playControl = (ImageView) llFootBar.findViewById(R.id.iv_play);
        curentTime = (TextView) llFootBar.findViewById(R.id.tv_time);
        totalTime = (TextView) llFootBar.findViewById(R.id.tv_alltime);

        curentTime.setText(DateFormatUtils.formatTime(0));
        totalTime.setText(DateFormatUtils.formatTime(mp3time));

        seekBar.setMax(mp3time);

        seekBar.setEnabled(false);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaInstance.getInstance().seekTo(seekBar.getProgress());
            }
        });
        playControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlayControl();
            }
        });

        initMediaPlayer();
    }

    public void onClickPlayControl(){



        if (status == 2 && !playControl.isSelected()){
            MediaInstance.getInstance().resumeMediaPlay();
            return;
        }
        if (status == 1 && playControl.isSelected()){
            MediaInstance.getInstance().pauseMediaPlay();
            return;
        }
        if (status == 0){
            MediaInstance.getInstance().startMediaPlayAsync(playurl);
            return;
        }
        MediaInstance.getInstance().stopMediaPlay();
        MediaInstance.getInstance().startMediaPlayAsync(playurl);

    }


    public void initMediaPlayer(){

        MediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                MediaInstance.getInstance().startTimerTask();
                int duration = MediaInstance.getInstance().mediaPlayer.getDuration();
                seekBar.setMax(duration);
                seekBar.setEnabled(true);
                playControl.setSelected(true);
                status = 1;
            }

            @Override
            public void onPlay() {
                MediaInstance.getInstance().startTimerTask();
                int duration = MediaInstance.getInstance().mediaPlayer.getDuration();
                seekBar.setMax(duration);
                seekBar.setEnabled(true);
                playControl.setSelected(true);
                status = 1;
            }

            @Override
            public void onPause() {
                MediaInstance.getInstance().stopTimerTask();
                playControl.setSelected(false);
                seekBar.setEnabled(true);
                status = 2;
            }


            @Override
            public void onStop() {
                MediaInstance.getInstance().stopTimerTask();
                playControl.setSelected(false);
                status = 3;
            }

            @Override
            public void onOver() {
                MediaInstance.getInstance().stopTimerTask();
                playControl.setSelected(false);
                status = 3;
            }

            @Override
            public void onError() {
                MediaInstance.getInstance().stopTimerTask();
                playControl.setSelected(false);
                status = 3;
                ToastUtils.toast(context,"播放出错啦");
            }
        });

        MediaInstance.getInstance().setOnProgressLitsener(new MediaInstance.OnProgressLitsener() {
            @Override
            public void progress(int progress) {
                int format =MediaInstance.getFormat(progress);
                seekBar.setProgress(progress);
                curentTime.setText(DateFormatUtils.formatTime(format));
            }
        });
    }


    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.onRefreshCompleted();
            }
        }, 800);
    }


    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lrc_template_make, parent, false);
        return new SampleViewHolder(view);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateLyricsData event) {
        WorksData worksData = event.getWorksData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 1) {
            finish();
        }
    }

    @Override
    public void onSuccess(int id, String message) {
        cancelPd();
        ToastUtils.toast(this,message);
        finish();
    }

    @Override
    public void onError(int id, String error) {
        cancelPd();
        ToastUtils.toast(this,error);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            saveToDraft();
            return true;
        }

        if (item.getItemId() == R.id.menu_save){
            WorksData worksData = new WorksData();

            worksData.title = getFormatTitle();
            worksData.lyrics = getFormatLyrics();

            if (StringUtils.isBlank(worksData.title)){
                ToastUtils.toast(this,"请输入歌词名称");
                return true;
            }
            if (StringUtils.isBlank(worksData.lyrics)){
                ToastUtils.toast(this,"请输入歌词");
                return true;
            }

            SaveWordActivity.toSaveWordActivity(this,worksData);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveToDraft(){

        LyricsDraftBean bean = new LyricsDraftBean();

        bean.title = getFormatTitle();
        bean.content = getFormatLyrics();

        if (StringUtils.isBlank(bean.title) && StringUtils.isBlank(bean.content)){
            finish();
            return;
        }

        bean.createtime = String.valueOf(System.currentTimeMillis());

        new MaterialDialog.Builder(context)
                .content("是否保存到草稿箱？")
                .positiveText("保存")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        draftPresenter.saveDraft(bean);
                        showPd("正在保存中");
                    }
                }).negativeText("放弃")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveToDraft();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @return
     */
    private String getFormatTitle(){
        return mDataList.get(0).lrcWord;
    }

    /**
     *
     * @return
     */

    private String getFormatLyrics(){
        int size = mDataList.size();
        StringBuffer buffer = new StringBuffer();
        for (int i=1;i<size;i++){
            String item = mDataList.get(i).lrcWord;
            if (item == null) item = "";
            buffer.append(item);
            if (i<size-1 && StringUtils.isNotBlank(item) ){
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }


    class SampleViewHolder extends BaseViewHolder {

        TextView templateWord;
        EditText lrcWord;
        Space space;
        public SampleViewHolder(View itemView) {
            super(itemView);
            templateWord = (TextView) itemView.findViewById(R.id.lrc_template_temp);
            lrcWord = (EditText) itemView.findViewById(R.id.lrc_template_make);
            space = (Space) itemView.findViewById(R.id.space);
        }

        @Override
        public void onBindViewHolder(int position) {
            final TemplateLrcBean template = mDataList.get(position);

            if (position == mDataList.size()-1){
                space.setVisibility(View.VISIBLE);
            }else {
                space.setVisibility(View.GONE);
            }
            templateWord.setText(template.template);
            lrcWord.setText(template.lrcWord);
//            lrcWord.setHint(template.template);
            lrcWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    template.lrcWord = s.toString();
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaInstance.getInstance().destroy();
    }
}
