package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xilu.wybz.R;

import static com.xilu.wybz.ui.MyApplication.context;

/**
 * Created by Administrator on 2016/10/18.
 *
 *
 * 用于作曲界面的倒计时.
 *
 */

public class CountdownDialog extends Dialog {


    private ProgressBar bar;
    private TextView text;
    private OnOkListener listener;


    private Handler mHandler;
    private ConutDownTask conutDownTask;
    private int count = 3;

    /**
     * CountdownDialog.
     * @param context
     */
    public CountdownDialog(Context context) {
        super(context,R.style.CountDownDialog);
        init();
    }

    /**
     * CountdownDialog.
     * @param context
     */
    public CountdownDialog(Context context, OnOkListener listener ) {
        super(context,R.style.CountDownDialog);
        init();
        this.listener = listener;
    }


    /**
     * init.
     */
    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_count_down,null);

//        bar = (ProgressBar) view.findViewById(R.id.loading_bar);
        text = (TextView) view.findViewById(R.id.loading_message);

        setContentView(view);
        setCanceledOnTouchOutside(false);


        mHandler = new Handler();
        conutDownTask = new ConutDownTask();
    }


    /**
     * 开始计数.
     */
    public void startCountDown(){
        if (!isShowing()){
            show();
        }

        count = 4;
        mHandler.post(conutDownTask);

    }

    /**
     * 倒计时结束时回掉.
     */
    private void countDownOk(){
        if (listener != null){
            listener.onOk();
        }
        dismiss();
    }

    /**
     * 每个计数周期回掉.
     */
    private void count(){
        text.setText("count:"+count);
    }


    /**
     * ConutDownTask.
     */
    public class ConutDownTask implements Runnable{
        @Override
        public void run() {
            --count ;
            count();
            if (count <= 0){
                countDownOk();
            } else {
                mHandler.postDelayed(conutDownTask,1000);
            }
        }
    }

    /**
     * setListener.
     * @param listener
     */
    public void setListener(OnOkListener listener) {
        this.listener = listener;
    }

    /**
     * onKeyDown.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
//            Log.d("yinchao","loading dialog onKeyDown envent.");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * OnOkListener.
     */
    public interface OnOkListener {
        void onOk();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
