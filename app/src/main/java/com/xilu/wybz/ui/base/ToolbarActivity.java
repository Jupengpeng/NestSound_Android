package com.xilu.wybz.ui.base;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
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
        View view =findViewById(R.id.view);
        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("No toolbar");
        }
        if(this instanceof PlayAudioActivity) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTitleAppearance);
            mToolbar.setSubtitleTextAppearance(context, R.style.ToolbarSubtitleAppearance);
        }else{
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTitleAppearance2);
        }
        if(this instanceof MainTabActivity){
            mToolbar.setBackgroundColor(getResources().getColor(R.color.main_theme_color));
            view.setVisibility(View.GONE);
        }
        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar.setElevation(10.6f);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}