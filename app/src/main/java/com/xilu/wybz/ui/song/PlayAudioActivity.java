package com.xilu.wybz.ui.song;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.UserInfo;
import com.umeng.socialize.UMShareAPI;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.PlayLyricsAdapter;
import com.xilu.wybz.adapter.PlayPagerAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.presenter.PlayPresenter;
import com.xilu.wybz.service.PlayService;
import com.xilu.wybz.ui.ExitApplication;
import com.xilu.wybz.ui.IView.IPlayView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.ui.setting.SettingFeedActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FormatHelper;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.dialog.ActionMoreDialog;
import com.xilu.wybz.view.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class PlayAudioActivity extends ToolbarActivity implements AdapterView.OnItemClickListener, IPlayView {
    @Bind(R.id.blurImageView)
    ImageView blurImageView;
    @Bind(R.id.status_bar_view)
    View statusBarView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.iv_point1)
    ImageView ivPoint1;
    @Bind(R.id.iv_point2)
    ImageView ivPoint2;
    @Bind(R.id.tv_zan)
    CheckedTextView tvZan;
    @Bind(R.id.tv_fav)
    CheckedTextView tvFav;
    @Bind(R.id.tv_comment_num)
    TextView tvCommentNum;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.playSeekBar)
    SeekBar playSeekBar;
    @Bind(R.id.tv_alltime)
    TextView tvAlltime;
    @Bind(R.id.iv_mode)
    ImageView ivMode;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.rl_fav)
    RelativeLayout rlFav;
    @Bind(R.id.rl_zan)
    RelativeLayout rlZan;
    TextView tvHotName;
    TextView tvDetail;
    List<View> viewList;//把需要滑动的页卡添加到这个li
    ListView listview_lyrics;
    ScrollView sv_content;
    String authorid;
    String author;
    String headurl;
    int is_fov;
    int is_zan;
    String name;
    String id;
    PlayService.MusicBinder musicBinder;
    String from;
    String gedanid;
    int position;
    ActionMoreDialog actionMoreDialog;
    boolean isCurrentMusic;
    boolean isSeek;
    int hotid;//伴奏ID
    Intent intent;
    Intent serviceIntent;
    Timer timer;
    WorksData worksData;
    ShareDialog shareDialog;
    PlayPresenter playPresenter;
    List<ActionBean> actionBeanList;
    String[] actionTitles = new String[]{"个人主页", "举报"};
    String[] actionTypes = new String[]{"homepage", "jubao"};
    PlayLyricsAdapter playLyricsAdapter;
    List<String> lyricsList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_playaudio;
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (PlayService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static void toPlayAudioActivity(Context context, String id, String gedanid, String from, int position) {
        Intent intent = new Intent(context, PlayAudioActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("from", from);
        intent.putExtra("gedanid", gedanid);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().exit();
        EventBus.getDefault().register(this);
        playPresenter = new PlayPresenter(this, this);
        playPresenter.init();
        initEvent();
        initData();
    }

    public void initView() {
        ExitApplication.getInstance().addActivity(this);
        if (!isChenjin) { //没有沉浸的时候 导航栏不需要上边距
            statusBarView.setVisibility(View.GONE);
        } else {
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.getStatusBarHeight(context)));
        }
        actionBeanList = new ArrayList<>();
        for (int i = 0; i < actionTitles.length; i++) {
            ActionBean actionBean = new ActionBean();
            actionBean.setTitle(actionTitles[i]);
            actionBean.setType(actionTypes[i]);
            actionBeanList.add(actionBean);
        }
        toolbar.setTitleTextColor(0xFFFFFFFF);
        worksData = new WorksData();
        LayoutInflater lf = getLayoutInflater().from(this);
        View view1 = lf.inflate(R.layout.ll_lyrics, null);
        listview_lyrics = (ListView) view1.findViewById(R.id.listview_lyrics);
        sv_content = (ScrollView) view1.findViewById(R.id.sv_content);
        View view2 = lf.inflate(R.layout.ll_music_detail, null);
        tvHotName = (TextView) view2.findViewById(R.id.tv_hot_name);
        tvDetail = (TextView) view2.findViewById(R.id.tv_detail);
        viewList = new ArrayList<>();
        // 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        int mode = PrefsUtil.getInt("playmodel", context);
        ivMode.setImageResource(mode == MyCommon.PLAY_MODEL_LOOP ? R.drawable.ic_play_loop : R.drawable.ic_play_order);
    }

    public void initEvent() {
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (isSeek) {
                    isSeek = false;
                    musicBinder.setCurrentPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                isSeek = musicBinder.getIsPlaying();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (isSeek) {
                    this.progress = progress;
                    tvTime.setText(FormatHelper.formatDuration(progress / 1000));
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ivPoint1.setImageResource(R.drawable.ic_play_point1);
                    ivPoint2.setImageResource(R.drawable.ic_play_point2);
                } else {
                    ivPoint1.setImageResource(R.drawable.ic_play_point2);
                    ivPoint2.setImageResource(R.drawable.ic_play_point1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            from = bundle.getString("from", "");
            gedanid = bundle.getString("gedanid", "");
            authorid = bundle.getString("authorid");
            position = bundle.getInt("position");
            isCurrentMusic = id.equals(PrefsUtil.getString("playId", context))
                    && from.equals(PrefsUtil.getString("playFrom", context));
        }
        viewPager.setAdapter(new PlayPagerAdapter(viewList));
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, PlayService.class);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        if (isCurrentMusic && PlayMediaInstance.getInstance().status != 1) {//正在播放的是本首
            String localData = PrefsUtil.getString("playdata" + id, context);
            worksData = new Gson().fromJson(localData, WorksData.class);
            adapterData();
            playSeekBar.setMax(PlayMediaInstance.getInstance().getDuration());
            playSeekBar.setProgress(PlayMediaInstance.getInstance().getCurrentPosition());
            tvTime.setText(FormatHelper.formatDuration(PlayMediaInstance.getInstance().getCurrentPosition() / 1000));
            if (PlayMediaInstance.getInstance().status == 2) {
                ivPlay.setImageResource(R.drawable.ic_play_play);
            } else {
                ivPlay.setImageResource(R.drawable.ic_play_pause);
                startTimer();
            }
        } else {//开启服务
            ivPlay.setEnabled(false);
            serviceIntent.putExtra("id", id);
            serviceIntent.putExtra("from", from);
            serviceIntent.putExtra("gedanid", gedanid);
            serviceIntent.putExtra("position", position);
            startService(serviceIntent);
        }
    }

    public void adapterData() {
        sv_content.fullScroll(ScrollView.FOCUS_UP);
        if (from.equals("info_comment")) viewPager.setCurrentItem(1);
        else viewPager.setCurrentItem(0);
        try {
            String pic = worksData.getPic();
            if (!TextUtils.isEmpty(pic)) {
                loadPic(pic);
            }
            String lyrics = worksData.getLyrics();
            author = worksData.getAuthor();
            name = worksData.getTitle();
            setTitle(name);
            headurl = worksData.getHeadurl();
            authorid = worksData.getUid() + "";
            hotid = worksData.getHotid();
            is_zan = worksData.getIsZan();
            is_fov = worksData.getIscollect();
            tvZan.setChecked(is_zan == 1);
            tvFav.setChecked(is_fov == 1);
            int times = Integer.valueOf(worksData.getMp3times());
            tvAlltime.setText(FormatHelper.formatDuration(times));
            toolbar.setSubtitle(author);
            if (!TextUtils.isEmpty(lyrics)) {
                Log.e("lyrics", lyrics);
                String[] lyricss = lyrics.split("\\n");
                lyricsList = Arrays.asList(lyricss);
            }
            if (worksData.commentnum > 0)
                tvCommentNum.setText(worksData.commentnum + "");
            playLyricsAdapter = new PlayLyricsAdapter(context, lyricsList);
            tvHotName.setText(worksData.getHotTitle());
            tvDetail.setText(worksData.getDetail());
            listview_lyrics.setAdapter(playLyricsAdapter);
            listview_lyrics.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    public void loadPic(String imageUrl) {
        imageUrl = MyCommon.getImageUrl(imageUrl,200,200);
        HttpUtils httpUtils = new HttpUtils(context);
        httpUtils.getImage(imageUrl, new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }
            @Override
            public void onResponse(Bitmap response) {
                Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(response, 200), 30);
                blurImageView.setImageBitmap(bmp);
            }
        });
    }

    //关闭时间
    public void closeTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //开始时间
    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (musicBinder == null) {
                            return;
                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (worksData != null) {
                    tvTime.setText(FormatHelper.formatDuration(musicBinder.getCurrentPosition() / 1000));//播放的时间变化
                    playSeekBar.setProgress(musicBinder.getCurrentPosition());//进度条对时间
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                if (worksData != null && !TextUtils.isEmpty(worksData.itemid)) {
                    if (shareDialog == null) {
                        String shareTitle = worksData.title;
                        String shareAuthor = worksData.author;
                        String shareLink = worksData.shareurl;
                        String sharePic = worksData.pic;
                        String playurl = worksData.playurl;
                        String shareContent = "我在音巢APP淘到一首好听的歌，快来看看有没有你喜欢的原创style 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
                        shareDialog = new ShareDialog(PlayAudioActivity.this, new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playurl));
                    }
                    if (!shareDialog.isShowing()) {
                        shareDialog.showDialog();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_mode, R.id.rl_zan, R.id.rl_reply, R.id.rl_more, R.id.iv_pre, R.id.iv_play, R.id.iv_next, R.id.iv_hot, R.id.rl_fav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_fav:
                if (!SystemUtils.isLogin(context)) {
                    return;
                }
                collectionMusic();
                break;

            case R.id.rl_zan:
                if (!SystemUtils.isLogin(context)) {
                    return;
                }
                zambiaMusic();
                break;
            case R.id.rl_reply:
                toCommentActivity();
                break;
            case R.id.rl_more:
                if (actionMoreDialog == null) {
                    actionMoreDialog = new ActionMoreDialog(this, this, actionBeanList);
                }
                if (!actionMoreDialog.isShowing()) {
                    actionMoreDialog.showDialog();
                }
                break;
            case R.id.iv_pre:
                musicBinder.toPreMusic();
                break;
            case R.id.iv_play:
                musicBinder.toPPMusic();
                break;
            case R.id.iv_next:
                musicBinder.toNextMusic();
                break;
            case R.id.iv_mode:
                int mode = PrefsUtil.getInt("playmodel", context);
                ivMode.setImageResource(mode == MyCommon.PLAY_MODEL_ORDER ? R.drawable.ic_play_order : R.drawable.ic_play_loop);
                PrefsUtil.putInt("playmodel", mode == MyCommon.PLAY_MODEL_ORDER ? MyCommon.PLAY_MODEL_LOOP : MyCommon.PLAY_MODEL_ORDER, context);
                break;
            case R.id.iv_hot:
                if (worksData.hotid > 0)
                    toHotActivity();
                else
                    showMsg("该伴奏不存在！");
                break;
        }
    }

    public void toJubao() {
        intent = new Intent(context, SettingFeedActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void toUserInfo() {
        intent = new Intent(context, UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void toHotActivity() {
        TemplateBean templateBean = new TemplateBean();
        templateBean.id = worksData.hotid + "";
        templateBean.author = worksData.hotAuthor;
        templateBean.mp3 = worksData.hotmp3;
        templateBean.mp3times = worksData.hotmp3times;
        templateBean.title = worksData.hotTitle;
        MakeSongActivity.ToMakeSongActivity(context, templateBean);
    }

    @Override
    public void toCommentActivity() {

    }

    @Override
    public void collectionMusic() {
        rlFav.setEnabled(false);
        playPresenter.setCollectionState(userId, id, is_fov);
    }

    @Override
    public void collectionMusicSuccess(String result) {
        rlFav.setEnabled(true);
        if (ParseUtils.checkCode(result)) {
            is_fov = 1 - is_fov;
            if (is_fov == 1) showMsg("收藏成功！");
            tvFav.setChecked(is_fov == 1);
            tvFav.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
            worksData.setIscollect(is_fov);
            worksData.setFovnum(is_fov == 1 ? worksData.getFovnum() + 1 : worksData.getFovnum() - 1);
            PrefsUtil.putString("playdata" + id, new Gson().toJson(worksData), context);
        }
    }

    @Override
    public void collectionMusicFail(String msg) {
        rlFav.setEnabled(true);
    }

    @Override
    public void zambiaMusic() {
        rlZan.setEnabled(false);
        playPresenter.setZambiaState(userId, id, worksData.uid);
    }

    @Override
    public void zambiaMusicSuccess(String result) {
        rlZan.setEnabled(true);
        if (ParseUtils.checkCode(result)) {
            is_zan = 1 - is_zan;
            if (is_zan == 1) showMsg("点赞成功！");
            tvZan.setChecked(is_zan == 1);
            tvZan.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
            worksData.setIsZan(is_zan);
            worksData.setZannum(is_zan == 1 ? worksData.getZannum() + 1 : worksData.getZannum() - 1);
            saveMusicBean();
        }
    }

    @Override
    public void zambiaMusicFail() {
        rlZan.setEnabled(true);
    }

    public void saveMusicBean() {
        PrefsUtil.putString("playdata" + id, new Gson().toJson(worksData), context);
    }

    public void onEventMainThread(Event.PPStatusEvent event) {
        switch (event.getStatus()) {
            case 1://开始
                ivPlay.setEnabled(true);
                playSeekBar.setMax(musicBinder.getDuration());
                ivPlay.setImageResource(R.drawable.ic_play_pause);
                startTimer();
                break;
            case 2://停止
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                break;
            case 3://播放
                startTimer();
                ivPlay.setImageResource(R.drawable.ic_play_pause);
                break;
            case 4://暂停
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                break;
            case 5://完成
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                tvTime.setText("00:00");
                break;
            case 6://出错
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                break;
            case 7://网络错误
                showNetErrorMsg();
                break;
            case 8://网络错误
                closeTimer();
                playSeekBar.setProgress(0);
                tvTime.setText("00:00");
                break;
            case 9://
                showMsg("没有上一首");
            case 10://
                showMsg("没有下一首");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(Event.MusicDataEvent event) {
        worksData = musicBinder.getWorksData();
        adapterData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTimer();
        unbindService(serviceConnection);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0://个人主页
                toUserInfo();
                break;
            case 1:
                toJubao();
                break;
        }
    }
}
