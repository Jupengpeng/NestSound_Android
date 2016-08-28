package com.xilu.wybz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.xilu.wybz.BuildConfig;
import com.xilu.wybz.R;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.manager.ConsoleActivity;
import com.xilu.wybz.utils.GetDomainUtil;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WelActivity extends BaseActivity {
    public static final int CODE = 1;

    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.splash_ext)
    ImageView ivSplashExt;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    private boolean isConsole = false;
    private Handler handler;

    public static final String[] exts = {"_360", "ppzs"};

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

        String channel = BuildConfig.FLAVOR;
        for (String ext : exts) {

            if (ext.equalsIgnoreCase(channel)) {
                ivSplashExt.setVisibility(View.VISIBLE);
            }
        }
        String appLogo = PrefsUtil.getString("applogo", context);
        if (StringUtils.isNotBlank(appLogo)) {
            String fileName = MD5Util.getMD5String(appLogo) + ".jpg";
            String filePath = FileDir.logoDir + fileName;
            if (new File(filePath).exists()) {
                Picasso.with(context).load(new File(filePath)).into(ivLogo);
            } else {
                Picasso.with(context).load(appLogo).into(ivLogo);
            }
        }
        GetDomainUtil getDomain = new GetDomainUtil(this);
        getDomain.getBootPic();
        if (PrefsUtil.getUserId(context) > 0) {
            getDomain.getCheck();
        }
        if (BuildConfig.DEBUG) {
            llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isConsole = true;
                    Intent intent = new Intent(WelActivity.this, ConsoleActivity.class);
                    startActivityForResult(intent, CODE);
                }
            });
        }
        delayToHome();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE && resultCode == RESULT_OK) {
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


    private void startMainActivity() {
        if (isConsole) {
            return;
        }
        Intent intent = new Intent(WelActivity.this, MainTabActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
