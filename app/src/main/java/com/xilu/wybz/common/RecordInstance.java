package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.czt.mp3recorder.MP3Recorder;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    protected OnRecordStatuListener listener;

    public List<Short> waveDatas;

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
     *
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
            if (isStart){
                mediaPlayer.stop();
            }
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
//                    startRecord();
                    mp.start();
                    isStart = true;
                    if (listener != null) {
                        listener.onRecordStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp3Recorder.stop();
                    mp.stop();
                    mp.release();
                    isStart = false;
                    if (listener != null) {
                        listener.onRecordComplete();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.release();
                    isStart = false;
                    if (listener != null) {
                        listener.onRecordError();
                    }
                    return false;
                }
            });

        }
    }

    public boolean startRecord() {
        try {
            mp3Recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        isStart = true;
        return true;
    }

    public void setDataSource(String url) {
        this.playUrl = url;
    }

    public boolean toStart() {
        if (playUrl == null || playUrl.length() == 0) {
            return false;
        }
        if (isStart) {
            return false;
        }
//        deleteCacheFile();
        try {
            mp3Recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startMediaPlay(playUrl);
    }

    public void toStop() {
        stopMediaPlay();
//        mp3Recorder.stop();
//        mp3Recorder.flush();
        if (listener != null) {
            listener.onRecordStop();
        }
    }

    public void toPause() {
        if (mp3Recorder != null) {
//            mp3Recorder.pause();
        }
        pauseMediaPlay();
        isStart = false;
        if (listener != null) {
            listener.onRecordPause();
        }
    }

    public void toRestart() {
        startMediaPlay();
        startRecord();
        if (listener != null) {
            listener.onRecordRestart();
        }
    }

    public void deleteCacheFile() {


        FileUtils.delFile(new File(localCacheFile));
    }

    public boolean saveRecorderFileTo(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            return false;
        }
        return FileUtils.copyFile(localCacheFile, fileName);
    }

    public void saveWaveDatas(){
        waveDatas = mp3Recorder.buf;
    }

    public void setOnRecordStatuListener(OnRecordStatuListener listener) {
        this.listener = listener;
    }

    public void destroy() {
        if (mediaPlayer != null) {
            if (isStart){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }

        if (mp3Recorder != null) {
            mp3Recorder.stop();
            mp3Recorder.flush();
        }
        mInstance = null;
        waveDatas = null;

    }


    interface OnRecordStatuListener {

        void onRecordStart();

        void onRecordPause();

        void onRecordStop();

        void onRecordRestart();

        void onRecordError();

        void onRecordComplete();

    }
}
