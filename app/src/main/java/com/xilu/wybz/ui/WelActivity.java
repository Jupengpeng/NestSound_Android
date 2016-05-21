package com.xilu.wybz.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.base.BaseActivity;


public class WelActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toMainAct(2000);
    }

    public void toMainAct(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainTabActivity.class);
                finish();
//                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
            }
        }, time);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
