package com.xilu.wybz.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.SplashActivity;
import com.xilu.wybz.ui.WelActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.login.PasswordActivity;
import com.xilu.wybz.ui.login.RegisterActivity;
import com.xilu.wybz.ui.lyrics.LyricsPosterActivity;
import com.xilu.wybz.ui.lyrics.ShareActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.mine.NewUserInfoActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.AnimImageView;
import com.xilu.wybz.view.SystemBarHelper;
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
    protected boolean isDestroy = false;
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
        isDestroy = false;
        adaptTheme(true);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        isChenjin = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        isHomeActivity = this instanceof MainActivity || this instanceof FindActivity
                || this instanceof MsgActivity || this instanceof MineActivity;
        PushAgent.getInstance(context).onAppStart();
    }

    //开启透明状态栏目
    protected void adaptTheme(boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue) {
            if (!(this instanceof PlayAudioActivity || this instanceof LoginActivity || this instanceof LyricsPosterActivity
                    || this instanceof RegisterActivity || this instanceof PasswordActivity
                    || this instanceof SongAblumActivity)) {
                    SystemBarHelper.setStatusBarDarkMode(this);
            }
            if (this instanceof WelActivity || this instanceof PlayAudioActivity || this instanceof LyricsPosterActivity
                    || this instanceof SongAblumActivity || this instanceof ShareActivity
                    || this instanceof SplashActivity ||
                    this instanceof NewUserInfoActivity || this instanceof MainTabActivity) {
                SystemBarHelper.immersiveStatusBar(this);
                SystemBarHelper.tintStatusBar(this, Color.argb(0, 0xFF, 0xD7, 0x05));
            } else if (this instanceof LoginActivity || this instanceof RegisterActivity || this instanceof PasswordActivity) {
//                SystemBarHelper.immersiveStatusBar(this);
            }
        }
    }

    protected void showMsg(String msg) {
        if (isDestroy){
            return;
        }
        ToastUtils.toast(this, msg);
    }
//    protected void showLocationMsg(String msg, ViewGroup viewGroup) {
//        ToastManager.toastLocation(this, msg, viewGroup);
//    }
    protected void showNoNetMsg() {
        if (isDestroy){
            return;
        }
        ToastUtils.toast(this, "网络无法连接");
    }

    protected void showNetErrorMsg() {
        if (isDestroy){
            return;
        }
        if (NetWorkUtil.isNetworkAvailable(context)) {
            ToastUtils.toast(this,"服务器出小差了！");
        }else{
            ToastUtils.toast(this,"网络无法连接！");
        }
    }

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
    protected void loadHeadImage(String picUrl, ImageView imageView) {
        ZnImageLoader.getInstance().displayImage(picUrl, ZnImageLoader.getInstance().headOptions, imageView);
    }
    /*
    * 通过Class跳转界面
    **/
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(intent);
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
            materialDialog.cancel();
    }


    DialogInterface.OnCancelListener listener;

    public void setOnCancelListener(DialogInterface.OnCancelListener listener){
        this.listener = listener;
    }

    protected void showIndeterminateProgressDialog(String msg) {
        materialDialog = new MaterialDialog.Builder(this)
                .content(msg)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (listener != null){
                            listener.onCancel(dialog);
                        }
                    }
                })
                .show();
    }

    private static String HOME_TAG = "home-activity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(HOME_TAG,this.getClass().getName()+":onResume");
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(HOME_TAG,this.getClass().getName()+":onPause");
        MobclickAgent.onPause(this);
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i(HOME_TAG,this.getClass().getName()+":onDestroy");
        isDestroy = true;
    }
}
