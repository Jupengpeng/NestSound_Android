package com.xilu.wybz.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.xilu.wybz.R;

/**
 * 倒计时工具类
 */
public class MyCountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 60100;// 时间防止从59s开始显示（以倒计时60s为例子）
    public TextView btn;
    public String endStr;
    public int normalColor, timingColor;// 未计时的文字颜色，计时期间的文字颜色
    public boolean isStart;

    public MyCountTimer(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStr = "获取验证码";
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        isStart = false;
        if (normalColor > 0) {
            btn.setTextColor(normalColor);
        }
        if (btn.getId() == R.id.tv_getsmscode) {
            btn.setBackgroundResource(R.drawable.corner_yellow3);
            btn.setEnabled(true);
            btn.setText(endStr);
        } else {
            btn.setText(endStr);
            btn.setEnabled(true);
        }
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        isStart = true;
        if (timingColor > 0) {
            btn.setTextColor(timingColor);
        }
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + "s");
    }
}