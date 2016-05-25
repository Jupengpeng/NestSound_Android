package com.xilu.wybz.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.login.PasswordActivity;
import com.xilu.wybz.ui.login.RegisterActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.SplashActivity;
import com.xilu.wybz.ui.WelActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.PhoneInfoUtil;
import com.xilu.wybz.utils.PrefsUtil;
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

    protected String userId;
    protected boolean isChenjin;
    protected Context context;
    boolean isHomeActivity;
    protected boolean isLogin;
    protected AnimImageView animImageView;
    protected List<Integer> resourceIdList;
    protected ImageView ivLoading;
    protected MaterialDialog materialDialog;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Fresco.initialize(this);
        adaptTheme(true);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        isChenjin = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        isHomeActivity = this instanceof MainActivity || this instanceof FindActivity
                || this instanceof MsgActivity || this instanceof MineActivity;
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = PrefsUtil.getUserId(context);
        isLogin = !userId.equals("0");
    }

    //适配不同手机以及sdk_int的状态栏
    private void initStatusBar() {
        isChenjin = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isChenjin && !Build.MANUFACTURER.toUpperCase().equals("OPPO")) {
            if (PhoneInfoUtil.isMIUI()) {
                PhoneInfoUtil.MiuiCj(this, !(this instanceof PlayAudioActivity));
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if (!(this instanceof PlayAudioActivity ||this instanceof SongAblumActivity || this instanceof SplashActivity || this instanceof WelActivity) && !isHomeActivity) {
                if (this instanceof LoginActivity || this instanceof PasswordActivity
                        || this instanceof RegisterActivity) {
                    setStatusColor(0xff3a3937);
                } else if (this instanceof MainTabActivity) {
                    setStatusColor(getResources().getColor(R.color.main_theme_color));
                } else {
                    if (Build.MANUFACTURER.toUpperCase().equals("MEIZU") || PhoneInfoUtil.isMIUI()) {
                        setStatusColor(0xffffffff);
                    } else {
                        setStatusRes(R.drawable.bg_statusbar);
                    }
                }
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void adaptTheme(boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (PhoneInfoUtil.isMIUI()) {
                PhoneInfoUtil.MiuiCj(this, !(this instanceof PlayAudioActivity));
            }
            if (this instanceof MainTabActivity) {
                setStatusColor(getResources().getColor(R.color.main_theme_color));
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

    public void setStatusColor(int color) {
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintColor(color);
    }

    protected void setStatusRes(int res) {
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(res);
    }

    protected void loadImage(String url, SimpleDraweeView mDraweeView) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .setResizeOptions(
                                new ResizeOptions(mDraweeView.getLayoutParams().width, mDraweeView.getLayoutParams().height))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }
    protected void loadImage(String picUrl, ImageView imageView){
        ZnImageLoader.getInstance().displayImage(picUrl,ZnImageLoader.getInstance().headOptions,imageView);
    }
    /*
    * 通过Class跳转界面
    **/
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivity(intent);
//        overridePendingTransition(R.anim.right_in_anim, R.anim.left_out_anim);
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
