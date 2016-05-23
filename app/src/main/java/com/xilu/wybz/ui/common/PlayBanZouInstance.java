package com.xilu.wybz.ui.common;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.xilu.wybz.common.*;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.FileUtils;

import java.io.File;

/**
 * Created by Zning on 2015/9/16.
 */
public class PlayBanZouInstance {
    static PlayBanZouInstance mInstance;
    MediaPlayer mediaPlayer;
    IMediaPlayerListener iml;
    public int status = 1;//1未播放 2 暂停 3 播放

    PlayBanZouInstance() {

    }

    public static PlayBanZouInstance getInstance() {
        if (mInstance == null) {
            mInstance = new PlayBanZouInstance();
        }
        return mInstance;
    }

    public boolean isPlaying() {
        return status != 1;
    }

    public void startMediaPlay(String url) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    status = 3;
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (iml != null) {
                        iml.onOver();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    status = 1;
                    mp.reset();
                    if (iml != null) {
                        iml.onError();
                    }
                    return false;
                }
            });
        }
        try {
            mediaPlayer.reset();
            if (!TextUtils.isEmpty(url) && new File(url).exists()) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mediaPlayer.setDataSource(url);
            status = 3;
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            status = 1;
            e.printStackTrace();
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer != null && status == 3) {
            status = 2;
            mediaPlayer.pause();
            if (iml != null) {
                iml.onPause();
            }
        }
    }


    public void resumeMediaPlay() {
        if (mediaPlayer != null && status == 2) {
            status = 3;
            mediaPlayer.start();
            if (iml != null) {
                iml.onPlay();
            }
        }
    }


    public void stopMediaPlay() {
        if (mediaPlayer != null) {
            status = 1;
            MyApplication.musicId = "";
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
            if (iml != null) {
                iml.onStop();
            }
        }
    }

    public boolean isPlay() {
        if (mediaPlayer != null) {
            return status == 3;
        }
        return false;
    }

    public boolean isPause() {
        if (mediaPlayer != null) {
            return status == 2;
        }
        return false;
    }

    public int getProgress(int allTimes) {
        if (mediaPlayer != null) {
            return (int) (mediaPlayer.getCurrentPosition() * 1.0f / (allTimes * 1000) * 100);
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void setCurrentPosition(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }


    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }

    public void setData(String tag, String id) {
        if (id == null) {
            stopMediaPlay();
            return;
        }
        if (!tag.equals(com.xilu.wybz.common.MyCommon.TYPE_DIYTYPE)) {
            MyApplication.musicId = id;
        }
        startMediaPlay(FileUtils.getMusicCachePath(tag + id));
    }
}
