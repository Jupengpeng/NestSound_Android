package com.xilu.wybz.ui.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MineAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.SystemBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/6/2.
 */
public class MineActivity extends BaseActivity {
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
    private boolean firstLoadUserInfo;
    private int currentIndex;
    private MineAdapter pagerAdapter;
    private List<LinearLayout> tabs;
    public boolean isFirst;
    public UserInfoBean userInfoBean;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_new_mine;
    }

    public void initData() {
        if (isFirst) return;
        else isFirst = true;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);
        EventBus.getDefault().register(this);
        setLocalUserInfo(PrefsUtil.getUserInfo(this));
        Bitmap bmp = NativeStackBlur.process(BitmapUtils.ReadBitmapById(this, R.mipmap.bg_top_mine), 200);
        ivBlurView.setImageBitmap(bmp);
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
        llMysong.setSelected(true);
        tabs.add(llMysong);
        tabs.add(llMylyrics);
        tabs.add(llMyfav);
        tabs.add(llMyrecord);
        pagerAdapter = new MineAdapter(context, getSupportFragmentManager(), PrefsUtil.getUserId(context), PrefsUtil.getUserInfo(context).name);
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
                        WorksDataFragment worksDataFragment = pagerAdapter.getFragment(position);
                        worksDataFragment.loadData();
                    }
                }, 120);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginOutEvent event) {
        firstLoadUserInfo = false;
        mNickname.setText("");
        mDesc.setText("");
        ivHead.setImageResource(R.drawable.ic_default_head_252);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        firstLoadUserInfo = false;
        mViewpager.setCurrentItem(0);
        WorksDataFragment worksDataFragment;
        worksDataFragment = pagerAdapter.getFragment(0);
        worksDataFragment.reSet();
        worksDataFragment.loadData();
        worksDataFragment = pagerAdapter.getFragment(1);
        worksDataFragment.reSet();
        worksDataFragment = pagerAdapter.getFragment(2);
        worksDataFragment.reSet();
        worksDataFragment = pagerAdapter.getFragment(3);
        worksDataFragment.reSet();
    }

    private void changeTabColor() {
        for (int i = 0; i < tabs.size(); i++) {
            LinearLayout tab = tabs.get(i);
            tab.setSelected(currentIndex == i);
        }
    }

    public void setUserInfo(UserBean userBean) {
        if (!firstLoadUserInfo) {
            //更新本地数据
            UserBean localUserBean = PrefsUtil.getUserInfo(context);
            if (userBean.userid > 0) localUserBean.userid = userBean.userid;
            if (StringUtil.isNotBlank(userBean.nickname)) localUserBean.name = userBean.nickname;
            if (StringUtil.isNotBlank(userBean.signature)) localUserBean.descr = userBean.signature;
            if (StringUtil.isNotBlank(userBean.headurl)) localUserBean.headurl = userBean.headurl;
            PrefsUtil.saveUserInfo(context, localUserBean);
            //更新本地我的信息
            setLocalUserInfo(localUserBean);
            firstLoadUserInfo = true;
        } else {
            //更新本地数据
            UserBean localUserBean = PrefsUtil.getUserInfo(context);
            if (userBean.userid > 0) localUserBean.userid = userBean.userid;
            if (StringUtil.isNotBlank(userBean.headurl)) localUserBean.headurl = userBean.headurl;
            PrefsUtil.saveUserInfo(context, localUserBean);
            //更新本地我的信息
            setLocalUserInfo(localUserBean);
        }
    }

    public void setLocalUserInfo(UserBean userBean) {
        if (StringUtil.isNotBlank(userBean.headurl) && !userBean.headurl.equals(MyCommon.defult_head)) {
            int headWidth = DensityUtil.dip2px(context, 92);
            loadImage(MyCommon.getImageUrl(userBean.headurl, headWidth, headWidth), ivHead);
        }
        if (StringUtil.isNotBlank(userBean.nickname)) mNickname.setText(userBean.nickname);
        if (StringUtil.isNotBlank(userBean.signature)) mDesc.setText(userBean.signature);
        if (StringUtil.isNotBlank(userBean.nickname)) setTitle(userBean.nickname);
    }

    public void updateUserFansNum(UserInfoBean userBean) {
        userInfoBean = userBean;
        mFansNum.setText(NumberUtil.format(userBean.fansnum));
        mFollowNum.setText(NumberUtil.format(userBean.gznum));
        tvSongNum.setText(NumberUtil.format(userBean.worknum));
        tvLyricsNum.setText(NumberUtil.format(userBean.lyricsnum));
        tvFovNum.setText(NumberUtil.format(userBean.fovnum));
        tvRecordNum.setText(NumberUtil.format(userBean.inspirenum));
    }

    //在修改个人资料页面发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateUserInfo event) {
        UserBean userBean = PrefsUtil.getUserInfo(context);
        setLocalUserInfo(userBean);
    }

    //在灵感记录的列表 发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserBean event) {
        if (event.getType() == 1) {
            setUserInfo(event.getUserBean());
        }
    }
    //在灵感记录的列表 发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserInfoBean event) {
        if (event.getType() == 1) {
            updateUserFansNum(event.getUserBean());
        }
    }

    //修改粉丝数量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateFollowNumEvent event) {
        int type = event.getType();
        if (type == 0) {
            userInfoBean.gznum -= 1;
            if (userInfoBean.gznum < 0) userInfoBean.gznum = 0;
        } else {
            userInfoBean.gznum += 1;
        }
        mFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
    }

    //更新点赞数
//    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.UpdateWorkNum event){
//        (pagerAdapter.getFragment(1)).updateNum(event.getWorksData(),event.getType());
//    }
    //灵感记录 歌曲  歌词 发布成功 更新列表数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataWorksList event) {
        int type = event.getType();
        if (event.getChange() == 0)
            (pagerAdapter.getFragment(type)).updateList();
        else if (event.getChange() == 1)
            (pagerAdapter.getFragment(type)).removeData(event.getWorksData());
        else if (event.getChange() == 2)
            (pagerAdapter.getFragment(type)).updateData(event.getWorksData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.RemoveMySongEvent event) {
        int itemid = event.getItemid();
        (pagerAdapter.getFragment(1)).removeByItemid(itemid);
    }

    @OnClick({R.id.user_fansnum, R.id.user_follownum, R.id.ll_myrecord, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_follownum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.user_fansnum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.ll_myrecord:
                if (currentIndex == 3) {
                    return;
                } else {
                    currentIndex = 3;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
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
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            startActivity(SettingActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
