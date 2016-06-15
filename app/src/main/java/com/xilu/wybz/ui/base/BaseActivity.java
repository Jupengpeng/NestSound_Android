package com.xilu.wybz.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.common.message.Log;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.WelActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.login.ForgetPwdActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.login.RegisterActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.main.SongablumMoreActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.PhoneInfoUtil;
import com.xilu.wybz.utils.StatusBarUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.AnimImageView;
import com.xilu.wybz.view.SystemBarTintManager;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by June on 8/9/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutRes();

    protected boolean isChenjin;
    protected Context context;
    boolean isHomeActivity;
    protected AnimImageView animImageView;
    protected List<Integer> resourceIdList;
    protected ImageView ivLoading;
    protected MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        adaptTheme(true);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        isChenjin = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        isHomeActivity = this instanceof MainActivity || this instanceof FindActivity
                || this instanceof MsgActivity || this instanceof MineActivity;
        PushAgent.getInstance(context).onAppStart();
    }

    //开启透明状态栏目
    protected void adaptTheme(boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (this instanceof PlayAudioActivity || this instanceof WelActivity) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                } else if (this instanceof MainTabActivity || this instanceof UserInfoActivity) {
                    window.setStatusBarColor(getResources().getColor(R.color.main_theme_color));
                } else if (this instanceof LoginActivity || this instanceof RegisterActivity || this instanceof ForgetPwdActivity) {
                    window.setStatusBarColor(getResources().getColor(R.color.login_bg_color));
                }
            }
            if (!(this instanceof PlayAudioActivity || this instanceof LoginActivity
                    || this instanceof RegisterActivity || this instanceof ForgetPwdActivity
                    || this instanceof SongAblumActivity)) {
                StatusBarUtil.StatusBarLightMode(this);
            }
        }
    }

    protected void showMsg(String msg) {
        ToastUtils.toast(context, msg);
    }

    protected void showNoNetMsg() {
        ToastUtils.toast(context, "网络无法连接");
    }

    protected void showNetErrorMsg() {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            showNoNetMsg();
        }
    }

//    public void setStatusColor(int color) {
//        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
//        systemBarTintManager.setStatusBarTintEnabled(true);
//        systemBarTintManager.setStatusBarTintColor(color);
//    }
//
//    protected void setStatusRes(int res) {
//        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
//        systemBarTintManager.setStatusBarTintEnabled(true);
//        systemBarTintManager.setStatusBarTintResource(res);
//    }

    protected void loadImage(String url, SimpleDraweeView mDraweeView) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setCallerContext(null)
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .setOldController(mDraweeView.getController())
                .build();
        mDraweeView.setController(controller);
    }

    protected void loadImage(String picUrl, ImageView imageView) {
        ZnImageLoader.getInstance().displayImage(picUrl, ZnImageLoader.getInstance().options, imageView);
    }

    /*
    * 通过Class跳转界面
    **/
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(intent);
        if (cls == LoginActivity.class) {
            overridePendingTransition(R.anim.activity_open, 0);
        }
    }

    protected void showLoading(View ll_loading) {
        if (resourceIdList == null) {
            resourceIdList = new ArrayList<>();
            resourceIdList.add(R.drawable.loading_1);
            resourceIdList.add(R.drawable.loading_2);
            resourceIdList.add(R.drawable.loading_3);
            resourceIdList.add(R.drawable.loading_4);
        }
        ivLoading = (ImageView) ll_loading.findViewById(R.id.iv_loading_anim);
        if (animImageView == null)
            animImageView = new AnimImageView();
        animImageView.setAnimation(ivLoading, resourceIdList);
        animImageView.start(true, 120);
        ll_loading.setVisibility(View.VISIBLE);
    }

    protected void disMissLoading(View ll_loading) {
        if (ll_loading != null) {
            ll_loading.setVisibility(View.GONE);
            animImageView.stop();
        }
    }

    protected void showPd(String msg) {
        showIndeterminateProgressDialog(msg);
    }

    protected void cancelPd() {
        if (materialDialog != null && materialDialog.isShowing())
            materialDialog.dismiss();
    }

    protected void showIndeterminateProgressDialog(String msg) {
        materialDialog = new MaterialDialog.Builder(this)
                .content(msg)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
