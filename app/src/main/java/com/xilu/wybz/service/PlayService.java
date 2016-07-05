package com.xilu.wybz.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
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
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 2015/9/14.
 */
public class PlayService extends Service implements AudioManager.OnAudioFocusChangeListener {
    WorksData currMdb;
    MusicBinder mBinder = new MusicBinder();
    int userId;
    int id;
    int position;
    String from;
    String gedanid;
    TelephonyManager tmgr;
    HttpUtils httpUtils;
    AudioManager mAudioManager;
    int status;//焦点是否拿到
    String TAG = "loadmusic";
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpUtils = new HttpUtils(this,TAG);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        requestAudioFocus();
        initPlayListener();
        initCallListener();
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

    public void initPlayListener() {
        PlayMediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                status = requestAudioFocus();
                startPlayback();
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_START));
            }

            @Override
            public void onStop() {
                stopPlayback();
                releaseAudioFocus();
            }

            @Override
            public void onPlay() {
                status = requestAudioFocus();
                startPlayback();
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_PLAY));
            }

            @Override
            public void onPause() {
                releaseAudioFocus();
                stopPlayback();
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_PAUSE));
            }

            @Override
            public void onOver() {
                releaseAudioFocus();
                startPlayback();
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_OVER));
                if (PrefsUtil.getInt("playmodel", PlayService.this) == MyCommon.PLAY_MODEL_LOOP || MyCommon.getFromMusicType(from) == 1) {
                    //单曲循环或者列表只有一首歌
                    PlayMediaInstance.getInstance().stopMediaPlay();
                    PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                } else {
                    switch (MyCommon.getFromMusicType(from)) {
                        case 2:
                            //下一首 从本地的ids里面取
                            position++;
                            if (position == MyApplication.ids.size()) {
                                position = 0;
                            }
                            if (MyApplication.ids.size() > 0)
                                loadData(MyApplication.ids.get(position));
                            break;
                    }
                }
            }
            @Override
            public void onError() {
                releaseAudioFocus();
                stopPlayback();
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_ERROR));
            }
        });
    }
    public void initCallListener() {
        tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void initData(int musicId) {
        if (musicId == 0) {
            return;
        }
        loadData(musicId);
    }

    public void loadData(int itemid) {
        PlayMediaInstance.getInstance().stopMediaPlay();
        Map<String, String> params = new HashMap<>();
        params.put("uid", userId + "");
        int openmodel = PrefsUtil.getInt("playmodel", PlayService.this);
        params.put("openmodel", (openmodel == 0 ? 1 : openmodel) + "");
        params.put("id", itemid + "");
        params.put("gedanid", gedanid);
        params.put("com", from);
        if (MyApplication.ids.size() > 1) {
            for (int i = 0; i < MyApplication.ids.size(); i++) {
                if (MyApplication.ids.get(i) == itemid) {
                    position = i;//遍历当前播放音乐的位置 用来切歌
                    break;
                }
            }
        } else {//默认
            position = 0;
        }
        httpUtils.cancelHttpByTag(TAG);
        httpUtils.get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_NO_DATA));
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                currMdb = ParseUtils.getWorkData(PlayService.this, response);
                if (currMdb != null && currMdb.itemid > 0) {
                    PrefsUtil.putInt("playId", currMdb.itemid, PlayService.this);
                    PrefsUtil.saveMusicData(PlayService.this, currMdb);
                    PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                    EventBus.getDefault().post(new Event.MusicDataEvent());
                    downLoadMp3(currMdb.playurl);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            id = intent.getIntExtra("id", 0);
            from = intent.getStringExtra("from");
            gedanid = intent.getStringExtra("gedanid");
            position = intent.getIntExtra("position", -1);
            PrefsUtil.putString("playFrom", from, this);
            PrefsUtil.putInt("playId", id, this);
            PrefsUtil.putString("playGedanId", gedanid, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id > 0) {
            userId = PrefsUtil.getUserId(this);
            initData(id);
        }
        return START_NOT_STICKY;
    }

    public class MusicBinder extends Binder {
        public WorksData getWorksData() {
            return currMdb;
        }

        public boolean getIsPlaying() {
            return PlayMediaInstance.getInstance().isPlaying();
        }

        public int getProgress(int allTimes) {
            return PlayMediaInstance.getInstance().getProgress(allTimes);
        }

        public int getCurrentPosition() {
            return PlayMediaInstance.getInstance().getCurrentPosition();
        }

        public void setCurrentPosition(int position) {
            PlayMediaInstance.getInstance().setCurrentPosition(position);
        }

        public int getDuration() {
            return PlayMediaInstance.getInstance().getDuration();
        }

        public void toPPMusic() {
            if (PlayMediaInstance.getInstance().status == 1) {
                PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
            } else if (PlayMediaInstance.getInstance().status == 2) {
                PlayMediaInstance.getInstance().resumeMediaPlay();
            } else if (PlayMediaInstance.getInstance().status == 3) {
                PlayMediaInstance.getInstance().pauseMediaPlay();
            }
        }

        public boolean isPlay() {
            return PlayMediaInstance.getInstance().isPlay();
        }

        public int getStatus() {
            return PlayMediaInstance.getInstance().status;
        }

        public void toPreMusic() {
            EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_OVER));
            switch (MyCommon.getFromMusicType(from)) {
                case 1://列表只有一首 无线循环
                    PlayMediaInstance.getInstance().stopMediaPlay();
                    PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                    break;
                case 2:
                    position--;
                    if (position == -1) {
                        position = MyApplication.ids.size() - 1;
                    }
                    if(position<0||position>=MyApplication.ids.size()){
                        PlayMediaInstance.getInstance().stopMediaPlay();
                        PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                    }else{
                        loadData(MyApplication.ids.get(position));
                    }
                    break;
            }
        }

        public void toNextMusic() {
            EventBus.getDefault().post(new Event.PPStatusEvent(MyCommon.PP_OVER));
            switch (MyCommon.getFromMusicType(from)) {
                case 1://列表只有一首 无线循环
                    PlayMediaInstance.getInstance().stopMediaPlay();
                    PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                    break;
                case 2:
                    position++;
                    if (position == MyApplication.ids.size()) {
                        position = 0;
                    }
                    if(position<0||position>=MyApplication.ids.size()){
                        PlayMediaInstance.getInstance().stopMediaPlay();
                        PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                    }else{
                        loadData(MyApplication.ids.get(position));
                    }
                    break;
            }
        }
        public void stopMusic() {
            PlayMediaInstance.getInstance().stopMediaPlay();
        }
    }

    @Override
    public void onDestroy() {
        tmgr.listen(mPhoneStateListener, 0);
        super.onDestroy();
    }

    boolean mResumeAfterCall = false;
    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int ringvolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_RING);
                if (ringvolume > 0) {
                    mResumeAfterCall = (PlayMediaInstance.getInstance().isPlay() || mResumeAfterCall);
                    try {
                        PlayMediaInstance.getInstance().pauseMediaPlay();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // pause the music while a conversation is in progress
                mResumeAfterCall = (PlayMediaInstance.getInstance().isPlay() || mResumeAfterCall);
                try {
                    PlayMediaInstance.getInstance().pauseMediaPlay();
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
                        PlayMediaInstance.getInstance().resumeMediaPlay();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mResumeAfterCall = false;
                }
            }
        }
    };

    public void downLoadMp3(String url) {
        if(ContextCompat.checkSelfPermission(PlayService.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            String fileName = MD5Util.getMD5String(url) + ".temp";
            String filePath = FileDir.songMp3Dir + fileName;
            File file = new File(filePath);
            if (file.exists()) return;
            if (!new File(FileDir.songMp3Dir).exists()) new File(FileDir.songMp3Dir).mkdirs();

            httpUtils.getFile(url, new FileCallBack(FileDir.songMp3Dir, fileName) {
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

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private void startPlayback() {
        registerReceiver(mNoisyAudioStreamReceiver, intentFilter);
    }

    private void stopPlayback() {
        try {
            unregisterReceiver(mNoisyAudioStreamReceiver);
        } catch (Exception e) {
        }
    }
}
