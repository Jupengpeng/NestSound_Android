package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.OtherCenterListAdapter;
import com.xilu.wybz.adapter.UserCenterListAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.DownPicPresenter;
import com.xilu.wybz.presenter.OnlyFollowPresenter;
import com.xilu.wybz.presenter.OtherCenterListPresenter;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.ui.IView.IOnlyFollowView;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.fragment.OtherWorksDataFragment;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.SystemBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/6/2.
 */
public class OtherUserCenterActivity extends BaseActivity implements IOnlyFollowView,ILoadPicView,IUserView {
    @Bind(R.id.iv_blur_view)
    ImageView ivBlurView;
    @Bind((R.id.ll_mine_info))
    LinearLayout llMineInfo;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.viewpager)
    IndexViewPager mViewpager;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_nickname)
    TextView mNickname;
    @Bind(R.id.tv_desc)
    TextView mDesc;
    @Bind(R.id.tv_fans_num)
    TextView mFansNum;
    @Bind(R.id.tv_follow_num)
    TextView mFollowNum;
    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;
    @Bind(R.id.tv_song_num)
    TextView tvSongNum;
    @Bind(R.id.tv_lyrics_num)
    TextView tvLyricsNum;
    @Bind(R.id.tv_fov_num)
    TextView tvFovNum;
    @Bind(R.id.tv_record_num)
    TextView tvRecordNum;
    @Bind(R.id.iv_headpic)
    ImageView ivHeadPic;
    private int currentIndex;
    private OtherCenterListAdapter pagerAdapter;
    private List<LinearLayout> tabs;
    public OnlyFollowPresenter presenter;
    private int isFocus = -1;
    private MenuItem menuFollowItem;
    private int[] followIcon = new int[]{R.drawable.ic_toolbar_follow, R.drawable.ic_toolbar_followed, R.drawable.ic_toolbar_each_follow};
    int userId;
    String userName;
    UserInfoBean userInfoBean;
    DownPicPresenter downPicPresenter;
    UserPresenter userPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_new_mine;
    }
    public static void toUserInfoActivity(Context context, int userId, String userName) {
        Intent intent = new Intent(context, OtherUserCenterActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OnlyFollowPresenter(context,this);
        userPresenter = new UserPresenter(context,this);
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("userId", userId);
        outState.putString("userName", userName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userId = savedInstanceState.getInt("userId");
        userName = savedInstanceState.getString("userName");
    }

    public void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);
        SystemBarHelper.immersiveStatusBar(this, 0);
        EventBus.getDefault().register(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getInt("userId");
            userName = bundle.getString("userName");
        } else {
            finish();
        }
        userPresenter.getUserInfo(userId);
        UserInfoBean userInfoBean = PrefsUtil.getUserInfoBean(context, userId);
        setUserInfoBean(userInfoBean);
        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                boolean showTitle = mCollapsingToolbar.getHeight() + verticalOffset <= mToolbar.getHeight() * 2;
                llMineInfo.setVisibility(showTitle ? View.GONE : View.VISIBLE);
                int height = mCollapsingToolbar.getHeight() - mToolbar.getHeight();
                float alpha = Math.abs(verticalOffset) / (height + 0.0f);
                Log.e("alpha", alpha + "");
                ivBlurView.setAlpha(alpha);
            }
        });

        tabs = new ArrayList<>();
        llMyrecord.setVisibility(View.GONE);
        llMysong.setSelected(true);
        tabs.add(llMysong);
        tabs.add(llMylyrics);
        tabs.add(llMyfav);
        pagerAdapter = new OtherCenterListAdapter(context, getSupportFragmentManager(), userId, userName);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(tabs.size());

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                changeTabColor();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OtherWorksDataFragment worksDataFragment = pagerAdapter.getFragment(position);
                        worksDataFragment.loadData();
                    }
                }, 120);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        if(userInfoBean!=null){
            mFansNum.setText(NumberUtil.format(userInfoBean.fansnum));
            mFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
            tvSongNum.setText(NumberUtil.format(userInfoBean.worknum));
            tvLyricsNum.setText(NumberUtil.format(userInfoBean.lyricsnum));
            tvFovNum.setText(NumberUtil.format(userInfoBean.fovnum));
            tvRecordNum.setText(NumberUtil.format(userInfoBean.inspirenum));
            setUserData(userInfoBean.user);
        }
    }


    public void setUserData(UserBean userBean){
        if(StringUtils.isNotBlank(userBean.headurl)&&!userBean.headurl.equals(MyCommon.defult_head)) {
            int headWidth = DensityUtil.dip2px(context,92);
            loadImage(MyCommon.getImageUrl(userBean.headurl,headWidth,headWidth), ivHead);
        }
        if (StringUtils.isNotBlank(userBean.bgpic)) {
            String path = FileDir.mineBgPic + MD5Util.getMD5String(userBean.bgpic) + ".jpg";
            if (new File(path).exists()) {
                if(loadHeadPic(path)){
                    return;
                }
            }
            if (downPicPresenter == null) {
                downPicPresenter = new DownPicPresenter(context, OtherUserCenterActivity.this);
                downPicPresenter.downLoadPic(userBean.bgpic, path);
            }
        } else {
            loadDefaultPic();
        }
        if (StringUtils.isNotBlank(userBean.nickname)) mNickname.setText(userBean.nickname);
        if (StringUtils.isNotBlank(userBean.signature))mDesc.setText(userBean.signature);

    }

    public boolean loadHeadPic(String bgPicPath) {
        Bitmap bitmap = BitmapUtils.getSDCardImg(bgPicPath);
        if (bitmap == null) return false;
        ivHeadPic.setImageBitmap(bitmap);
        Bitmap bmp = NativeStackBlur.process(bitmap, 200);
        ivBlurView.setImageBitmap(bmp);
        return true;
    }
    public void loadDefaultPic() {
        ivHeadPic.setImageResource(R.mipmap.bg_top_mine);
        Bitmap bmp = NativeStackBlur.process(BitmapUtils.ReadBitmapById(this, R.mipmap.bg_top_mine), 200);
        ivBlurView.setImageBitmap(bmp);
    }
    public void updateUserFansNum(UserInfoBean userBean) {
        userInfoBean = userBean;
        mFansNum.setText(NumberUtil.format(userBean.fansnum));
        mFollowNum.setText(NumberUtil.format(userBean.gznum));

        if(menuFollowItem!=null)
            menuFollowItem.setIcon(followIcon[userBean.isFocus]);
        isFocus = userBean.isFocus;

        tvSongNum.setText(NumberUtil.format(userBean.worknum));
        tvLyricsNum.setText(NumberUtil.format(userBean.lyricsnum));
        tvFovNum.setText(NumberUtil.format(userBean.fovnum));
        tvRecordNum.setText(NumberUtil.format(userBean.inspirenum));
    }
    private void changeTabColor() {
        for (int i = 0; i < tabs.size(); i++) {
            LinearLayout tab = tabs.get(i);
            tab.setSelected(currentIndex == i);
        }
    }

    @OnClick({R.id.user_fansnum, R.id.user_follownum, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_follownum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT, userId);
                break;
            case R.id.user_fansnum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT, userId);
                break;
            case R.id.ll_mysong:
                if (currentIndex == 0) {
                    return;
                } else {
                    currentIndex = 0;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mylyrics:
                if (currentIndex == 1) {
                    return;
                } else {
                    currentIndex = 1;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_myfav:
                if (currentIndex == 2) {
                    return;
                } else {
                    currentIndex = 2;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting,menu);
        menuFollowItem = menu.findItem(R.id.menu_setting);
        menuFollowItem.setIcon(R.drawable.ic_user_follow);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_setting){
            FollowUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void FollowUser() {
        if (isFocus > -1) {
            showPd(isFocus==0?"正在关注中，请稍候...":"正在取消关注，请稍候...");
            setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (presenter != null)
                        presenter.cancel();
                }
            });
            presenter.follow(userId);
        }
    }
    @Override
    public void followSuccess(String message) {
        cancelPd();
        isFocus = OnlyFollowPresenter.paraseStatuByString(message);
        if (isFocus>=0&&isFocus<=2&&menuFollowItem!=null){
            menuFollowItem.setIcon(followIcon[isFocus]);
        }
        EventBus.getDefault().post(new Event.UpdateFollowNumEvent(isFocus,0));
    }

    @Override
    public void followFailed(String message) {
        cancelPd();
    }

    @Override
    public void initView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserInfoBean event) {
        if (event.getType() == 2) {
            updateUserFansNum(event.getUserBean());
        }
    }

    @Override
    protected void onDestroy() {
        cancelPd();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPic(String path) {
        loadHeadPic(path);
    }

    @Override
    public void downPicFail() {
        loadDefaultPic();
    }
}
