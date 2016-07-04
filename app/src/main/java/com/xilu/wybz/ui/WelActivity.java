package com.xilu.wybz.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xilu.wybz.BuildConfig;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.manager.ConsoleActivity;
import com.xilu.wybz.utils.GetDomainUtil;
import com.xilu.wybz.utils.PrefsUtil;

import butterknife.Bind;


public class WelActivity extends BaseActivity {
    public static final int CODE = 1;

    @Bind(R.id.rl_main)
    RelativeLayout rlMain;

    private boolean isConsole = false;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetDomainUtil getDomainUtil = new GetDomainUtil(context);
//        String appLogo = PrefsUtil.getString("applogo", context);
//        String fileName = MD5Util.getMD5String(appLogo) + ".png";
//        String filePath = FileDir.logoDir + fileName;
//        if (StringUtil.isNotBlank(appLogo)) {
//            if (new File(filePath).exists()) {
//                rlMain.setBackground(new BitmapDrawable(BitmapUtils.getSDCardImg(filePath)));
//            }else{
//                rlMain.setBackgroundResource(R.drawable.bg_wel);
//                getDomainUtil.downLoadLogo(appLogo);
//            }
//        }else{
//            rlMain.setBackgroundResource(R.drawable.bg_wel);
//        }
        rlMain.setBackgroundResource(R.drawable.bg_wel);
        getDomainUtil.getNewIp();
        if(PrefsUtil.getUserId(context)>0){
            getDomainUtil.getCheck();
        }

        if (BuildConfig.DEBUG){

            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelActivity.this, ConsoleActivity.class);
                    startActivityForResult(intent,CODE);
                    isConsole = true;
                    if (handler != null){
                        handler.removeCallbacks(runnable);
                    }
                }
            });
        }
        toHome();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE && resultCode == RESULT_OK){
        isConsole = false;
        toHome();
    }
}

    // 跳转到首页
    public void toHome() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                toMainAct();
            }
        };

        handler.postDelayed(runnable, 1000);
    }


    private void toMainAct(){
        if (isConsole){
            return;
        }
        Intent intent = new Intent(WelActivity.this, MainTabActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
