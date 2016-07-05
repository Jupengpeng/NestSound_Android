package com.xilu.wybz.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.LoadMusicDetailPresenter;
import com.xilu.wybz.ui.IView.IMusicDetailView;
import com.xilu.wybz.utils.PrefsUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;


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
    private MediaPlayer mPlayer;
    private int mCurrentState;
    private int PlayId;
    private String PlayFrom;
    private int currentPos;
    private List<Integer> playIds;

    public int getCurrentPos() {
        if (playIds == null) return currentPos;
        for (int i = 0; i < playIds.size(); i++) {
            if (playIds.get(i) == PlayId) {
                //遍历当前播放音乐的位置 用来切歌
                currentPos = i;
                break;
            }
        }
        return currentPos;
    }

    public List<Integer> getPlayIds() {
        return playIds;
    }

    public void setPlayIds(List<Integer> playIds) {
        this.playIds = playIds;
    }

    private boolean mResumeAfterCall;

    public static class CurrentMusic {
        public static final String
                PLAY_ID = "PLAY_ID",
                PLAY_FROM = "PLAY_FROM",
                PLAY_GEDAN_ID = "PLAY_GEDAN_ID";
    }

    public static class State {
        public static final int
                IDLE = 0,
                INITIALIZED = 1,
                PREPARED = 2,
                PREPARING = 3,
                STARTED = 4,
                PAUSED = 5,
                STOPPED = 6,
                COMPLETED = 7,
                SUCCESSED = 8,
                END = -1,
                ERROR = -2,
                FAILED = -3;
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

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    //播放歌曲 首次播放 或者 切入进来的 需要定位
    public void loadData(int PlayId, String PlayFrom, String PlayGedanId) {
        this.PlayFrom = PlayFrom;
        this.PlayId = PlayId;
        getCurrentPos();
        PrefsUtil.putString(CurrentMusic.PLAY_FROM, PlayFrom, this);
        PrefsUtil.putInt(CurrentMusic.PLAY_ID, PlayId, this);
        PrefsUtil.putString(CurrentMusic.PLAY_GEDAN_ID, PlayGedanId, this);
        loadMusicDetailPresenter.loadMusicDetail(PlayId);
    }

    //列表里面切歌 position顺延
    public void loadNowListData(int PlayId) {
        this.PlayId = PlayId;
        loadMusicDetailPresenter.loadMusicDetail(PlayId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadMusicDetailPresenter = new LoadMusicDetailPresenter(this, this);
        loadMusicDetailPresenter.init();
    }

    @Override
    public void showMusicDetail(WorksData worksData) {
        mCurrentAudio = worksData;
        changeState(State.SUCCESSED);
    }

    @Override
    public void loadFail() {
        changeState(State.FAILED);
    }

    @Override
    public void initView() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initCallListener();
    }

    //获取当前正在播放的音乐
    public WorksData getCurrentAudio() {
        return mCurrentAudio;
    }

    //这个方法用来初始化我们的MediaPlayer
    private void init() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            changeState(State.IDLE);
        } else {
            if (mCurrentState == State.IDLE || mCurrentState == State.INITIALIZED || mCurrentState == State.PREPARED ||
                    mCurrentState == State.STARTED || mCurrentState == State.PAUSED || mCurrentState == State.STOPPED ||
                    mCurrentState == State.COMPLETED || mCurrentState == State.ERROR) {
                mPlayer.reset();
                changeState(State.IDLE);        //注意状态更改的代码
            }
        }
        mPlayer.setOnErrorListener(this);        //MainService 要实现这三个接口
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
    }

    //开始播放歌曲
    public void start() {
        init();
        if (mCurrentAudio == null) return;
        try {
            if (mCurrentState == State.IDLE) {
                mPlayer.setDataSource(mCurrentAudio.getPlayurl());    //Valid Sates IDLE
            }
            changeState(State.INITIALIZED);
            if (mCurrentState != State.ERROR) {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);   //Invalid States ERROR
            }
            if (mCurrentState == State.INITIALIZED || mCurrentState == State.STOPPED) {
                mPlayer.prepareAsync();//Valid Sates{Initialized, Stopped}
                changeState(State.PREPARING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放
    private void doStart() {
        mPlayer.start();
        changeState(State.STARTED);
    }

    //暂停
    private void doPause() {
        mPlayer.pause();
        changeState(State.PAUSED);
    }

    //停止
    private void doStop() {
        mPlayer.stop();
        changeState(State.STOPPED);
    }

    //释放
    private void doRelease() {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
            mCurrentAudio = null;
            changeState(State.END);
            mTelephonyManager.listen(mPhoneStateListener, 0);
        } catch (Exception e) {

        }
    }

    //改变播放状态
    private void changeState(int state) {
        mCurrentState = state;
        if (state == State.PAUSED || state == State.STOPPED || state == State.COMPLETED
                || state == State.ERROR || state == State.IDLE || state == State.PREPARED) {
            stopNoisyListner();
            releaseAudioFocus();
        } else if (state == State.STARTED) {
            startNoisyListner();
            requestAudioFocus();
        }
        if (mNoisyListener != null) {
            mNoisyListener.onStateChanged(mCurrentAudio, mCurrentState);
        }
    }

    public void toPreMusic() {
        changeState(State.STOPPED);
        switch (MyCommon.getFromMusicType(PlayFrom)) {
            case 1://列表只有一首 无线循环
                doStop();
                start();
                break;
            case 2:
                if (playIds == null) {
                    doStop();
                    start();
                } else {
                    if (currentPos == 0) {
                        currentPos = playIds.size() - 1;
                    } else {
                        currentPos--;
                    }
                    loadNowListData(playIds.get(currentPos));
                }
                break;
        }
    }

    public void toNextMusic() {
        EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_OVER));
        switch (MyCommon.getFromMusicType(PlayFrom)) {
            case 1://列表只有一首 无线循环
                doStop();
                start();
                break;
            case 2:
                if (playIds == null) {
                    doStop();
                    start();
                } else {
                    if (currentPos == playIds.size() - 1) {
                        currentPos = 0;
                    } else {
                        currentPos++;
                    }
                    loadNowListData(playIds.get(currentPos));
                }
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCurrentState == State.ERROR) {//如果播放错误会走到这里 故需拦截
            return;
        }
        changeState(State.COMPLETED);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changeState(State.ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        changeState(State.PREPARED);
        doStart();
    }

    //打电话监听
    public void initCallListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int ringvolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_RING);
                if (ringvolume > 0) {
                    mResumeAfterCall = (mCurrentState == State.STARTED || mResumeAfterCall);
                    try {
                        doPause();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // pause the music while a conversation is in progress
                mResumeAfterCall = (mCurrentState == State.STARTED || mResumeAfterCall);
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
                        doStart();
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
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private BroadcastReceiver mNoisyAudioStreamReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (PlayMediaInstance.getInstance().status == 3) {
                    PlayMediaInstance.getInstance().pauseMediaPlay();
                }
            }
        }
    };

    private void startNoisyListner() {
        registerReceiver(mNoisyAudioStreamReceiver, intentFilter);
    }

    private void stopNoisyListner() {
        try {
            unregisterReceiver(mNoisyAudioStreamReceiver);
        } catch (Exception e) {
        }
    }

    //以下是进行申请焦点的两个方法，
    private int requestAudioFocus() {
        return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private int releaseAudioFocus() {
        return mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS://播放长音频
                PlayMediaInstance.getInstance().pauseMediaPlay();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_LOSS:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_GAIN://你已经得到了音频焦点
                recoverVolume();
                PlayMediaInstance.getInstance().resumeMediaPlay();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_GAIN:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://播放短音频
                PlayMediaInstance.getInstance().pauseMediaPlay();
                Log.e("onAudioFocusChange", "AUDIOFOCUS_LOSS_TRANSIENT:" + focusChange);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://暂时失去AudioFocus,但是可以继续播放,不过要降低音量
                lowerVolume();
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
