package com.xilu.wybz.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xilu.wybz.bean.MusicDetailBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by June on 2015/9/14.
 */
public class PlayService extends Service {
    WorksData currMdb;
    MusicBinder mBinder = new MusicBinder();
    int userId;
    int id;
    String from;
    String gedanid;
    int position;
    TelephonyManager tmgr;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayListener();
        initCallListener();
    }

    public void initPlayListener() {
        PlayMediaInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                EventBus.getDefault().post(new Event.PPStatusEvent(1));
            }

            @Override
            public void onStop() {
            }

            @Override
            public void onPlay() {
                EventBus.getDefault().post(new Event.PPStatusEvent(3));
            }

            @Override
            public void onPause() {
                EventBus.getDefault().post(new Event.PPStatusEvent(4));
            }

            @Override
            public void onOver() {
                EventBus.getDefault().post(new Event.PPStatusEvent(8));
                switch (MyCommon.getFromMusicType(from)) {
                    case 1://列表只有一首 无线循环
                        PlayMediaInstance.getInstance().stopMediaPlay();
                        PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                        break;
                    case 2:
                        if (PrefsUtil.getInt("playmodel", PlayService.this) == MyCommon.PLAY_MODEL_LOOP) {
                            PlayMediaInstance.getInstance().stopMediaPlay();
                            PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                        } else {
                            //下一首 从本地的ids里面取
                            position++;
                            if (position == MyApplication.ids.size()) {
                                position = 0;
                            }
                            if (MyApplication.ids.size() > 0)
                                loadData(MyApplication.ids.get(position));
                        }
                        break;
                    case 3:
                        if (PrefsUtil.getInt("playmodel", PlayService.this) == MyCommon.PLAY_MODEL_LOOP) {
                            PlayMediaInstance.getInstance().stopMediaPlay();
                            PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                        } else {
                            initNextData();
                        }
                        break;
                }
            }

            @Override
            public void onError() {
                EventBus.getDefault().post(new Event.PPStatusEvent(6));
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

    public void initPreData() {
        if (currMdb == null || currMdb.getPrev() == 0) {
            EventBus.getDefault().post(new Event.PPStatusEvent(9));
            return;
        }
        loadData(currMdb.getPrev());
    }

    public void initNextData() {
        if (currMdb == null || currMdb.getNext() == 0) {
            EventBus.getDefault().post(new Event.PPStatusEvent(10));
            return;
        }
        loadData(currMdb.getNext());
    }

    public void loadData(int itemid) {
        PlayMediaInstance.getInstance().stopMediaPlay();
        Map<String, String> params = new HashMap<>();
        params = new HashMap<>();
        params.put("uid", userId + "");
        int openmodel = PrefsUtil.getInt("playmodell", PlayService.this);
        params.put("openmodel", (openmodel == 0 ? 1 : openmodel) + "");
        params.put("id", itemid+"");
        params.put("gedanid", gedanid);
        params.put("com", from);
        new HttpUtils(PlayService.this).get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new Event.PPStatusEvent(7));
            }

            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    try {
                        String data = new JSONObject(response).getString("data");
                        currMdb = new Gson().fromJson(data, WorksData.class);
                        id = currMdb.getItemid();
                        PrefsUtil.putInt("playId", id, PlayService.this);
                        PrefsUtil.putString("playdata" + id, new Gson().toJson(currMdb), PlayService.this);
                        PlayMediaInstance.getInstance().startMediaPlay(currMdb.getPlayurl());
                        EventBus.getDefault().post(new Event.MusicDataEvent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            id = intent.getIntExtra("id",0);
            from = intent.getStringExtra("from");
            gedanid = intent.getStringExtra("gedanid");
            position = intent.getIntExtra("position", -1);
            PrefsUtil.putString("playFrom", from, this);
            PrefsUtil.putInt("playId", id, this);
            PrefsUtil.putInt("playPos", position, this);
            PrefsUtil.putString("playGedanId", gedanid, this);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }
        if (id>0) {
            userId = PrefsUtil.getUserId(this);
            initData(id);
        }
        return super.onStartCommand(intent, flags, startId);
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
            EventBus.getDefault().post(new Event.PPStatusEvent(8));
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
                    loadData(MyApplication.ids.get(position));
                    break;
                case 3:
                    initPreData();
                    break;
            }
        }

        public void toNextMusic() {
            EventBus.getDefault().post(new Event.PPStatusEvent(8));
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
                    loadData(MyApplication.ids.get(position));
                    break;
                case 3:
                    initNextData();
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
        PrefsUtil.putString("playId", "", this);
        PrefsUtil.putString("playFrom", "", this);
        PrefsUtil.putInt("playPos", -1, this);
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
}
