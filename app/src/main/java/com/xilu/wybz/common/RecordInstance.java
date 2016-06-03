package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by June on 2016/5/4.
 */
public class RecordInstance {

    protected static RecordInstance mInstance;

    protected MP3Recorder mp3Recorder;
    protected MediaPlayer mediaPlayer;
    protected boolean isStart = false;// 设置正在录制的状态
    protected String tag;
    protected String localCacheFile;
    protected String playUrl;

    protected IMediaPlayerListener iml;

    protected RecordInstance() {
        this.localCacheFile = FileUtils.getTempRecordPath();
        this.mp3Recorder = new MP3Recorder(new File(localCacheFile));
    }

    public synchronized static RecordInstance getInstance() {
        if (mInstance == null) {
            mInstance = new RecordInstance();
        }
        return mInstance;
    }


    public MP3Recorder getMp3Recorder() {
        return mp3Recorder;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isStart() {
        return isStart;
    }

    /**
     * 开始播放伴奏音乐文件.
     * 待prepare好后即开始播放和录音
     * @param url
     * @return
     */
    public boolean startMediaPlay(final String url) {
        if (!isStart) {
            isStart = true;
            creatMediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                return true;
            } catch (IOException e) {
                isStart = false;
                stopMediaPlay();
                e.printStackTrace();
            }
        }
        return false;
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
            mediaPlayer.release();
        }
    }

    public void pauseMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void creatMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (!startRecord()){
                        return;
                    }
                    mp.start();
                    isStart = true;
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp3Recorder.stop();
                    mp.release();
                    mp.reset();
                    mediaPlayer = null;
                    isStart = false;
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
                    isStart = false;
                    if (iml != null) {
                        iml.onError();
                    }
                    return false;
                }
            });

        }
    }

    private boolean startRecord() {
        try {
            mp3Recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        isStart = true;
        return true;
    }

    public void setDataSource(String url){
        this.playUrl = url;
    }

    public boolean toStart() {
        if (playUrl == null || playUrl.length() == 0) {
            return false;
        }
        if (isStart){
            return false;
        }
        try {
            mp3Recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startMediaPlay(playUrl);
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
        if (mp3Recorder != null){

            mp3Recorder.pause();
        }
        if (iml != null) {
            iml.onPause();
        }
    }

    public void toReplay() {
        startMediaPlay();
        startRecord();
        isStart = true;
    }

    public void deleteCacheFile(){


        FileUtils.delFile(new File(localCacheFile));
    }

    public boolean saveRecorderFileTo(String fileName){
        if (StringUtil.isBlank(fileName)){
            return false;
        }
        return FileUtils.copyFile(localCacheFile,fileName);
    }

    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }

    public void destroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer.reset();

        mp3Recorder.stop();
        mp3Recorder.flush();
    }
}
