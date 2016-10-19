package com.xilu.wybz.ui.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.umeng.analytics.MobclickAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.login.ModifyPwdActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by June on 16/5/13.
 */
public class SettingActivity extends ToolbarActivity {
    @Bind(R.id.cb_notice)
    CheckBox cbNotice;
    @Bind(R.id.cb_countdown)
    CheckBox cbCountdown;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setBackgroundResource(R.color.main_theme_color);
        initView();
    }
    private void initView() {
        setTitle("设置");
        cbNotice.setChecked(PrefsUtil.getBoolean(KeySet.KEY_PUSH_OPEN,getApplicationContext()));
        cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushInterface.resumePush(getApplicationContext());
                    showMsg("打开消息推送");
                }else{
                    JPushInterface.stopPush(getApplicationContext());
                    showMsg("关闭消息推送");
                }
                PrefsUtil.putBoolean(KeySet.KEY_PUSH_OPEN, isChecked, context);
            }
        });
        /** 倒计时开关*/
        cbCountdown.setChecked(PrefsUtil.getBoolean(KeySet.KEY_COUNTDOWN_OPEN,getApplicationContext()));
        cbCountdown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showMsg("打开倒计时");
                }else{
                    showMsg("关闭倒计时");
                }
                PrefsUtil.putBoolean(KeySet.KEY_COUNTDOWN_OPEN, isChecked, context);
            }
        });



    }
    @OnClick({R.id.ll_other_account, R.id.ll_clear_cache, R.id.ll_modify_pwd, R.id.ll_feedback, R.id.ll_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_other_account:
                startActivity(OtherAccountActivity.class);
                break;
            case R.id.ll_clear_cache:
                DelCache();
                break;
            case R.id.ll_modify_pwd:
                startActivity(ModifyPwdActivity.class);
                break;
            case R.id.ll_feedback:
                startActivity(AboutMessageActivity.class);
                break;
            case R.id.ll_loginout:
                loginOut();
                break;
        }
    }
    private void loginOut(){
        new MaterialDialog.Builder(context)
                .title(getString(R.string.dialog_title))
                .content("请确认是否退出当前账号?")
                .positiveText("退出登录")
                .positiveColor(getResources().getColor(R.color.red))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        HttpUtils httpUtils = new HttpUtils(context);
                        httpUtils.post(MyHttpClient.getLoginOut(),null,new MyStringCallback(){
                        });
                        PrefsUtil.saveUserInfo(context, new UserBean());
                        MobclickAgent.onProfileSignOff();
                        EventBus.getDefault().post(new Event.LoginOutEvent());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },300);
                    }
                }).negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();
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
                .title(R.string.dialog_title)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }

}
