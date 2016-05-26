package com.xilu.wybz.ui.song;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class WaveSurfaceHelper {

    protected SurfaceView surfaceView;
    protected SurfaceHolder surfaceHolder;

    protected Rect viewRect ;

    protected int currentPosition;

    private List<Short> data;

    Thread runThead;
    boolean isrun = false;

    public WaveSurfaceHelper(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();

        init();
    }


    public void init(){

        initPaint();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("surface","surfaceCreated");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("surface","surfaceChanged");
                calculateViewRect(viewRect = new Rect());
                Log.d("surface",viewRect+":"+viewRect.right);
                onDrawWaveByThread();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("surface","surfaceDestroyed");
                isrun = false;
            }
        });

    }

    protected void calculateViewRect(Rect rect){
        surfaceView.getLocalVisibleRect(rect);
    }


    public void onDrawWaveByThread(){
        runThead = new Thread(){
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                isrun = true;
                while (isrun){

                    startList();
                    long start = System.currentTimeMillis();
                    onDrawWave();
                    long stop = System.currentTimeMillis();

                    Log.d("surface","onDraw time:"+(stop -start));

                    try {

                        runThead.sleep(40l);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };


        runThead.start();
    }

    public void onDrawWave() {
        onDrawWave(data,0);
    }


    int backgroundColor = Color.parseColor("#ffffff");

    Paint p1 = new Paint();
    Paint p2 = new Paint();

    Paint p3 = new Paint();
    Paint p4 = new Paint();
    Paint p5 = new Paint();
    Paint p6 = new Paint();

    public void initPaint(){
        p1.setColor(Color.parseColor("#ff686868"));
        p1.setTextSize(20);
        p1.setAntiAlias(true);

        p2.setColor(Color.parseColor("#ffff0000"));

        p3.setColor(Color.parseColor("#ffffd705"));
        p4.setColor(Color.parseColor("#42ffd705"));

        p5.setColor(Color.parseColor("#ffefd705"));
        p6.setColor(Color.parseColor("#42f4d705"));
    }

    public void onDrawWave(List<Short> data, int position) {
        Canvas canvas;

        synchronized (surfaceHolder) {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(backgroundColor);
//            Rect r = new Rect(100, 50, 300, 250);
//            canvas.drawRect(r, p1);
//            canvas.drawText("这是第" + (100) + "秒", 100, 0, p1);
            drawWave(canvas);
            drawsRuler(canvas);

        }


        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    public void drawsRuler(Canvas canvas){
        int x = 10;
        int y = 40;
        int d = 1;
        int h1 = 30;
        int h2 = 10;
        int s = 60;

        Rect v=viewRect;
        Rect r;

        r = new Rect(x, y, v.right, y+d);

        canvas.drawRect(r,p1);

        r = new Rect(x, y-h1, x+d, y);
        int i=0;
        while (true){
            if (!checkRect(r)) break;

            if (i%2 == 0){
                r.top = r.bottom-h1;
            } else {
                r.top = r.bottom-h2;
            }

            canvas.drawRect(r,p1);

            if (i%2 == 0){
                canvas.drawText("00.00",r.left+5,r.bottom-10,p1);
            }
            i++;
            r.left += s;
            r.right += s;
        }


        r = new Rect(0, v.centerY(), v.right, v.centerY()+1);
        canvas.drawRect(r,p1);

        r = new Rect(v.centerX(), 0, v.centerX()+1, v.bottom);
        canvas.drawRect(r,p2);

//        canvas.drawRect(r,p1);


    }

    public void drawWave(Canvas canvas){

        int d = 2;
        int s = 4;


        Rect v=viewRect;
        Rect r;

        List<Short> list = data;

        if (list == null || list.size() == 0){
            return;
        }

        int curent = list.size()-1;

        int cx = v.centerX();
        int cy = v.centerY();
        int w = list.get(curent);

        w = w*2;
        r = new Rect(cx-s, cy-w, cx-s+d, cy);

        while (true){
            if (!checkRect(r)) break;

            canvas.drawRect(r,p3);

            curent--;
            if (curent < 0) break;
            w = list.get(curent);
            w = w*2;
            r.top = cy-w;
            r.left -= s;
            r.right -= s;
        }

        curent = list.size()-1;
        w = list.get(curent);
        w = w*2;
        r = new Rect(cx-s, cy, cx-s+d, cy+w);

        while (true){
            if (!checkRect(r)) break;

            canvas.drawRect(r,p4);

            curent--;
            if (curent < 0) break;
            w = list.get(curent);
            w = w*2;
            r.bottom = cy+w;
            r.left -= s;
            r.right -= s;
        }





    }

    protected boolean checkRect(Rect r){
        Rect v = viewRect;
        if (r.left>=v.right || r.right < v.left){
            return false;
        }
        return true;
    }





    public void startList(){

        if (data == null){
            data = new ArrayList<>(500);
        }
        int size = data.size();
        if (size > 400){
            data = data.subList(size-100,size);
        }

        short w = (short) (Math.random()*60);
        data.add(w);

    }


    public List<Short> getList(){
        List<Short> list = new ArrayList<>();
               list.add((short) 16);

        list.add((short) 10);
        list.add((short) 12);

        return list;
    }

}
