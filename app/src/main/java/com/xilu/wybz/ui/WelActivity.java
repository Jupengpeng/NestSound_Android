package com.xilu.wybz.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.utils.GetDomainUtil;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import java.io.File;

import butterknife.Bind;
import de.greenrobot.event.EventBus;


public class WelActivity extends BaseActivity {
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetDomainUtil getDomainUtil = new GetDomainUtil(context);
        String appLogo = PrefsUtil.getString("applogo", context);
        String fileName = MD5Util.getMD5String(appLogo) + ".png";
        String filePath = FileDir.logoDir + fileName;
        if (StringUtil.isNotBlank(appLogo)) {
            if (new File(filePath).exists()) {
                rlMain.setBackground(new BitmapDrawable(BitmapUtils.getSDCardImg(filePath)));
            }else{
                rlMain.setBackgroundResource(R.drawable.bg_wel);
                getDomainUtil.downLoadLogo(appLogo);
            }
        }else{
            rlMain.setBackgroundResource(R.drawable.bg_wel);
        }
//        getDomainUtil.getNewIp();
        if(PrefsUtil.getUserId(context)>0)getDomainUtil.getCheck();
        toHome();
    }
    // 跳转到首页
    public void toHome() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                toMainAct();
            }
        }, 1000);
    }
    private void toMainAct(){
        Intent intent = new Intent(WelActivity.this, MainTabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_logo_scale_enter, R.anim.activity_logo_scale_exit);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
