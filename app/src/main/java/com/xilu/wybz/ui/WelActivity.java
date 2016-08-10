package com.xilu.wybz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.xilu.wybz.BuildConfig;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.manager.ConsoleActivity;
import com.xilu.wybz.utils.GetDomainUtil;
import com.xilu.wybz.utils.PrefsUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


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
        handler = new Handler();
        GetDomainUtil getDomainUtil = new GetDomainUtil(context);
        String appLogo = PrefsUtil.getString("applogo", context);
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
            Log.e("welcom_yinchao","BuildConfig.DEBUG");
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("welcom_yinchao","onClick.DEBUG");
                    isConsole = true;
                    Intent intent = new Intent(WelActivity.this, ConsoleActivity.class);
//                    Intent intent = new Intent(WelActivity.this, SongTuningActivity.class);
                    startActivityForResult(intent,CODE);
                }
            });
        }
        delayToHome();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE && resultCode == RESULT_OK){
        isConsole = false;
        delayToHome();
    }
}

    // 跳转到首页
    public void delayToHome() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 1000);

    }


    private void startMainActivity(){
        Log.e("welcom_yinchao","startMainActivity.DEBUG");
        if (isConsole){
            Log.e("welcom_yinchao","isConsole.DEBUG");
            return;
        }
        Intent intent = new Intent(WelActivity.this, MainTabActivity.class);
//        Intent intent = new Intent(WelActivity.this, ConsoleActivity.class);
        startActivity(intent);
        Log.e("welcom_yinchao","startActivity.DEBUG");
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
