package com.xilu.wybz.utils;

import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/10.
 */
public class AliPayUtils {


    public final static int SDK_PAY_FLAG = 1;

    /**
     * pay.
     *
     * @param handler
     * @param orderInfo
     * @return
     */
    public static boolean pay(Handler handler, String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(null);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        return true;
    }


    /**
     * createHandler.
     * @param listener
     * @return
     */
    public static Handler createHandler(AliPayrespListener listener) {
        Handler mHandler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == SDK_PAY_FLAG){
                    listener.onResult();
                }
            }
        };
        return mHandler;
    }

    /**
     *  支付宝回调接口.
     */
    public interface AliPayrespListener{
        void onResult();
    }


}
