package com.xilu.wybz.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.presenter.LoadMusicDetailPresenter;
import com.xilu.wybz.service.helper.HeadSetHelper;
import com.xilu.wybz.ui.IView.IMusicDetailView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by hujunwei on 16/6/29.
 */
public class MainService extends Service implements IMusicDetailView, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private LoadMusicDetailPresenter loadMusicDetailPresenter;
    private TelephonyManager mTelephonyManager;
    private AudioManager mAudioManager;
    private OnNoisyListener mNoisyListener;
    private WorksData mCurrentAudio;
    public MediaPlayer mPlayer;
    public int mCurrentState;
    private int playId;
    private String playFrom;
    private int playProgress;
    public static List<Integer> ids;
    private String playUrl;
    private int currentPos;
    boolean isPlay = true;
    private int playType;// 1单曲 2列表
    private int playModel;//循环模式
    //    private List<Integer> playIds;
    public static int status = 1;//1未播放 2 暂停 3 播放

    public int getCurrentPos() {
        if (ids == null) return currentPos;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == playId) {
                //遍历当前播放音乐的位置 用来切歌
                currentPos = i;
                break;
            }
        }
        return currentPos;
    }

//    public List<Integer> getplayIds() {
//        return playIds;
//    }
//
//    public void setplayIds(List<Integer> playIds) {
//        this.playIds = playIds;
//    }

    private boolean mResumeAfterCall;

    public static class CurrentMusic {
        public static final String
                PLAY_ID = "playId",
                PLAY_FROM = "playFrom",
                PLAY_GEDAN_ID = "playGedanId";
    }

    public static class ServiceBinder extends Binder {
        private MainService mService = null;

        public ServiceBinder(MainService service) {
            mService = service;
        }

        public MainService getService() {
            return mService;
        }
    }

    public int getCurrentPosition() {
        if (mPlayer != null) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    public void setCurrentPosition(int position) {
        playProgress = position;
        if (mPlayer != null && status != 1) {
            mPlayer.seekTo(position);
        }
    }

    public boolean isPlaying() {
        return mCurrentState == MyCommon.STARTED || mCurrentState == MyCommon.PLAYED;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    //播放歌曲 首次播放 或者 切入进来的 需要定位
    public void loadData(int playId, String playFrom, String PlayGedanId) {
        isPlay = true;
        this.playId = playId;
        this.playFrom = playFrom;
        playType = MyCommon.getFromMusicType(playFrom);
        getCurrentPos();
        PrefsUtil.putString(CurrentMusic.PLAY_FROM, playFrom, this);
        PrefsUtil.putInt(CurrentMusic.PLAY_ID, playId, this);
        PrefsUtil.putString(CurrentMusic.PLAY_GEDAN_ID, PlayGedanId, this);
        loadMusicDetailPresenter.loadMusicDetail(playId);
    }

    //列表里面切歌 position顺延
    public void loadNowListData(int playId) {
        isPlay = true;
        this.playId = playId;
        loadMusicDetailPresenter.loadMusicDetail(playId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadMusicDetailPresenter = new LoadMusicDetailPresenter(this, this);
        loadMusicDetailPresenter.init();
    }

    @Override
    public void initView() {
        ids = new ArrayList<>();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        requestAudioFocus();
        initCallListener();
        initHeadSetListener();
    }

    @Override
    public void showMusicDetail(WorksData worksData) {
        if (StringUtils.isNotBlank(worksData.playurl)) {
            mCurrentAudio = worksData;
            EventBus.getDefault().post(new Event.MusicDataEvent());
            PrefsUtil.saveMusicData(MainService.this, worksData);
            PrefsUtil.putInt(CurrentMusic.PLAY_ID, worksData.itemid, this);
            downLoadMp3(worksData.playurl);
            changeState(MyCommon.SUCCESSED);
            doStop();
            start();
        } else {
            ToastUtils.toast(MainService.this, "播放地址不存在！");
            changeState(MyCommon.FAILED);
        }
    }

    public void downLoadMp3(String url) {
        if (ContextCompat.checkSelfPermission(MainService.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            String fileName = MD5Util.getMD5String(url) + ".temp";
            String filePath = FileDir.songMp3Dir + fileName;
            File file = new File(filePath);
            if (file.exists()) return;
            if (!new File(FileDir.songMp3Dir).exists()) new File(FileDir.songMp3Dir).mkdirs();
            new HttpUtils(MainService.this).getFile(url, new FileCallBack(FileDir.songMp3Dir, fileName) {
                @Override
                public void inProgress(float progress, long total) {

                }

                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(File response) {
                    FileUtils.renameFile(FileDir.songMp3Dir + fileName, filePath);
                }
            });
        }
    }

    @Override
    public void loadFail() {
        changeState(MyCommon.FAILED);
    }

    //获取当前正在播放的音乐
    public WorksData getCurrentAudio() {
        return mCurrentAudio;
    }

    //这个方法用来初始化我们的MediaPlayer
    public void init() {
        if (mPlayer == null) {
            mPlayer = MyApplication.getMediaPlayer();
            changeState(MyCommon.IDLE);
        } else {
            if (mCurrentState == MyCommon.IDLE || mCurrentState == MyCommon.INITIALIZED || mCurrentState == MyCommon.PREPARED ||
                    mCurrentState == MyCommon.STARTED || mCurrentState == MyCommon.PAUSED || mCurrentState == MyCommon.STOPPED ||
                    mCurrentState == MyCommon.COMPLETED || mCurrentState == MyCommon.ERROR) {
                mPlayer.reset();
                changeState(MyCommon.IDLE);        //注意状态更改的代码
            }
        }
        mPlayer.setOnErrorListener(this);        //MainService 要实现这三个接口
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
    }

    //播放伴奏或者本地的音频文件 playUrl可以是网络或者本地的路径
    public void playOneMusic(String playUrl, String from) {
        if (StringUtils.isBlank(playUrl)) return;
        isPlay = true;
        playType = 1;//单曲的标识
        playFrom = from;//单曲的标识
        Log.e("playUrl", playUrl);
        playMusic(playUrl);
    }

    //播放指定url歌曲
    public void playMusic(String playUrl) {
        this.playUrl = playUrl;
        playProgress = 0;
        init();
        if (StringUtils.isBlank(playUrl)) {
            ToastUtils.toast(MainService.this, "播放路径不存在！");
            return;
        }
        try {
            if (mCurrentState == MyCommon.IDLE) {
                if (playUrl.startsWith("http")) {
                    String fileName = MD5Util.getMD5String(playUrl);
                    if (playFrom.endsWith("_hot")) {//伴奏
                        if (new File(FileDir.hotDir).exists()) {
                            String filePath = FileDir.hotDir + fileName;
                            File file = new File(filePath);
                            if (file.exists()) playUrl = filePath;
                        }
                    } else {
                        if (new File(FileDir.songMp3Dir).exists()) {
                            String filePath = FileDir.songMp3Dir + fileName;
                            File file = new File(filePath);
                            if (file.exists()) playUrl = filePath;
                        }
                    }
                }
                mPlayer.setDataSource(playUrl);    //Valid Sates IDLE
            }
            changeState(MyCommon.INITIALIZED);
            if (mCurrentState != MyCommon.ERROR) {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);   //Invalid States ERROR
            }
            if (mCurrentState == MyCommon.INITIALIZED || mCurrentState == MyCommon.STOPPED) {
                mPlayer.prepareAsync();//Valid Sates{Initialized, Stopped}
                changeState(MyCommon.PREPARING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //开始播放歌曲
    public void start() {
        if (mCurrentAudio == null) {
            playId = PrefsUtil.getInt(CurrentMusic.PLAY_ID, MainService.this);
            mCurrentAudio = PrefsUtil.getMusicData(MainService.this, playId);
        }
        if (mCurrentAudio != null)
            playMusic(mCurrentAudio.playurl);
    }

    //播放
    public void doStart() {
        if (mPlayer != null) {
            mPlayer.start();
            mPlayer.seekTo(playProgress);
            changeState(MyCommon.STARTED);
        }
    }

    //播放
    public void doPlay() {
        if (mPlayer != null) {
            mPlayer.start();
            changeState(MyCommon.PLAYED);
        }
    }

    //暂停
    public void doPause() {
        if (mPlayer != null) {
            mPlayer.pause();
            changeState(MyCommon.PAUSED);
        }
    }

    //停止
    public void doStop() {
        if (mPlayer != null) {
            mPlayer.stop();
            changeState(MyCommon.STOPPED);
        }
    }

    //播放暂停
    public void doPP(boolean isPlay) {
        this.isPlay = isPlay;
        if (status == 3) {
            doPause();
        } else if (status == 2) {
            doPlay();
        } else if (status == 1) {
            if (mCurrentState != MyCommon.PREPARING) {
                if (mCurrentState == MyCommon.PREPARED) {
                    if (isPlay) {
                        doStart();
                    }
                } else {
                    start();
                }
            }
        }
    }

    //释放
    public void doRelease() {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
            mCurrentAudio = null;
            status = 1;
            isPlay = true;
            changeState(MyCommon.END);
            mTelephonyManager.listen(mPhoneStateListener, 0);
        } catch (Exception e) {

        }
    }

    //改变播放状态
    public void changeState(int state) {
        mCurrentState = state;
        if (state == MyCommon.STARTED || state == MyCommon.PLAYED) {
            status = 3;
        } else if (state == MyCommon.PAUSED) {
            status = 2;
        } else {
            status = 1;
        }
        if (state != 0 && state != 1 && state != 2 && state != 3 && state != 9)
            if (state == MyCommon.STOPPED || state == MyCommon.COMPLETED
                    || state == MyCommon.FAILED
                    || state == MyCommon.ERROR) {
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.STOPPED, playFrom));
            } else {
                EventBus.getDefault().post(new Event.PPStatusEvent(state, playFrom));
            }

        if (state == MyCommon.PAUSED || state == MyCommon.STOPPED || state == MyCommon.COMPLETED
                || state == MyCommon.ERROR || state == MyCommon.IDLE || state == MyCommon.PREPARED) {
            stopNoisyListner();
            releaseAudioFocus();
        } else if (state == MyCommon.STARTED || state == MyCommon.PLAYED) {
            startNoisyListner();
            requestAudioFocus();
        }
        if (mNoisyListener != null) {
            mNoisyListener.onStateChanged(mCurrentAudio, mCurrentState);
        }
    }

    public void toPreMusic() {
        doStop();
        if (ids == null) {
            start();
        } else {
            if (currentPos == 0) {
                currentPos = ids.size() - 1;
            } else {
                currentPos--;
            }
            loadNowListData(ids.get(currentPos));
        }
    }

    public void toNextMusic() {
        doStop();
        if (ids == null) {
            start();
        } else {
            if (currentPos == ids.size() - 1) {
                currentPos = 0;
            } else {
                currentPos++;
            }
            loadNowListData(ids.get(currentPos));
        }
    }

    //播放结束
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCurrentState == MyCommon.ERROR) {//如果播放错误会走到这里 故需拦截
            return;
        }
        changeState(MyCommon.COMPLETED);
        //切歌
        playModel = PrefsUtil.getInt("playmodel", MainService.this);
        if (playType == 1 || playModel == MyCommon.PLAY_MODEL_LOOP) {
            doStop();
            playMusic(playUrl);//播放当前歌曲
        } else if (playType == 2) {
            toNextMusic();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changeState(MyCommon.ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        changeState(MyCommon.PREPARED);
        if (isPlay) {
            doStart();
        }
    }

    //线控监听
    private void initHeadSetListener() {
        HeadSetHelper.getInstance().setOnHeadSetListener(headSetListener);
        HeadSetHelper.getInstance().open(this);
    }

    HeadSetHelper.OnHeadSetListener headSetListener = new HeadSetHelper.OnHeadSetListener() {

        @Override
        public void onDoubleClick() {
            // TODO Auto-generated method stub
            if (playType == 2 && MyCommon.STARTED == mCurrentState) {
                toNextMusic();
            }
        }

        @Override
        public void onClick() {
            // TODO Auto-generated method stub
            doPP(isPlay);
        }
    };

    //打电话监听
    public void initCallListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int ringvolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_RING);
                if (ringvolume > 0) {
                    mResumeAfterCall = (mCurrentState == MyCommon.STARTED || mCurrentState == MyCommon.PLAYED || mResumeAfterCall);
                    try {
                        doPause();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // pause the music while a conversation is in progress
                mResumeAfterCall = (mCurrentState == MyCommon.STARTED || mCurrentState == MyCommon.PLAYED || mResumeAfterCall);
                try {
                    doPause();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                // start playing again
                if (mResumeAfterCall) {
                    // resume playback only if music was playing
                    // when the call was answered
                    try {
                        doPlay();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mResumeAfterCall = false;
                }
            }
        }
    };
    //耳机插拔监听
    public IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    public BroadcastReceiver mNoisyAudioStreamReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (PlayMediaInstance.getInstance().status == 3) {
                    PlayMediaInstance.getInstance().pauseMediaPlay();
                }
            }
        }
    };

    public void startNoisyListner() {
        registerReceiver(mNoisyAudioStreamReceiver, intentFilter);
    }

    public void stopNoisyListner() {
        try {
            unregisterReceiver(mNoisyAudioStreamReceiver);
        } catch (Exception e) {
        }
    }

    //以下是进行申请焦点的两个方法，
    public int requestAudioFocus() {
        return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public int releaseAudioFocus() {
        return mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS://播放长音频
                doStop();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_LOSS:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_GAIN://你已经得到了音频焦点
//                recoverVolume();
                doStart();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_GAIN:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://播放短音频
                doStop();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_LOSS_TRANSIENT:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://暂时失去AudioFocus,但是可以继续播放,不过要降低音量
//                lowerVolume();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:" + focusChange);
                break;
            default:
                Log.e("onAudioFocusChange", "default:" + focusChange);
                break;
        }
    }

    public void recoverVolume() {
        int curVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume,
                AudioManager.FLAG_PLAY_SOUND);
    }

    public void lowerVolume() {
        int curVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume / 10,
                AudioManager.FLAG_PLAY_SOUND);

    }

    /*这里采用了setOnPlaybackListener的方法，如果有需要，也可以用一个List去保存一个Listener集合，只要在适当的时候进行释放，例如在Service的onDestroy方法中，去把这个List清空掉*/
    public void setOnPlaybackListener(OnNoisyListener listener) {
        mNoisyListener = listener;
    }

    public static interface OnNoisyListener {
        public void onStateChanged(WorksData source, int state);
    }

    @Override
    public void onDestroy() {
        doRelease();
        super.onDestroy();
    }
}
