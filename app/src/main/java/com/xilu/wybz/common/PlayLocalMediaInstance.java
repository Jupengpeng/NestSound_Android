package com.xilu.wybz.common;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.xilu.wybz.bean.CzMusicBean;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zning on 2015/9/16.
 */
public class PlayLocalMediaInstance {

    static PlayLocalMediaInstance mInstance;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayer2;
    IMediaPlayerListener iml;

    PlayLocalMediaInstance() {
    }

    public synchronized static PlayLocalMediaInstance getInstance() {
        if (mInstance == null)
            mInstance = new PlayLocalMediaInstance();
        return mInstance;
    }

    public void startMediaPlay(String url1, String url2) {
        if (!MyApplication.isPlay) {
            try {
                MyApplication.isPlay = true;
                if (mediaPlayer == null) {
                    creatMediaPlayer(url1);
                }
                if (mediaPlayer2 == null) {
                    Log.i("hehe", "med2");
                    creatMediaPlayer2(url2);
                }
                mediaPlayer.prepare();
                mediaPlayer2.prepare();
            } catch (Exception e) {
                MyApplication.isPlay = false;
                e.printStackTrace();
            }

        }

    }

    public void startMediaPlay(String url1) {
        if (!MyApplication.isPlay) {
            try {
                MyApplication.isPlay = true;
                if (mediaPlayer == null) {
                    creatMediaPlayer(url1);
                }
                mediaPlayer.prepare();
            } catch (Exception e) {
                MyApplication.isPlay = false;
                e.printStackTrace();
            }

        }

    }

    public void pauseMediaPlay() {
        if (MyApplication.isPlay) {

            MyApplication.isPlay = false;
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            if (mediaPlayer2 != null) {
                mediaPlayer2.pause();
            }
            if (iml != null) {
                iml.onPause();
            }
        }
    }

    public void resumeMediaPlay() {
        if (!MyApplication.isPlay) {
            MyApplication.isPlay = true;
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            if (mediaPlayer2 != null) {
                mediaPlayer2.start();
            }
            if (iml != null) {
                iml.onStart();
            }
        }
    }

    public void stopMediaPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.reset();
            mediaPlayer2 = null;
        }
        MyApplication.isPlay = false;
        if (iml != null) {
            iml.onStop();
        }
    }

    public boolean isPlay() {
        if (mediaPlayer != null) {
            return MyApplication.isPlay;
        }
        return false;
    }

    public boolean isInit() {
        return mediaPlayer == null;
    }

    public int getProgress() {
        if (mediaPlayer != null) {
            return (int) (mediaPlayer.getCurrentPosition() * 1.0f / mediaPlayer.getDuration() * 100);
        }
        return 0;
    }

    public int getCurrPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
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
                    MyApplication.isPlay = true;
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    MyApplication.isPlay = false;
                    if (mp != null) {
                        mp.reset();
                        mp.release();
                        mediaPlayer = null;
                    }

                    if (mediaPlayer2 != null) {
                        mediaPlayer2.reset();
                        mediaPlayer2.release();
                        mediaPlayer2 = null;
                    }

                    if (iml != null) {
                        iml.onOver();
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    MyApplication.isPlay = false;
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

    public void creatMediaPlayer2(String file) {
        if (mediaPlayer2 == null) {
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    MyApplication.isPlay = true;
                    mp.start();
                    if (iml != null) {
                        iml.onStart();
                    }
                }
            });
            try {
                mediaPlayer2.setDataSource(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setIMediaPlayerListener(IMediaPlayerListener iml) {
        this.iml = iml;
    }

    public void setData(CzMusicBean czMusicBean) {
        if (czMusicBean == null) {
            stopMediaPlay();
            return;
        } else {
            MyApplication.musicId = czMusicBean.getId();
        }

        if (czMusicBean.getUseheadset().equals(MyCommon.TAG_RECORD_IN)) {
            //插上耳机录的，需要播放双曲
            File file = new File(FileUtils.getLocalplayurl(czMusicBean.getId()));
            if (file.exists()) {
                startMediaPlay(FileUtils.getLocalplayurl(czMusicBean.getId()), FileUtils.getMusicCachePath(MyCommon.TYPE_MAKE + czMusicBean.getId()));
            } else {
                startMediaPlay(FileUtils.getMusicCachePath(MyCommon.TYPE_MAKE + czMusicBean.getId()));
            }
        } else {
            startMediaPlay(FileUtils.getLocalplayurl(czMusicBean.getId()));
        }
    }
}
