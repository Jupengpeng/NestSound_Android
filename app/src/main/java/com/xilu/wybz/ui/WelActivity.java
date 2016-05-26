package com.xilu.wybz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.service.GetDomainService;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.utils.PrefsUtil;

import butterknife.Bind;


public class WelActivity extends BaseActivity {
    @Bind(R.id.iv_logo)
    SimpleDraweeView ivLogo;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String applogo = PrefsUtil.getString("applogo",this);
        if(!TextUtils.isEmpty(applogo)){
            Log.e("applogo",applogo);
            loadImage(applogo,ivLogo);
        }else{
            loadImage("res:///"+R.drawable.bg_wel,ivLogo);
        }
        Intent getDomainService = new Intent(this, GetDomainService.class);
        startService(getDomainService);
        toMainAct(2000);
    }

    public void toMainAct(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.class);
                finish();
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
