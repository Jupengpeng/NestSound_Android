package com.xilu.wybz.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.service.GetDomainService;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import java.io.File;

import butterknife.Bind;


public class WelActivity extends BaseActivity {
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wel;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String page_start_pic = PrefsUtil.getString("applogo", context);
        if (StringUtil.isNotBlank(page_start_pic)) {
            String fileName = MD5Util.getMD5String(page_start_pic) + ".png";
            String filePath = DownLoaderDir.logoDir + fileName;
            if (new File(filePath).exists()) {
                rlMain.setBackground(new BitmapDrawable(BitmapUtils.getSDCardImg(filePath)));
            }
        }else{
            rlMain.setBackgroundResource(R.drawable.bg_wel);
        }
        Intent getDomainService = new Intent(this, GetDomainService.class);
        startService(getDomainService);

        startActivity(MainTabActivity.class);
        finish();
        overridePendingTransition(0, R.anim.wave_scale);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
