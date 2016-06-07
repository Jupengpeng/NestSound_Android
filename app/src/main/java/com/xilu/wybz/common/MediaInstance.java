package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.xilu.wybz.common.interfaces.IMediaPlayerListener;

import java.io.IOException;

/**
 * Created by June on 2015/9/16.
 */
public class MediaInstance {
    public static MediaInstance mInstance;
    public MediaPlayer mediaPlayer;
    public boolean isPlay = false;

    public boolean asynchronization = false;
    private IMediaPlayerListener iml;

    MediaInstance() {

    }

    public synchronized static MediaInstance getInstance() {
        if (mInstance == null)
            mInstance = new MediaInstance();
        return mInstance;
    }

    public void startMediaPlay(final String path) {
        if (!isPlay) {
            try {
                isPlay = true;
                if (mediaPlayer == null) {
                    creatMediaPlayer(path);
                }
                mediaPlayer.prepare();
                asynchronization = false;
            } catch (IOException e) {
                isPlay = false;
                e.printStackTrace();
            }
        }
    }


    public void startMediaPlayAsync(final String path) {

        MyThreadPool.getInstance().doTask(new Runnable() {
            @Override
            public void run() {

                if (!isPlay) {
                    try {
                        isPlay = true;
                        if (mediaPlayer == null) {
                            creatMediaPlayer(path);
                        }
                        mediaPlayer.prepareAsync();
                        asynchronization = true;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    public void stopMediaPlay() {
        if (mediaPlayer != null && isPlay) {
            isPlay = false;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
            if (iml != null) {
                iml.onStop();
            }
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer != null && isPlay) {
            isPlay = false;
            mediaPlayer.pause();
            if (iml != null) {
                iml.onPause();
            }
        }
    }


    public void resumeMediaPlay() {
        if (mediaPlayer != null && !isPlay) {
            isPlay = true;
            mediaPlayer.start();
            if (iml != null) {
                iml.onPlay();
            }
        }
    }

    public boolean isPlay() {
        if (mediaPlayer != null) {
            return isPlay;
        }
        return false;
    }

    public int getProgress() {
        if (mediaPlayer != null) {
            return (int) (mediaPlayer.getCurrentPosition() * 1.0f / mediaPlayer.getDuration() * 100);
        }
        return 0;
    }

    public void creatMediaPlayer(String file) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPlay = true;
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlay = false;
                    mp.reset();
                    mp.release();
                    mediaPlayer = null;
                    if (iml != null) {
                        iml.onOver();
                    }
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    isPlay = false;
                    mp.reset();
                    if (iml != null) {
                        iml.onError();
                    }
                    return false;
                }
            });
            try {
                mediaPlayer.setDataSource(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy(){
        try{
            mediaPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mInstance = null;
    }


    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }
}
