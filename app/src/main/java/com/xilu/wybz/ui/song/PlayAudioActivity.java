package com.xilu.wybz.ui.song;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.PlayLyricsAdapter;
import com.xilu.wybz.adapter.PlayPagerAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.ShareBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.PlayPresenter;
import com.xilu.wybz.service.PlayService;
import com.xilu.wybz.ui.ExitApplication;
import com.xilu.wybz.ui.IView.IPlayView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.ui.setting.SettingFeedActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.FormatHelper;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.dialog.ActionMoreDialog;
import com.xilu.wybz.view.dialog.ShareDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/4.
 */
public class PlayAudioActivity extends ToolbarActivity implements AdapterView.OnItemClickListener, IPlayView {
    @Bind(R.id.iv_reply)
    ImageView ivReply;
    @Bind(R.id.blurImageView)
    ImageView blurImageView;
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
    @Bind(R.id.tv_fav_num)
    TextView tvFavNum;
    @Bind(R.id.tv_zan_num)
    TextView tvZanNum;
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
    int authorid;
    String author;
    String headurl;
    String name;
    int id;
    PlayService.MusicBinder musicBinder;
    String from;
    String gedanid;
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
    String[] actionTitles2 = new String[]{"删除"};
    String[] actionTypes = new String[]{"homepage", "jubao"};
    String[] actionTypes2 = new String[]{"del"};
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

    public static void toPlayAudioActivity(Context context, int id, String gedanid, String from) {
        Intent intent = new Intent(context, PlayAudioActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("from", from);
        intent.putExtra("gedanid", gedanid);
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
        toolbar.setTitleTextColor(Color.WHITE);
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
                if (isSeek) {
                    isSeek = false;
                    musicBinder.setCurrentPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeek = musicBinder.getIsPlaying();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
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
            id = bundle.getInt("id");
            from = bundle.getString("from", "");
            gedanid = bundle.getString("gedanid", "");
            isCurrentMusic = (id == PrefsUtil.getInt("playId", context)) && from.equals(PrefsUtil.getString("playFrom", context));
        }
        actionBeanList = new ArrayList<>();
        viewPager.setAdapter(new PlayPagerAdapter(viewList));
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, PlayService.class);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        if (isCurrentMusic && PlayMediaInstance.getInstance().status != 1) {//正在播放的是本首
            worksData = PrefsUtil.getMusicData(context, id);
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
            worksData.status = 1;
            String lyrics = worksData.getLyrics();
            author = worksData.getAuthor();
            name = worksData.getTitle();
            setTitle(name);
            headurl = worksData.getHeadurl();
            authorid = worksData.getUid();
            actionBeanList.clear();
            if (authorid == PrefsUtil.getUserId(context)) {
                for (int i = 0; i < actionTitles2.length; i++) {
                    ActionBean actionBean = new ActionBean();
                    actionBean.setTitle(actionTitles2[i]);
                    actionBean.setType(actionTypes2[i]);
                    actionBeanList.add(actionBean);
                }
            } else {
                for (int i = 0; i < actionTitles.length; i++) {
                    ActionBean actionBean = new ActionBean();
                    actionBean.setTitle(actionTitles[i]);
                    actionBean.setType(actionTypes[i]);
                    actionBeanList.add(actionBean);
                }
            }
            hotid = worksData.getHotid();
            tvZan.setChecked(worksData.getIsZan() == 1);
            tvFav.setChecked(worksData.getIscollect() == 1);
            int times = Integer.valueOf(worksData.getMp3times());
            tvAlltime.setText(FormatHelper.formatDuration(times));
            toolbar.setSubtitle(author);
            if (!TextUtils.isEmpty(lyrics)) {
                Log.e("lyrics", lyrics);
                String[] lyricss = lyrics.split("\\n");
                lyricsList = Arrays.asList(lyricss);
            } else {
                lyricsList = new ArrayList<>();
            }
            updateFavNum();
            updateZanNum();
            updateCommentNum();
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
        File file = new File(FileDir.blurPic);
        if (!file.exists()) file.mkdirs();

        String path = FileDir.blurPic + MD5Util.getMD5String(worksData.pic);
        if (new File(path).exists()) {//加载本地
            blurImageView.setImageBitmap(BitmapUtils.getSDCardImg(path));
        } else {//下载并保存到本地
            imageUrl = MyCommon.getImageUrl(imageUrl, 200, 200);
            playPresenter.downLoadPic(imageUrl, path);
        }
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
                if (tvTime == null || playSeekBar == null) {
                    return;
                }
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
                if (worksData != null && worksData.itemid > 0) {
                    if (shareDialog == null) {
                        String shareTitle = worksData.title;
                        String shareAuthor = worksData.author;
                        String shareLink = worksData.shareurl + "?id=" + worksData.itemid;
                        String sharePic = worksData.pic;
                        String playurl = worksData.playurl;
                        String shareContent = "我在音巢APP淘到一首好听的歌，快来看看有没有你喜欢的原创style 《" + shareTitle + "》 ▷" + shareLink + " (@音巢音乐)";
                        shareDialog = new ShareDialog(PlayAudioActivity.this, new ShareBean(shareTitle, shareAuthor, shareContent, shareLink, sharePic, playurl));
                    }
                    if (!shareDialog.isShowing()) {
                        shareDialog.showDialog();
                    }
                }
                return true;
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
                if (actionBeanList != null && actionBeanList.size() > 0) {
                    if (actionMoreDialog == null) {
                        actionMoreDialog = new ActionMoreDialog(this, this, actionBeanList);
                    }
                    if (!actionMoreDialog.isShowing()) {
                        actionMoreDialog.showDialog();
                    }
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
                ivMode.setImageResource(mode == MyCommon.PLAY_MODEL_ORDER ? R.drawable.ic_play_loop : R.drawable.ic_play_order);
                PrefsUtil.putInt("playmodel", mode == MyCommon.PLAY_MODEL_ORDER ? MyCommon.PLAY_MODEL_LOOP : MyCommon.PLAY_MODEL_ORDER, context);
                break;
            case R.id.iv_hot:
                if (worksData.hotid > 0)
                    if (PrefsUtil.getUserId(context) > 0) {
                        toHotActivity();
                    } else {
                        ToastUtils.logingTip(context, "登录后才能录歌，确认要登录吗？");
                    }
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
        if (worksData.uid > 0) {
            UserInfoActivity.ToUserInfoActivity(context, worksData.uid, worksData.author);
        }
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
        CommentActivity.ToCommentActivity(context, worksData);
    }

    @Override
    public void collectionMusic() {
        rlFav.setEnabled(false);
        playPresenter.setCollectionState(worksData.itemid, worksData.uid);
    }

    @Override
    public void collectionMusicSuccess() {
        if (rlFav == null) return;
        rlFav.setEnabled(true);
        worksData.iscollect = 1 - worksData.iscollect;
        if (worksData.iscollect == 1) showMsg("收藏成功！");
        worksData.fovnum = worksData.fovnum+(worksData.iscollect==1?1:-1);
        EventBus.getDefault().post(new Event.UpdateWorkNum(worksData, 1));
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 3, 1 - worksData.iscollect));
        tvFav.setChecked(worksData.iscollect == 1);
        tvFav.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
        updateFavNum();
    }

    @Override
    public void collectionMusicFail() {
        if (rlFav == null){
            return;
        }
        rlFav.setEnabled(true);
    }

    @Override
    public void zambiaMusic() {
        rlZan.setEnabled(false);
        playPresenter.setZambiaState(id, worksData.uid);
    }
    @Override
    public void zambiaMusicSuccess() {
        if (rlZan == null) return;
        rlZan.setEnabled(true);
        worksData.isZan = 1 - worksData.isZan;
        if (worksData.isZan == 1) showMsg("点赞成功！");
        worksData.zannum = worksData.zannum+(worksData.isZan==1?1:-1);
        EventBus.getDefault().post(new Event.UpdateWorkNum(worksData, 2));
        tvZan.setChecked(worksData.isZan == 1);
        tvZan.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
        updateZanNum();
    }

    @Override
    public void zambiaMusicFail() {
        if (rlZan == null){
            return;
        }
        rlZan.setEnabled(true);
    }

    @Override
    public void setPic(Bitmap bitmap) {
        if (blurImageView != null)
            blurImageView.setImageBitmap(bitmap);
    }

    @Override
    public void deleteSuccess() {
        cancelPd();
        //关闭音乐
        PlayMediaInstance.getInstance().release();
        //通知上个页面移除
        EventBus.getDefault().post(new Event.RemoveMySongEvent(worksData.itemid));
        //清除本地数据
        PrefsUtil.clearMusicData(context, worksData.itemid);
        finish();
    }

    @Override
    public void deleteFail() {
        showMsg("删除失败！");
    }


    //更新评论数量
    public void onEventMainThread(Event.UpdataCommentNumEvent event) {
        if (event.getType() == 1) {
            worksData.commentnum = worksData.getCommentnum() + event.getNum();
            updateCommentNum();
        }

    }

    public void updateCommentNum() {
        tvCommentNum.setText(NumberUtil.format(worksData.commentnum));
        PrefsUtil.saveMusicData(context, worksData);
    }

    public void updateFavNum() {
        tvFavNum.setText(NumberUtil.format(worksData.fovnum));
        PrefsUtil.saveMusicData(context, worksData);
    }

    public void updateZanNum() {
        tvZanNum.setText(NumberUtil.format(worksData.zannum));

        PrefsUtil.saveMusicData(context, worksData);
    }





    //更新缓存进度
    public void onEventMainThread(Event.UpdataSecondProgressEvent event) {
//        if (event.getPercent() > 0) {
//            playSeekBar.setSecondaryProgress(event.getPercent());
//            if(event.getPercent()==100){
//            }
//        }
    }

    public void onEventMainThread(Event.PPStatusEvent event) {
        switch (event.getStatus()) {
            case MyCommon.PP_START://开始
                ivPlay.setEnabled(true);
                playSeekBar.setMax(musicBinder.getDuration());
                ivPlay.setImageResource(R.drawable.ic_play_pause);
                if (musicBinder != null)
                    tvAlltime.setText(FormatHelper.formatDuration(musicBinder.getDuration() / 1000));
                startTimer();
                break;
            case MyCommon.PP_STOP://停止
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                break;
            case MyCommon.PP_PLAY://播放
                startTimer();
                ivPlay.setImageResource(R.drawable.ic_play_pause);
                break;
            case MyCommon.PP_PAUSE://暂停
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                break;
            case MyCommon.PP_OVER://完成
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                tvTime.setText("00:00");
                break;
            case MyCommon.PP_ERROR://出错
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                showMsg("播放出错！");
                break;
            case MyCommon.PP_NO_DATA://获取数据失败
                closeTimer();
                ivPlay.setImageResource(R.drawable.ic_play_play);
                playSeekBar.setProgress(0);
                tvTime.setText("00:00");
                break;
            case MyCommon.PP_NO_PRE://
                showMsg("没有上一首");
            case MyCommon.PP_NO_NEXT://
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
        worksData.type = 1;
        worksData.status = 1;
        adapterData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        actionMoreDialog.dismiss();
        if (actionBeanList.size() > 1) {
            switch (position) {
                case 0://个人主页
                    toUserInfo();
                    break;
                case 1:
                    toJubao();
                    break;
            }
        } else if (actionBeanList.size() == 1) {
            showDeleteDialog();
        }
    }

    public void showDeleteDialog() {
        new MaterialDialog.Builder(context)
                .title(getString(R.string.dialog_title))
                .content("确认删除该作品吗?")
                .positiveText("删除")
                .positiveColor(getResources().getColor(R.color.red))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showPd("正在删除中...");
                        playPresenter.delete(worksData.itemid);
                    }
                }).negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        closeTimer();
        if (!PlayMediaInstance.getInstance().isPlaying()) {
            PlayMediaInstance.getInstance().release();
        }
        if (playPresenter != null)
            playPresenter.cancleRequest();
        PrefsUtil.saveMusicData(context, worksData);
        unbindService(serviceConnection);
        EventBus.getDefault().unregister(this);

        if (playPresenter != null) {
            playPresenter.cancelRequest();
        }
        super.onDestroy();
    }


}
