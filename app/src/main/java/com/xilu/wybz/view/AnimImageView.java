package com.xilu.wybz.view;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片动态切换器
 */
public class AnimImageView {
    static final int MSG_START = 0xf1;
    static final int MSG_STOP = 0xf2;
    static final int STATE_STOP = 0xf3;
    static final int STATE_RUNNING = 0xf4;

    /* 运行状态*/
    int mState = STATE_RUNNING;
    ImageView mImageView;
    /* 图片资源ID列表*/
    List<Integer> mResourceIdList = null;
    /* 定时任务*/
    Timer mTimer = null;
    AnimTimerTask mTimeTask = null;
    /* 记录播放位置*/
    int mFrameIndex = 0;
    /* 播放形式*/
    boolean isLooping = false;

    public AnimImageView() {
        mTimer = new Timer();
    }

    /**
     * 设置动画播放资源
     */
    public void setAnimation(ImageView imageview, List<Integer> resourceIdList) {
        mImageView = imageview;
        mResourceIdList = resourceIdList;
    }

    /**
     * 开始播放动画
     *
     * @param loop     时候循环播放
     * @param duration 动画播放时间间隔
     */
    public void start(boolean loop, int duration) {
        stop();
        isLooping = loop;
        mFrameIndex = 0;
        mState = STATE_RUNNING;
        mTimeTask = new AnimTimerTask();
        mTimer.schedule(mTimeTask, 0, duration);
    }

    /**
     * 停止动画播放
     */
    public void stop() {
        if (mTimeTask != null) {
            mFrameIndex = 0;
            mState = STATE_STOP;
            mTimer.purge();
            mTimeTask.cancel();
            mTimeTask = null;
            mImageView.setBackgroundResource(0);
        }
    }

    /**
     * 定时器任务
     */
    class AnimTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mFrameIndex < 0 || mState == STATE_STOP) {
                return;
            }

            if (mFrameIndex < mResourceIdList.size()) {
                Message msg = AnimHanlder.obtainMessage(MSG_START, 0, 0, null);
                msg.sendToTarget();
            } else {
                mFrameIndex = 0;
                if (!isLooping) {
                    Message msg = AnimHanlder.obtainMessage(MSG_STOP, 0, 0, null);
                    msg.sendToTarget();
                }
            }
        }
    }

    Handler AnimHanlder = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_START: {
                    if (mFrameIndex >= 0 && mFrameIndex < mResourceIdList.size() && mState == STATE_RUNNING) {
                        mImageView.setImageResource(mResourceIdList.get(mFrameIndex));
                        mFrameIndex++;
                    }
                }
                break;
                case MSG_STOP: {
                    if (mTimeTask != null) {
                        mFrameIndex = 0;
                        mTimer.purge();
                        mTimeTask.cancel();
                        mState = STATE_STOP;
                        mTimeTask = null;
                        mImageView.setImageResource(0);
                    }
                }
                break;
                default:
                    break;
            }
        }
    };
}