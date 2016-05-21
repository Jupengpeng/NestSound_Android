package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceView;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by June on 2016/5/4.
 */
public class NewRecordInstance {
    public MP3Recorder mp3Recorder;

    MediaPlayer mediaPlayer;
    boolean isStart = false;// 设置正在录制的状态
    IMediaPlayerListener iml;
    String tag;
    SurfaceView sfv;
    //NewAudioName可播放的音频文件
    String NewAudioName = "";

    static NewRecordInstance mInstance;

    NewRecordInstance() {
        NewAudioName = FileUtils.getTempRecordPath();
        Log.e("ss", NewAudioName);
        mp3Recorder = new MP3Recorder(new File(NewAudioName));
    }

    public synchronized static NewRecordInstance getInstance() {
        if (mInstance == null) {
            mInstance = new NewRecordInstance();
        }
        return mInstance;
    }

    public String startRecord() {
        //判断是否有外部存储设备sdcard
        if (FileUtils.isSdcardExit()) {
            if (isStart) {
                return "正在录音中，请先停止录音";
            } else {
                try {
                    mp3Recorder.start();
                } catch (Exception e) {

                }
                // 让录制状态为true
                isStart = true;
                return "success";
            }

        } else {
            return "没有SD卡，无法存储录音数据";
        }

    }

    public boolean isStart() {
        return isStart;
    }

    public void startMediaPlay(final String url) {
        if (!isStart) {
            try {
                if (mediaPlayer == null) {
                    creatMediaPlayer(url);
                }
                mediaPlayer.prepare();
            } catch (IOException e) {
                isStart = false;
                stopMediaPlay();
                mp3Recorder.stop();
                e.printStackTrace();
            }
        }

    }

    public void startMediaPlay() {
        if (!isStart) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }

    }

    public void stopMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void creatMediaPlayer(String file) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    startRecord();
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp3Recorder.stop();
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
                    mp3Recorder.stop();
                    mp.reset();
                    mp.release();
                    mediaPlayer = null;
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

    public void setData(String url, String tag) {
        this.tag = tag;
        startMediaPlay(url);


    }

    public void toStart() {
        startRecord();
        startMediaPlay();
        if (iml != null) {
            iml.onStart();
        }
    }

    public void toStop() {
        stopMediaPlay();
        isStart = false;//停止文件写入
        mp3Recorder.stop();
        if (iml != null) {
            iml.onStop();
        }
    }

    public void toPause() {
        pauseMediaPlay();
        isStart = false;//停止文件写入
        mp3Recorder.stop();
        if (iml != null) {
            iml.onPause();
        }
    }

    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }
}
