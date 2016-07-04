package com.xilu.wybz.ui.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.lyrics.ShareActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.view.SystemBarHelper;
import com.xilu.wybz.view.dialog.LoadingDialog;

import butterknife.ButterKnife;

public abstract class ToolbarActivity extends BaseActivity {

    protected RelativeLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppBar = (RelativeLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isHomeActivity || this instanceof ShareActivity || this instanceof UserInfoActivity || this instanceof PlayAudioActivity || this instanceof SongAblumActivity) {
            SystemBarHelper.setHeightAndPadding(this, mAppBar);
        }
        View view = findViewById(R.id.view);
        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("No toolbar");
        }
        if (this instanceof PlayAudioActivity) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTitleAppearance);
            mToolbar.setSubtitleTextAppearance(context, R.style.ToolbarSubtitleAppearance);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTitleAppearance2);
        }
        if (this instanceof FindActivity || this instanceof MsgActivity || this instanceof MainActivity) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.main_theme_color));
            view.setVisibility(View.GONE);
        } else {
            setTitle("");
        }
        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean canBack() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void setAppBarAlpha(float alpha) {
        mAppBar.setAlpha(alpha);
    }

    protected void hideOrShowToolbar() {
        mAppBar.animate()
                .translationY(mIsHidden ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }


    protected LoadingDialog loading;

    public void showLoading(){
        if (loading == null){
            loading = new LoadingDialog(this);
            loading.setOnKeyCancelListener(new LoadingDialog.OnKeyCancelListener() {
                @Override
                public void onCancel() {
                    onLoadingCancel();
                }
            });
        }
        if (!loading.isShowing()){
            loading.show();
        }
    }
    public void cancelLoading(){
        if (loading == null){
            return;
        }
        if (loading.isShowing()){
            loading.cancel();
        }
    }

    public void onLoadingCancel(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading!=null && loading.isShowing())  {
            loading.cancel();
        }
        loading = null;
        ButterKnife.unbind(this);
    }
}