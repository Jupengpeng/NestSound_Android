package com.xilu.wybz.ui.record;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.RecordImageAdapter;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.kpswitch.util.KPSwitchConflictUtil;
import com.xilu.wybz.view.kpswitch.util.KeyboardUtil;
import com.xilu.wybz.view.kpswitch.widget.KPSwitchPanelLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/11.
 */
public class PublishRecordActivity extends ToolbarActivity {

    @Bind(R.id.recycler_view_pic)
    RecyclerView recyclerViewPic;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.rl_add_voice)
    RelativeLayout rlAddVoice;
    @Bind(R.id.panel_root)
    KPSwitchPanelLinearLayout mPanelRoot;
    @Bind(R.id.tv_record_status)
    TextView tvRecordStatus;
    @Bind(R.id.iv_record_status)
    ImageView ivRecordStatus;
    @Bind(R.id.iv_del_record)
    ImageView ivDelRecord;
    private RecordImageAdapter adapter;
    private List<PhotoBean> list;
    private int recordStatus;//录音状态 0未开始 1进行中 2结束
    private RecordInstance recordInstance;
    Timer recordTimer, playTimer;
    long startRecordTime, startPlayTime, allTime;
    int recordIndex, playIndex;
    int playState; //1播放 2暂停
    String recordPath;//录音文件保存路径
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_publish_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setTitle("灵感记录");
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        int space10 = DensityUtil.dip2px(context, 10);
        recyclerViewPic.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerViewPic.addItemDecoration(new GridSpacingItemDecoration(4, space10, false));
        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
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
        recyclerViewPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                }

                return false;
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new RecordImageAdapter(context, list);
        recyclerViewPic.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecordImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onDelClick(View view, int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.rl_add_pic, R.id.iv_record_status})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_pic:
                Intent intent = new Intent(this, SelectPicActivity.class);
                ArrayList<String> pics = new ArrayList<>();
                if (list.size() > 0) {
                    for (PhotoBean photoBean : list) {
                        if (!photoBean.isAddPic) {
                            pics.add(photoBean.path);
                        }
                    }
                }
                intent.putStringArrayListExtra("pics", pics);
                startActivity(intent);
                break;
            case R.id.iv_record_status:
                switch (recordStatus){
                    case 0://开始录音
//                        RecordInstance.getInstance().setData(FileUtils.getMusicCachePath(MyCommon.TYPE_TEMPLATE + id), tag);
                        recordStatus = 1;
                        break;
                    case 1://录音中
                        recordStatus = 2;
                        ivDelRecord.setVisibility(View.VISIBLE);
                        break;
                    case 2://录音完成
                        switch (playState){
                            case 0://开始播放
                                ivRecordStatus.setImageResource(R.drawable.ic_record_pause);
                                break;
                            case 1://暂停
                                ivRecordStatus.setImageResource(R.drawable.ic_record_play);
                                playState=2;
                                break;
                            case 2://开始
                                ivRecordStatus.setImageResource(R.drawable.ic_record_pause);
                                playState=1;
                                break;
                        }
                        break;
                }
                break;
        }
    }

    void startRecordTimer() {
        startRecordTime = System.currentTimeMillis();
        if (recordTimer == null) {
            recordTimer = new Timer();
            recordTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, 1000, 1000);
        }
    }

    void stopRecordTimer() {
        if (recordTimer != null) {
            recordTimer.cancel();
            recordTimer = null;
        }
    }

    void startPlayTimer() {
        startPlayTime = System.currentTimeMillis();
        if (playTimer == null) {
            playTimer = new Timer();
            playTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            }, 1000, 1000);
        }
    }

    void stopPlayTimer() {
        if (playTimer != null) {
            playTimer.cancel();
            playTimer = null;
            playIndex = 1;
        }
    }

    public void onEventMainThread(Event.SelectPicEvent event) {
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //recordPb.setProgress(preProgress + (int) ((System.currentTimeMillis() - startRecordTime) * 1.0 / allTime * 100));
                    recordIndex++;
                    tvRecordStatus.setText(SystemUtils.getTimeFormat(recordIndex * 1000));
                    break;
                case 1:
                    playIndex++;
                    tvRecordStatus.setText(SystemUtils.getTimeFormat(playIndex * 1000)+"/"+allTime);
                    break;
            }
        }
    };
}