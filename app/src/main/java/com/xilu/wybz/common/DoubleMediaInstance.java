package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.ui.MyApplication;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Zning on 2015/9/16.
 */
public class DoubleMediaInstance {

    public static DoubleMediaInstance mInstance;

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;

    private IMediaPlayerListener iml;

    DoubleMediaInstance() {

    }

    public synchronized static DoubleMediaInstance getInstance() {
        if (mInstance == null)
            mInstance = new DoubleMediaInstance();
        return mInstance;
    }

    public void startMediaPlay(String url1, String url2) {
        if (!MyApplication.isPlay) {
            try {
                MyApplication.isPlay = true;
                if (mediaPlayer1 == null) {
                    creatMediaPlayer1(url1);
                }
                if (mediaPlayer2 == null) {
                    creatMediaPlayer2(url2);
                }
                mediaPlayer1.prepare();
                mediaPlayer2.prepare();
            } catch (IOException e) {
                MyApplication.isPlay = false;
                e.printStackTrace();
            }
        }
    }

    public void stopMediaPlay() {
        if (mediaPlayer1 != null && mediaPlayer2 != null && MyApplication.isPlay) {
            MyApplication.isPlay = false;
            mediaPlayer1.stop();
            mediaPlayer1.reset();
            mediaPlayer1 = null;
            mediaPlayer2.stop();
            mediaPlayer2.reset();
            mediaPlayer2 = null;
            if (iml != null) {
                iml.onStop();
            }
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer1 != null && mediaPlayer2 != null && MyApplication.isPlay) {
            MyApplication.isPlay = false;
            mediaPlayer1.pause();
            mediaPlayer2.pause();
            if (iml != null) {
                iml.onPause();
            }
        }
    }


    public void resumeMediaPlay() {
        if (mediaPlayer1 != null && mediaPlayer2 != null && !MyApplication.isPlay) {
            MyApplication.isPlay = true;
            mediaPlayer1.start();
            mediaPlayer2.start();
            if (iml != null) {
                iml.onStart();
            }
        }
    }


    public boolean isPlay() {
        if (mediaPlayer1 != null && mediaPlayer2 != null) {
            return MyApplication.isPlay;
        }
        return false;
    }

    public int getProgress() {
        if (mediaPlayer1 != null) {
            return (int) (mediaPlayer1.getCurrentPosition() * 1.0f / mediaPlayer1.getDuration() * 100);
        }
        return 0;
    }

    public void creatMediaPlayer1(String file) {
        if (mediaPlayer1 == null) {
            mediaPlayer1 = new MediaPlayer();
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    MyApplication.isPlay = true;
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });

            mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    MyApplication.isPlay = false;
                    stopTimerTask();
                    releaseMp();
                    if (iml != null) {
                        iml.onOver();
                    }
                }
            });
            mediaPlayer1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    MyApplication.isPlay = false;
                    releaseMp();
                    if (iml != null) {
                        iml.onError();
                    }
                    return false;
                }
            });
            try {
                mediaPlayer1.setDataSource(file);
                Log.i("hehe", "mediaPlayer>>prepare");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void creatMediaPlayer2(String file) {
        if (mediaPlayer2 == null) {
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            try {
                mediaPlayer2.setDataSource(file);
                Log.i("hehe", "mediaPlayer>>prepare");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void releaseMp() {
        mediaPlayer1.reset();
        mediaPlayer1.release();
        mediaPlayer1 = null;
        mediaPlayer2.reset();
        mediaPlayer2.release();
        mediaPlayer2 = null;
    }

    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }




    Timer timer;
    OnProgressLitsener onProgressLitsener;




    /**
     *
     */
    public void startTimerTask(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer1 != null){
                    int current = mediaPlayer1.getCurrentPosition();
                    Log.d("timer","time:"+current);
                    if (onProgressLitsener != null){
                        onProgressLitsener.progress(current);
                    }
                } else {
                    Log.d("timer","cancel");
                    timer.cancel();
                    timer = null;
                }
            }
        };

        timer.schedule(task,50,50);

    }


    /**
     *
     */
    public void stopTimerTask(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public void setOnProgressLitsener(OnProgressLitsener onProgressLitsener) {
        this.onProgressLitsener = onProgressLitsener;
    }

    public interface OnProgressLitsener{
        void progress(int progress);
    }


    public void destroy(){

        if (isPlay()){
            mediaPlayer1.stop();
            mediaPlayer2.stop();
        }
        try{
            mediaPlayer1.reset();
            mediaPlayer1.release();
            mediaPlayer2.reset();
            mediaPlayer2.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mInstance = null;
    }


}
