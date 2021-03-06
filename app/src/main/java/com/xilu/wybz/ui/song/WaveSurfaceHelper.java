package com.xilu.wybz.ui.song;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xilu.wybz.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class WaveSurfaceHelper {

    //protected
    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;

    protected Rect viewRect ;
    protected int currentPosition;
    protected int screenOff;
    protected Configure configure;

    protected Thread runThead;
    public boolean isrun = false;
    public boolean prepare = false;

    public List<Short> data;
    public static List<Short> dataCache;



    protected int totalSize = 0;
    protected int currentSize = 0;

    public boolean scroll = false;

    //private
    int backgroundColor;

    Paint p0 = new Paint();
    Paint p1 = new Paint();
    Paint p2 = new Paint();

    Paint p3 = new Paint();
    Paint p4 = new Paint();
    Paint p5 = new Paint();
    Paint p6 = new Paint();



    public WaveSurfaceHelper(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();
        init();
    }

    /**
     * WaveSurfaceHelper 初始化.
     */
    public void init(){
        //初始化参数配置，因为需要适配不同手机屏幕，需要计算必要参数值
        initConfigure();
        //初始化画笔，方便使用减少重复创建
//        initPaint();
        //设置Callback为了自动初始化View

        data = new ArrayList<>();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                prepare = true;
                Log.e("surface","surfaceCreated");
                calculateViewRect(viewRect = new Rect());
                adaptationScreen();
                initPaint();
                onDrawKernel();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                prepare = true;

                Log.e("surface","surfaceChanged");
                calculateViewRect(viewRect = new Rect());
                adaptationScreen();
                Log.e("surface",viewRect+":"+viewRect.right);
                initPaint();
                onDrawWaveByThread();

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                prepare = false;
                Log.e("surface","surfaceDestroyed");
                isrun = false;
            }
        });

    }

    public void initConfigure(){
        //使用默认配置
        configure = new Configure();

        screenOff = 0;
    }


    public void adaptationScreen(){

        float density = (float) (1.0*viewRect.right/360);

        configure.adapter(density);

        configure.waveMAX = (viewRect.bottom -configure.offy)/2;

    }

    public void initPaint(){

        //背景色
        backgroundColor = configure.backgroundColor;

        //绘制ruler
        p0.setColor(configure.color0);
        p0.setTextSize(configure.offy/2);
        p0.setAntiAlias(true);


        p1.setColor(configure.color1);
        p1.setTextSize(configure.offy/2);
        p1.setAntiAlias(true);
        //绘制中线
        p2.setColor(configure.color2);

        //绘制波形
        p3.setColor(configure.color3);
        p4.setColor(configure.color4);
        p5.setColor(configure.color5);
        p6.setColor(configure.color6);
    }

    /**
     * 计算画布尺寸.
     * @param rect
     */
    protected void calculateViewRect(Rect rect){
        surfaceView.getLocalVisibleRect(rect);
    }

    public void onDrawWaveByThread(){
        runThead = new Thread(){
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                isrun = true;
                int i=0;
                while (isrun){

                    try {

                        runThead.sleep(10l);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    long start = System.currentTimeMillis();
                    onDrawWave();
                    long stop = System.currentTimeMillis();
                    Log.e("surface","onDraw createtime:"+(stop -start));

                    if (i>20){

                        isrun = false;
                    }
                    i++;

                }
            }
        };
        runThead.start();
    }


    public void onDrawWave() {
        onDrawWave(-1);
    }


    public void onDrawWave(int position) {
        onDrawWave(null,position);
    }


    public void onDrawWave(List<Short> data, int position) {
        if (data != null){
            this.data = data;
        }
        if (position >= 0){
            if (position > this.data.size()){
                this.currentPosition = this.data.size();
            } else {
                this.currentPosition = position;
            }
        }
        if (prepare){
            onDrawKernel();
        }
    }



    public void onDrawKernel(){
        Canvas canvas;
        synchronized (surfaceHolder) {

            canvas = surfaceHolder.lockCanvas();
            if (canvas == null){
                return;
            }
            canvas.drawColor(backgroundColor);
            drawWave(canvas);
            drawsRuler(canvas);

            drawsText(canvas);

        }
        if (canvas != null){
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    public String getRulerDegreeScale(){
        String scale = "";


        return scale;
    }



    public int caculateCurrentSize(){
        int size = currentPosition/20;
        if (currentPosition%20 > 1){
            size++;
        }
        return size;
    }
    public int caculateTotalSize(){
        int size = data.size()/20;
        if (data.size()%20 > 1){
            size++;
        }
        totalSize = size;
        return size;
    }


    public void drawsText(Canvas canvas){

        currentSize = caculateCurrentSize();

        String total =  DateFormatUtils.formatTime(totalSize);
        String curent =  DateFormatUtils.formatTime(currentSize);

        Rect v=viewRect;

        canvas.drawText(curent+"/"+total, v.right - configure.offy*3, v.bottom - configure.offy/2, p1);

    }


    public void drawsRuler(Canvas canvas){

        int offy = configure.offy;
        int d = configure.d;
        int h1 = configure.h1;
        int h2 = configure.h2;
        int s = configure.s;

        Rect v=viewRect;

        int offx = -currentPosition*configure.waveSpace;

        int x = v.centerX() + offx ;
        int y = offy;

        int start = 0;

        if (x < 0){
            start = -(x/(2*s));
            x += 2*s*start;
        } else {
            start = -(x/(2*s)+1);
            x += 2*s*(start);
        }

        x -= screenOff;

        Rect r;

        r = new Rect(v.left, y, v.right, y+d);

        canvas.drawRect(r,p1);

        r = new Rect(x, y-h1, x+d, y);


        while (true) {
            if (checkRectOut(r) == 1) break;

            r.top = r.bottom - h1;
            canvas.drawRect(r, p1);

            canvas.drawText(DateFormatUtils.formatTime(start), r.left + configure.offy/8, r.bottom - configure.offy/4, p1);
            start++;

            r.top = r.bottom - h2;

            r.left += s;
            r.right += s;

            canvas.drawRect(r, p1);

            r.left += s;
            r.right += s;
        }

        //画横向中线
        r = new Rect(0, v.centerY()+offy/2, v.right, v.centerY()+offy/2+1);
        canvas.drawRect(r,p0);

        //画标示竖线
        r = new Rect(v.centerX(), offy, v.centerX()+1, v.bottom);
        canvas.drawRect(r,p2);

    }

    public void drawWave(Canvas canvas){

        int d = configure.wave;
        int s = configure.waveSpace;
        int offy = configure.offy;


        Rect v=viewRect;
        Rect r;

        List<Short> list = data;

        if (list == null){
            return;
        }

        if (list == null || list.size() == 0 ||currentPosition < 0){
            return;
        }

        int curent = 0;

        int cx = v.centerX()-screenOff-s;
        int cy = v.centerY()+offy/2;
        int w = 0;

        curent = currentPosition;
        r = new Rect(cx+s, cy, cx+s+d, cy);

        while (true){
            curent--;
            if (curent < 0) break;
            w = list.get(curent);
            w = computeWaveHeight(w);
            r.top = cy-w;
            r.left -= s;
            r.right -= s;

            if (!checkRect(r)) break;
            canvas.drawRect(r,p3);
        }

        curent = currentPosition;
        r = new Rect(cx+s, cy, cx+s+d, cy);

        while (true){
            curent--;
            if (curent < 0) break;
            w = list.get(curent);
            w = computeWaveHeight(w);
            r.bottom = cy+w;
            r.left -= s;
            r.right -= s;

            if (!checkRect(r)) break;
            canvas.drawRect(r,p4);
        }
        //--------------------------------------------
        //--------------------------------------------
        curent = currentPosition-1;
        r = new Rect(cx, cy, cx+d, cy);

        while (true){
            curent++;
            if (curent >= data.size()) break;
            w = list.get(curent);
            w = computeWaveHeight(w);
            r.top = cy-w;
            r.left += s;
            r.right += s;

            if (!checkRect(r)) break;
            canvas.drawRect(r,p5);
        }



        curent = currentPosition-1;
        r = new Rect(cx, cy, cx+d, cy+w);

        while (true){
            curent++;
            if (curent >= data.size()) break;
            w = list.get(curent);
            w = computeWaveHeight(w);
            r.bottom = cy+w;
            r.left += s;
            r.right += s;

            if (!checkRect(r)) break;
            canvas.drawRect(r,p6);
        }

    }


    protected int computeWaveHeight(int height ){
        int h;
        if (height < 0){
            h = -height;
        }else {
            h = height;
        }

        h= (int)Math.sqrt(1.0*h);

        h= (int)(h*configure.scaleRate);

        if (h > configure.waveMAX){
            h = configure.waveMAX;
        }
//        Log.d("d","h:"+height+" h2:"+h);
        return h;
    }

    protected boolean checkRect(Rect r){
        Rect v = viewRect;
        if (r.left>=v.right || r.right < v.left){
            return false;
        }
        return true;
    }



    protected int checkRectOut(Rect r){
        Rect v = viewRect;
        if (r.left>=v.right){
            return 1;
        }
        if (r.right < v.left){
            return -1;
        }
        return 0;
    }




    static int op = 0;
    public void startList(){

        if (data == null){
            data = new ArrayList<>(500);
        }
        int size = data.size();
//        if (size > 400){
//            data = data.subList(size-100,size);
//        }
//
//        short w = (short) (Math.random()*60);
//        data.add(w);

        if (size > 400){
            isrun = false;
            if ( op == 1 && currentPosition < 400){
                screenOff++;
                if (screenOff > 3){
                    screenOff = 0;
                    currentPosition ++;
                }
                return;
            } else {
                op = 0;
            }

            if (op == 0 && currentPosition > 0){
                screenOff--;
                if (screenOff < 0){
                    screenOff = 3;
                    currentPosition --;
                }
                return;
            } else {
                op = 1;
            }

        } else {
            short w = (short) (Math.random() * 60);
            data.add(w);
            currentPosition = data.size();
        }

    }



    public void setOffX(int offx){

//        Log.d("sur","offx:" + offx);
        if (data == null || data.size() == 0){
            return;
        }
        scroll = true;
        if (offx > 0){

            int i = offx/configure.waveSpace;
            int j = offx%configure.waveSpace;

            if (j + screenOff >= configure.waveSpace){
                i++;
                screenOff = screenOff+j-configure.waveSpace;
            } else {
                screenOff += j;
            }

            if ((currentPosition + i) <= data.size()){
                currentPosition += i;
            } else {

                currentPosition = data.size();
                screenOff = configure.waveSpace-1;
            }

        } else {

            int i = offx/configure.waveSpace;
            int j = offx%configure.waveSpace;

            if (j + screenOff < 0){
                i--;
                screenOff = screenOff+j+configure.waveSpace;
            } else {
                screenOff += j;
            }

            if ((currentPosition + i) >= 0){
                currentPosition += i;
            } else {

                currentPosition = 0;
                screenOff = 0;
            }
        }

        notifyChanged();

        onDrawWave();
    }

    private void notifyChanged () {
        if (progressChangedListener != null) {
            progressChangedListener.onChanged(currentPosition);
        }
    }


    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<Short> getList(){
        List<Short> list = new ArrayList<>();
               list.add((short) 16);

        list.add((short) 10);
        list.add((short) 12);

        return list;
    }


    public static class Configure{

        //global
        public int backgroundColor = Color.parseColor("#ffffffff");

        public int color0 = Color.parseColor("#ffe5e5e5");
        public int color1 = Color.parseColor("#ffc1c1c1");
        public int color2 = Color.parseColor("#ffff0000");
        public int color3 = Color.parseColor("#ffffd705");
        public int color4 = Color.parseColor("#32ffd705");
        public int color5 = Color.parseColor("#ffd6d6d6");
        public int color6 = Color.parseColor("#32d6d6d6");

        public float scaleRate = 0.68f;
        public float density = 2.0f;

        //ruler
        public int offy = 40;
        public int d = 1;
        public int h1 = 28;
        public int h2 = 6;
        public int s = 40;
        public int textSize = 20;

        //wave
        public int waveMAX = 60;
        public int wave = 2;
        public int waveSpace = 4;


        public void adapter(float density){

            if (density >= 1.7 && density <= 2.3){
               return;
            }
            if (this.density != 2){
                return;
            }
            this.density = density;

            offy = (int)computeValue(offy,density);
            h1 = (int)computeValue(h1,density);
            h2 = (int)computeValue(h2,density);

            s = (int)computeValue(s,density);
            textSize = (int)computeValue(textSize,density);

            waveMAX = (int)computeValue(waveMAX,density);
            wave = (int)computeValue(wave,density);
            waveSpace = (int)computeValue(waveSpace,density);
            scaleRate = scaleRate*density/2;

        }

        private float computeValue(int value, float rate){
            return value*rate/2;
        }


    }

    ProgressChangedListener progressChangedListener;

    public interface ProgressChangedListener{
        void onChanged(int progress);
    }


    public void setProgressChangedListener(ProgressChangedListener progressChangedListener) {
        this.progressChangedListener = progressChangedListener;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }
}
