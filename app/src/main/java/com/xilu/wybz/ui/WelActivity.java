package com.xilu.wybz.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.service.GetDomainService;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
        EventBus.getDefault().register(this);
        String page_start_pic = PrefsUtil.getString("applogo", context);
        if (StringUtil.isNotBlank(page_start_pic)) {
            String fileName = MD5Util.getMD5String(page_start_pic) + ".png";
            String filePath = FileDir.logoDir + fileName;
            if (new File(filePath).exists()) {
                rlMain.setBackground(new BitmapDrawable(BitmapUtils.getSDCardImg(filePath)));
            }
        }else{
            rlMain.setBackgroundResource(R.drawable.bg_wel);
        }
//        Intent getDomainService = new Intent(this, GetDomainService.class);
//        startService(getDomainService);
        if(PrefsUtil.getUserId(context)>0) {
            HttpUtils httpUtils = new HttpUtils(context);
            Map<String, String> params = new HashMap<>();
            UserBean userBean = PrefsUtil.getUserInfo(context);
            params.put("token", userBean.loginToken);
            httpUtils.post(MyHttpClient.getTokenCheck(),params,new MyStringCallback(){
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if(code==200){
                            UserBean userBean1 = ParseUtils.getUserBean(context,response);
                            PrefsUtil.saveUserInfo(context, userBean1);
                            toHome();
                        }else{
                            //清除本地用户信息
                            PrefsUtil.saveUserInfo(context, new UserBean());
                            new MaterialDialog.Builder(context)
                                    .title(getResources().getString(R.string.progress_dialog))
                                    .content("登录状态失效，请重新登录！")
                                    .positiveText("登录")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            startActivity(LoginActivity.class);
                                        }
                                    }).negativeText("取消")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            toMainAct();//去首页
                                        }
                                    }).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            toHome();
        }
    }
    public void onEventMainThread(Event.LoginSuccessEvent event){
        UserBean ub = event.getUserBean();
        MyApplication.getInstance().setUserid(ub.userid);
        MyApplication.getInstance().setIsLogin(true);
        PrefsUtil.saveUserInfo(context, ub);
        MobclickAgent.onProfileSignIn(ub.userid+"");
        PushAgent.getInstance(context).setAlias(ub.userid+"", "yinchao");
        PushAgent.getInstance(context).setExclusiveAlias(ub.userid+"", "yinchao");
        toMainAct();
    }
    public void onEventMainThread(Event.LoginEvent event){
        toMainAct();
    }
    // 跳转到首页
    public void toHome() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                toMainAct();
            }
        }, 1200);
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
        EventBus.getDefault().unregister(this);
    }
}
