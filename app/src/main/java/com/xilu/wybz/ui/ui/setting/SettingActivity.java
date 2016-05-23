package com.xilu.wybz.ui.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.setting.*;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;
import MaterialDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/13.
 */
public class SettingActivity extends ToolbarActivity {
    @Bind(R.id.cb_notice)
    CheckBox cbNotice;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("设置");
        cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked&& PushAgent.getInstance(context).isEnabled()) {
                    PushAgent.getInstance(context).disable();
                    showMsg("关闭消息推送");
                }
                PrefsUtil.putBoolean("isPushOpen", isChecked, context);
            }
        });
    }

    @OnClick({R.id.iv_modify, R.id.ll_clear_cache, R.id.ll_score, R.id.ll_feedback, R.id.ll_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_modify:
                startActivity(com.xilu.wybz.ui.setting.ModifyActivity.class);
                break;
            case R.id.ll_clear_cache:
                DelCache();
                break;
            case R.id.ll_score:
                Uri uri = Uri.parse("market://details?id=com.xilu.wybz");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.ll_feedback:
                break;
            case R.id.ll_loginout:
                break;
        }
    }

    private void DelCache() {
        showIndeterminateProgressDialog(true);
        //删除图片缓存文件夹和本地音乐缓存文件夹
        new Thread() {
            @Override
            public void run() {
                FileUtils.delAllFile();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x123);
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                    showMsg("清理完成！");
                }
            }
        }
    };

    private void showIndeterminateProgressDialog(boolean horizontal) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }
}
