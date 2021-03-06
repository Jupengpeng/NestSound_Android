package com.xilu.wybz.ui.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.RecordImageAdapter;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.NewPlayInstance;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.presenter.InspireRecordPresenter;
import com.xilu.wybz.ui.IView.IInspireRecordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadFileUtil;
import com.xilu.wybz.utils.UploadMorePicUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.RoundProgressBar;
import com.xilu.wybz.view.SystemBarHelper;
import com.xilu.wybz.view.kpswitch.util.KPSwitchConflictUtil;
import com.xilu.wybz.view.kpswitch.util.KeyboardUtil;
import com.xilu.wybz.view.kpswitch.widget.KPSwitchPanelLinearLayout;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/11.
 */
public class ModifyInspireRecordActivity extends ToolbarActivity implements IInspireRecordView, NewPlayInstance.IPlayerListener {

    @Bind(R.id.recycler_view_pic)
    RecyclerView recyclerViewPic;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.rl_add_voice)
    RelativeLayout rlAddVoice;
    @Bind(R.id.iv_add_voice)
    ImageView ivAddVoice;
    @Bind(R.id.panel_root)
    KPSwitchPanelLinearLayout mPanelRoot;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_record_status)
    TextView tvRecordStatus;
    @Bind(R.id.iv_record_status)
    ImageView ivRecordStatus;
    @Bind(R.id.rl_loading)
    RelativeLayout rlLoading;
    @Bind(R.id.iv_play_progressbar)
    RoundProgressBar ivPlayProgressbar;
    @Bind(R.id.iv_del_record)
    ImageView ivDelRecord;

    @Bind(R.id.rl_volume_view)
    TextView rlVolumeView;
    @Bind(R.id.rl_volume_root)
    RelativeLayout rlVolumeRoot;

    private RecordImageAdapter adapter;
    private List<PhotoBean> list;
    private int recordStatus;//录音状态 0未开始 1进行中 2结束
    private Timer recordTimer, playTimer;
    private long allTime;
    private int recordIndex, playIndex;
    private int playState; //1播放 2暂停
    private String recordPath;//录音文件保存路径
    private MP3Recorder mp3Recorder;
    private WorksData worksData;
    int column = 3;
    int itemSpace;
    InspireRecordPresenter inspireRecordPresenter;
    protected MaterialDialog backDialog;
    List<Short> volumeData;

    public static void toModifyInspireRecordActivity(Context mContext, WorksData worksData) {
        Intent intent = new Intent(mContext, ModifyInspireRecordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        mContext.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_publish_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setStatusBarDarkMode(this);
        SystemBarHelper.setHeightAndPadding(this, mAppBar);

        EventBus.getDefault().register(this);
        inspireRecordPresenter = new InspireRecordPresenter(context, this);
        inspireRecordPresenter.init();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    private void uploadPics() {
        worksData.pics = "";
        UploadMorePicUtil uploadMorePicUtil = new UploadMorePicUtil(context);
        List<String> pics = new ArrayList<>();
        for (PhotoBean photoBean : list) {
            if (StringUtils.isNotBlank(photoBean.path) && new File(photoBean.path).exists())
                pics.add(photoBean.path);
            else {
                if (StringUtils.isNotBlank(photoBean.path))
                    worksData.pics += photoBean.path.replace(MyHttpClient.QINIU_URL, "") + ",";
            }
        }
        if (StringUtils.isNotBlank(worksData.pics) && worksData.pics.endsWith(",")) {
            worksData.pics = worksData.pics.substring(0, worksData.pics.length() - 1);
        }
        if (pics.size() == 0) {
            if (StringUtils.isNotBlank(recordPath) && new File(recordPath).exists()) {
                uploadRecord();
            } else {
                inspireRecordPresenter.updateData(worksData);
            }
            return;
        }
        uploadMorePicUtil.uploadPics(pics, new UploadMorePicUtil.UploadPicResult() {
            @Override
            public void onSuccess(String images) {
                if (!TextUtils.isEmpty(images)) {
                    if (StringUtils.isBlank(worksData.pics))
                        worksData.pics = images;
                    else
                        worksData.pics = worksData.pics + "," + images;
                    if (StringUtils.isNotBlank(recordPath) && new File(recordPath).exists()) {
                        uploadRecord();
                    } else {
                        inspireRecordPresenter.updateData(worksData);
                    }
                }
            }

            @Override
            public void onFail() {
                cancelPd();
                showMsg("图片上传失败！");
            }
        });
    }

    private void uploadRecord() {
        if (!new File(recordPath).exists()) {
            showMsg("本地录音文件不存在！");
        }
        UploadFileUtil uploadFileUtil = new UploadFileUtil(context);
        uploadFileUtil.uploadFile(recordPath, MyCommon.fixxs[3], new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String filrUrl) {
                if (!TextUtils.isEmpty(filrUrl)) {
                    worksData.audio = filrUrl;
                    inspireRecordPresenter.updateData(worksData);
                    PrefsUtil.putString(MyHttpClient.QINIU_AUDIO_URL+filrUrl,recordPath,context);
                }
            }

            @Override
            public void onFail() {
                cancelPd();
                showMsg("录音上传失败！");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send:
                String content = etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content) || list.size() > 0 || !TextUtils.isEmpty(recordPath)) {
                    showPd("正在修改中，请稍候...");
                    worksData.spirecontent = content;
                    if (list.size() > 0) {
                        uploadPics();
                    } else {
                        worksData.pics = "";
                        if (TextUtils.isEmpty(recordPath) || StringUtils.isNotBlank(worksData.audio)) {
                            inspireRecordPresenter.updateData(worksData);
                        } else {
                            uploadRecord();
                        }
                    }
                }
                return true;
            case android.R.id.home:
                onKeyBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        setTitle(DateTimeUtil.timestamp2DateTime(System.currentTimeMillis()));
        itemSpace = DensityUtil.dip2px(context, 10);
        recyclerViewPic.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewPic.addItemDecoration(new GridSpacingItemDecoration(column, itemSpace, false));
        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's setCurrentPageView/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d("onKeyboardShowing", String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });

        KPSwitchConflictUtil.attach(mPanelRoot, rlAddVoice, etContent,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(boolean switchToPanel) {
                        if (switchToPanel) {
                            etContent.clearFocus();
                        } else {
                            etContent.requestFocus();
                        }
                    }
                });

    }

    private void initData() {
        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        } else {
            finish();
        }
        //初始化文字
        if (StringUtils.isNotBlank(worksData.spirecontent)) {
            etContent.setText(worksData.spirecontent);
            etContent.setSelection(etContent.length());
        }
        //初始化图片
        if (StringUtils.isNotBlank(worksData.pics)) {
            String[] pics = worksData.pics.split(",");
            List<String> picss = Arrays.asList(pics);
            for (String str : picss) {
                PhotoBean photoBean = new PhotoBean();
                photoBean.path = str.startsWith(MyHttpClient.QINIU_URL) ? str.replace(MyHttpClient.QINIU_URL,"") : str;
                list.add(photoBean);
            }
            if (list.size() < 9) {
                PhotoBean photoBean = new PhotoBean();
                photoBean.isAddPic = true;
                list.add(photoBean);
            }
        }
        adapter = new RecordImageAdapter(context, list, column);
        recyclerViewPic.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecordImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list.get(position).isAddPic) {
                    ToSelectPicActivity();
                }
            }

            @Override
            public void onDelClick(View view, int position) {
                adapter.removeItem(position);
            }
        });
        //初始化语音
        if (StringUtils.isNotBlank(worksData.audio)) {
            recordStatus = 2;
            playState = 0;
            tvRecordStatus.setText("录音完成");
            ivAddVoice.setImageResource(R.drawable.ic_record_luyin_ed);
            tvTime.setText(SystemUtils.getTimeFormat(allTime));
            ivRecordStatus.setImageResource(R.drawable.ic_record_play);
            ivDelRecord.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rl_add_pic, R.id.iv_record_status, R.id.iv_del_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_del_record:
                if (StringUtils.isNotBlank(recordPath) && new File(recordPath).exists()) {
                    new File(recordPath).delete();
                }
                worksData.audio = "";
                stopPlayTimer();
                NewPlayInstance.getInstance().release();
                tvRecordStatus.setText("点击录音");
                tvTime.setText("");
                recordPath = "";
                ivPlayProgressbar.setVisibility(View.GONE);
                ivDelRecord.setVisibility(View.GONE);
                ivRecordStatus.setImageResource(R.drawable.ic_record_luyin_unstart);
                playState = 0;
                recordStatus = 0;
                recordIndex = 0;
                playIndex = 0;
                ivAddVoice.setImageResource(R.drawable.ic_record_luyin);
                break;
            case R.id.rl_add_pic:
                ToSelectPicActivity();
                break;
            case R.id.iv_record_status:
                switch (recordStatus) {
                    case 0://开始录音
                        if (!FileUtils.isSdcardExit()) {
                            showMsg("没有SD卡，无法存储录音数据");
                            return;
                        }
                        if (!new File(FileDir.inspireMp3Dir).exists()) {
                            new File(FileDir.inspireMp3Dir).mkdirs();
                        }

                        rlVolumeView.setText("0:00:00");
                        rlVolumeRoot.setVisibility(View.VISIBLE);

                        recordPath = FileDir.inspireMp3Dir + System.currentTimeMillis() + ".mp3";
                        mp3Recorder = new MP3Recorder(new File(recordPath));
                        mp3Recorder.setOnWaveChangeListener(new MP3Recorder.OnWaveChangeListener() {
                            @Override
                            public void onChange(List<Short> data) {
                                volumeData = data;
                                if (data.size()%5 == 4){
                                    mHandler.sendEmptyMessage(3);
                                }
                            }
                        });
                        try {
                            mp3Recorder.start();
                            ivRecordStatus.setImageResource(R.drawable.ic_record_luyin_start);
                            recordStatus = 1;
                            tvRecordStatus.setText("正在录音");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1://录音中
                        rlVolumeRoot.setVisibility(View.GONE);
                        recordStatus = 2;
                        tvRecordStatus.setText("录音完成");
                        ivAddVoice.setImageResource(R.drawable.ic_record_luyin_ed);
                        tvTime.setText(SystemUtils.getTimeFormat(allTime));
                        ivRecordStatus.setImageResource(R.drawable.ic_record_play);
                        ivDelRecord.setVisibility(View.VISIBLE);
                        try {
                            mp3Recorder.stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2://录音完成
                        switch (playState) {
                            case 0://开始播放
                                if (StringUtils.isNotBlank(recordPath) && new File(recordPath).exists()) {
                                    NewPlayInstance.getInstance().setIMediaPlayerListener(ModifyInspireRecordActivity.this);
                                    NewPlayInstance.getInstance().creatMediaPlayer(recordPath);
                                } else if (StringUtils.isNotBlank(worksData.audio)) {
                                    NewPlayInstance.getInstance().setIMediaPlayerListener(ModifyInspireRecordActivity.this);
                                    String localRecord = PrefsUtil.getString(worksData.audio,context);
                                    if(StringUtils.isNotBlank(localRecord) && new File(localRecord).exists()){
                                        NewPlayInstance.getInstance().creatMediaPlayer(localRecord);
                                    }else{
                                        NewPlayInstance.getInstance().creatMediaPlayer(worksData.audio.startsWith("http")?worksData.audio:MyHttpClient.QINIU_AUDIO_URL+worksData.audio);
                                    }
                                }
                                ivRecordStatus.setVisibility(View.INVISIBLE);
                                rlLoading.setVisibility(View.VISIBLE);
                                break;
                            case 1://暂停
                                NewPlayInstance.getInstance().pauseMediaPlay();
                                break;
                            case 2://开始
                                NewPlayInstance.getInstance().startMediaPlay();
                                break;
                        }
                        break;
                }
                break;
        }
    }


    int [] resource = {R.drawable.ic_record_volume0
            ,R.drawable.ic_record_volume1
            ,R.drawable.ic_record_volume2
            ,R.drawable.ic_record_volume3
            ,R.drawable.ic_record_volume4};

    private void showVolume(List<Short> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        short volume = data.get(data.size()-1);
        String time = DateFormatUtils.formatVolumeTime(data.size());

        allTime = data.size()*50;

        rlVolumeView.setText(time);

        int index = 0;
        if (volume>300){
            index = 1;
        }
        if (volume>700){
            index = 2;
        }
        if (volume>1500){
            index = 3;
        }
        if (volume>3000){
            index = 4;
        }
        rlVolumeView.setBackgroundResource(resource[index]);
    }


    void startPlayTimer() {
        if (playTimer == null) {
            playTimer = new Timer();
            playTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            }, 0, 200);
        }
    }

    void stopPlayTimer() {
        if (playTimer != null) {
            playTimer.cancel();
            playTimer = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.SelectPicEvent event) {
        list.clear();
        List<PhotoBean> pics = event.getPics();
        list.addAll(pics);
        if (pics.size() < 9) {
            PhotoBean photoBean = new PhotoBean();
            photoBean.isAddPic = true;
            list.add(photoBean);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public void onBackPressed() {
        onKeyBack();
    }
    public void onKeyBack(){
        try {
            if (backDialog == null) {
                backDialog = new MaterialDialog.Builder(this)
                        .title("请确认操作")
                        .content("是否放弃当前灵感记录？")
                        .negativeText("取消")
                        .positiveText("确认放弃")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .canceledOnTouchOutside(true).build();
            }
            backDialog.show();
        }catch (Exception e){
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        if(mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        stopPlayTimer();
        //没有发布就退出 默认不保存本地的音频文件
        if(StringUtils.isBlank(worksData.audio)){
            if(StringUtils.isNotBlank(recordPath)&&new File(recordPath).exists()){
                new File(recordPath).delete();
            }
        }
        EventBus.getDefault().unregister(this);
        if(inspireRecordPresenter!=null){
            inspireRecordPresenter.cancelRequest();
        }
        NewPlayInstance.getInstance().release();
        RecordInstance.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(recordStatus==1){
                showMsg("请先暂停录音后再继续返回");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    if(isDestroy)return;
//                    recordIndex++;
//                    tvTime.setText(SystemUtils.getTimeFormat(recordIndex * 1000));
//                    allTime = recordIndex * 1000;
                    break;
                case 1:
                    if(isDestroy)return;
                    playIndex++;
                    ivPlayProgressbar.setProgress(playIndex * 200);
                    tvTime.setText(SystemUtils.getTimeFormat(playIndex * 200) + "/" + SystemUtils.getTimeFormat(allTime));
                    break;
                case 3:
                    showVolume(volumeData);
                    break;
            }
        }
    };


    @Override
    public void onMusicStart() {
        startPlayTimer();
        playState = 1;
        ivRecordStatus.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.INVISIBLE);
        allTime = NewPlayInstance.getInstance().getDuration()+999;
        ivPlayProgressbar.setMax(allTime);
        ivRecordStatus.setImageResource(R.drawable.ic_record_pause);
    }

    @Override
    public void onMusicPause() {
        stopPlayTimer();
        playState = 2;
        ivRecordStatus.setImageResource(R.drawable.ic_record_play);
    }

    @Override
    public void onMusicPlay() {
        startPlayTimer();
        playState = 1;
        ivRecordStatus.setImageResource(R.drawable.ic_record_pause);
    }

    @Override
    public void onMusicError() {
        stopPlayTimer();
        playState = 0;
        ivPlayProgressbar.setVisibility(View.GONE);
        ivRecordStatus.setImageResource(R.drawable.ic_record_play);
    }

    @Override
    public void onMusicStop() {
        stopPlayTimer();
        ivPlayProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onMusicOver() {
        stopPlayTimer();
        playState = 0;
        playIndex = 0;
        ivPlayProgressbar.setVisibility(View.GONE);
        tvTime.setText(SystemUtils.getTimeFormat(allTime));
        ivRecordStatus.setImageResource(R.drawable.ic_record_play);
    }

    private void ToSelectPicActivity() {
        Intent intent = new Intent(this, NewSelectPicActivity.class);
        ArrayList<String> pics = new ArrayList<>();
        if (list.size() > 0) {
            for (PhotoBean photoBean : list) {
                if (!photoBean.isAddPic) {
                    pics.add(photoBean.path);
                }
            }
        }
        intent.putStringArrayListExtra("pics", pics);
        intent.putExtra("column", column);
        startActivity(intent);
    }

    @Override
    public void pubSuccess() {
        cancelPd();
        showMsg("修改成功！");
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 3, 2));
        finish();
    }

    @Override
    public void pubFail() {
        cancelPd();
    }

}