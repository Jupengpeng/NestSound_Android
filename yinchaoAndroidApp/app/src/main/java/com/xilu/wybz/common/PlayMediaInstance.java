package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Created by Zning on 2015/9/16.
 */
public class PlayMediaInstance {

    public static PlayMediaInstance mInstance;
    public int status = 1;//1未播放 2 暂停 3 播放

    public MediaPlayer mediaPlayer;
    private IMediaPlayerListener iml;

    private PlayMediaInstance() {

    }

    public static synchronized PlayMediaInstance getInstance() {
        if (mInstance == null) {
            mInstance = new PlayMediaInstance();
        }
        return mInstance;
    }

    public boolean isPlaying() {
        return status != 1;
    }

    public void startMediaPlay(String url) {
        if (mediaPlayer == null) {
            mediaPlayer = MyApplication.getMediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    status = 3;
                    Log.e("onPrepared", "onPrepared");
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (iml != null&&status>1) {
                        iml.onOver();
                    }
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.e("percent",percent+"");
                    EventBus.getDefault().post(new Event.UpdataSecondProgressEvent(percent));
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
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if(StringUtils.isNotBlank(url)){
                String fileName = MD5Util.getMD5String(url);
                String filePath = FileDir.songMp3Dir+fileName;
                File file = new File(filePath);
                if(file.exists())url = filePath;
            }
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            status = 1;
//            e.printStackTrace();
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

    public void release(){
        if (mediaPlayer != null) {
            status = 1;
            if(status>1)
                mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopMediaPlay() {
        if (mediaPlayer != null&&status>1) {
            status = 1;
            MyApplication.musicId = "";
            mediaPlayer.stop();
            mediaPlayer.release();
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
        if (!tag.equals(MyCommon.TYPE_DIYTYPE)) {
            MyApplication.musicId = id;
        }
        startMediaPlay(FileUtils.getMusicCachePath(tag + id));
    }
}
