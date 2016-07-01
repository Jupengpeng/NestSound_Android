package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.xilu.wybz.R;

/**
 * Created by Administrator on 2016/7/1.
 */
public class LoadingDialog extends Dialog {


    protected Context context;
    protected OnKeyCancelListener onKeyCancelListener;


    private ProgressBar bar;
    private TextView text;

    public LoadingDialog(Context context) {
        super(context,R.style.ToastDialog);
        this.context = context;
        init();
    }


    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null);

        bar = (ProgressBar) view.findViewById(R.id.loading_bar);
        text = (TextView) view.findViewById(R.id.loading_message);

        setContentView(view);
        setCanceledOnTouchOutside(false);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (onKeyCancelListener != null){
                onKeyCancelListener.onCancel();
                return true;
            }
            Log.d("yinchao","loading dialog onKeyDown envent.");
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setOnKeyCancelListener(OnKeyCancelListener listener){

        this.onKeyCancelListener = listener;
    }


    public interface OnKeyCancelListener {
        void onCancel();
    }
}
