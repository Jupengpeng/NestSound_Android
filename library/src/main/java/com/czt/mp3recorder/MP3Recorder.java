package com.czt.mp3recorder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

import com.czt.mp3recorder.util.LameUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MP3Recorder {
    //=======================AudioRecord Default Settings=======================
    private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    private static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    /**
     * 下面是对此的封装
     * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
     */
    private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings=====================
    private static final int DEFAULT_LAME_MP3_QUALITY = 7;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    private static final int DEFAULT_LAME_IN_CHANNEL = 1;
    /**
     * Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
    private static final int DEFAULT_LAME_MP3_BIT_RATE = 32;

    //==================================================================
    private ArrayList<Short> inBuf = new ArrayList<Short>();
    public int rateX = 30;
    public int rateY = 0;
    private int baseLine = 0;
    private long c_time;
    private int divider = 2;
    private int draw_time = 1000 / 15;
    private SurfaceView sfv;
    private Paint mPaint;
    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    private static final int FRAME_COUNT = 160;
    private AudioRecord mAudioRecord = null;
    private int mBufferSize;
    private short[] mPCMBuffer;
    private DataEncodeThread mEncodeThread;
    private boolean mIsRecording = false;
    private File mRecordFile;


    /**
     * Default constructor. Setup recorder with default sampling rate 1 channel,
     * 16 bits pcm
     *
     * @param recordFile target file
     */
    public MP3Recorder(File recordFile) {
        mRecordFile = recordFile;
    }
    public void setSurfaceView(SurfaceView sfv) {
        this.sfv = sfv;
        inBuf.clear();
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException initAudioRecorder throws
     */
    public void start() throws IOException {
        if (mIsRecording) return;
        initAudioRecorder();
        mAudioRecord.startRecording();
        new Thread() {

            @Override
            public void run() {
                //设置线程权限
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                mIsRecording = true;
                while (mIsRecording) {
                    int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
                    if (readSize > 0) {
                        mEncodeThread.addTask(mPCMBuffer, readSize);
                    }


                    Log.d("auto","size:"+readSize+"--:"+mBufferSize);



                    synchronized (inBuf) {
                        int len = readSize / rateX;
                        for (int i = 0; i < len; i += rateX) {
                            inBuf.add((short) ((0x0000 | mPCMBuffer[i + 1]) << 8 | mPCMBuffer[i]));
                        }
                    }

                    Log.d("auto","inBuf:"+inBuf.size()+"v:"+inBuf.get(inBuf.size()-1));



                    long time = new Date().getTime();
                    if (time - c_time >= draw_time) {
                        ArrayList<Short> buf = new ArrayList<Short>();
                        synchronized (inBuf) {
                            if (inBuf.size() == 0)
                                return;
//                            while (inBuf.size() > sfv.getWidth() / divider) {
//                                inBuf.remove(0);
//                            }
                            buf = (ArrayList<Short>) inBuf.clone();// 保存
                        }

                        if (listenner!= null){
                            listenner.onChange(buf);
                        }


//                        toDraw(buf);// 把缓冲区数据画出来
//                        c_time = new Date().getTime();

//                        calculateRealVolume(mPCMBuffer, readSize);
                    }
                }
                    // release and finalize audioRecord
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                    // stop the encoding thread and try to wait
                    // until the thread finishes its job
                    Message msg = Message.obtain(mEncodeThread.getHandler(),
                            DataEncodeThread.PROCESS_STOP);
                    msg.sendToTarget();

            }
//			/**
//			 * 此计算方法来自samsung开发范例
//			 *
//			 * @param buffer buffer
//			 * @param readSize readSize
//			 */
//			private void calculateRealVolume(short[] buffer, int readSize) {
//				double sum = 0;
//				for (int i = 0; i < readSize; i++) {
//				    // 这里没有做运算的优化，为了更加清晰的展示代码
//				    sum += buffer[i] * buffer[i];
//				}
//				if (readSize > 0) {
//					double amplitude = sum / readSize;
//					mVolume = (int) Math.sqrt(amplitude);
//				}
//			};


        }.start();
    }

    private int mVolume;

    public int getVolume() {
        return mVolume;
    }

    private static final int MAX_VOLUME = 2000;

    public int getMaxVolume() {
        return MAX_VOLUME;
    }

    public void stop() {
        mIsRecording = false;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    /**
     * Initialize audio recorder
     */
    private void initAudioRecorder() throws IOException {
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());

        int bytesPerFrame = DEFAULT_AUDIO_FORMAT.getBytesPerFrame();
        /* Get number of samples. Calculate the buffer size
		 * (round up to the factor of given frame size) 
		 * 使能被整除，方便下面的周期性通知
		 * */
        int frameSize = mBufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            mBufferSize = frameSize * bytesPerFrame;
        }

		/* Setup audio recorder */
        mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
                DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(),
                mBufferSize);

        mPCMBuffer = new short[mBufferSize];
		/*
		 * Initialize lame buffer
		 * mp3 sampling rate is the same as the recorded pcm sampling rate 
		 * The bit rate is 32kbps
		 * 
		 */
        LameUtil.init(DEFAULT_SAMPLING_RATE, DEFAULT_LAME_IN_CHANNEL, DEFAULT_SAMPLING_RATE, DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
        // Create and run thread used to encode data
        // The thread will
        mEncodeThread = new DataEncodeThread(mRecordFile, mBufferSize);
        mEncodeThread.start();
        mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
        mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
    }
    public void cleanDraw() {
        Canvas canvas = sfv.getHolder().lockCanvas(
                new Rect(0, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
        Log.d("TAG_cleanDraw", String.valueOf(canvas));
        canvas.drawColor(Color.WHITE);// 清除背景
        sfv.getHolder().unlockCanvasAndPost(canvas);
        rateY = 0;
    }

    private void toDraw(ArrayList<Short> buf) {
        if (rateY == 0) {
            mPaint = new Paint();
            mPaint.setColor(Color.GREEN);
            mPaint.setStrokeWidth(2);// 设置画笔粗细
            rateY = 70000 / sfv.getHeight();
            baseLine = sfv.getHeight() / 2;
        }
        Canvas canvas = sfv.getHolder().lockCanvas(
                new Rect(0, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
        canvas.drawColor(Color.WHITE);// 清除背景
        int start = sfv.getWidth() - buf.size() * divider;
        int py = baseLine;
        if (buf.size() > 0)
            py += buf.get(0) / rateY;
        int y;
        canvas.drawLine(0, baseLine, start - divider, baseLine, mPaint);
        for (int i = 0; i < buf.size(); i++) {
            y = buf.get(i) / rateY + baseLine;// 调节缩小比例，调节基准线
            canvas.drawLine(start + (i - 1) * divider, py, start + i * divider, y, mPaint);
            py = y;
        }
        sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
    }

    public OnWaveChangeListenner listenner;


    public OnWaveChangeListenner getListenner() {
        return listenner;
    }

    public void setListenner(OnWaveChangeListenner listenner) {
        this.listenner = listenner;
    }

    public interface OnWaveChangeListenner {
        public void onChange(List<Short> data);
    }
}