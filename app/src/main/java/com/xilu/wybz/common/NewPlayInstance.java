package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by June on 2016/5/4.
 */
public class NewPlayInstance {
    MediaPlayer mediaPlayer;
    IPlayerListener iml;
    static NewPlayInstance mInstance;
    int status = 1;
    int duration = 0;
    public static NewPlayInstance getInstance() {
        if (mInstance == null) {
            mInstance = new NewPlayInstance();
        }
        return mInstance;
    }

    public void startMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            status = 3;
        }
        if(iml!=null){
            iml.onMusicPlay();
        }
    }

    public void stopMediaPlay() {
        if (mediaPlayer != null) {
            status = 1;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
        }
        if(iml!=null){
            iml.onMusicStop();
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            status = 2;
        }
        if(iml!=null){
            iml.onMusicPause();
        }
    }
    public long getDuration(){
        return duration;
    }
    public void creatMediaPlayer(String file) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    duration = mediaPlayer.getDuration();
                    status = 3;
                    if (iml != null) {
                        iml.onMusicStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    status = 1;
                    mediaPlayer = null;
                    if (iml != null) {
                        iml.onMusicOver();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    status = 1;
                    mp.reset();
                    mp.release();
                    mediaPlayer = null;
                    if (iml != null) {
                        iml.onMusicError();
                    }
                    return false;
                }
            });
            try {
                mediaPlayer.setDataSource(file);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }
    public void release(){
        if (mediaPlayer != null) {
            if(status>1)
                mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public interface IPlayerListener{
        void onMusicStart();
        void onMusicPause();
        void onMusicPlay();
        void onMusicError();
        void onMusicStop();
        void onMusicOver();

    }
    public void setIMediaPlayerListener(IPlayerListener iml) {
        this.iml = iml;
    }
}
