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
                    startActivityForResult(intent,CODE);
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainAct();
            }
        }, 1200);
    }


    private void toMainAct(){
        Log.e("welcom_yinchao","toMainAct.DEBUG");
        if (isConsole){
            Log.e("welcom_yinchao","isConsole.DEBUG");
            return;
        }
        Intent intent = new Intent(WelActivity.this, MainTabActivity.class);
        startActivity(intent);
        Log.e("welcom_yinchao","startActivity.DEBUG");
        finish();
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
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
