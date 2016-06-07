package com.xilu.wybz.ui.mine;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MineAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.StickyNavLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/6/2.
 */
public class MineActivity extends ToolbarActivity {
    @Bind(R.id.iv_head)
    SimpleDraweeView ivHead;
    @Bind(R.id.user_tv_name)
    TextView userTvName;
    @Bind(R.id.user_tv_info)
    TextView userTvInfo;
    @Bind(R.id.user_follownum)
    TextView userFollownum;
    @Bind(R.id.user_fansnum)
    TextView userFansnum;
    @Bind(R.id.stickynav_layout)
    StickyNavLayout stickynavLayout;
    @Bind(R.id.id_stickynavlayout_viewpager)
    IndexViewPager container;
    boolean firstLoadUserInfo;
    int currentIndex;
    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;
    private MineAdapter pagerAdapter;
    private List<LinearLayout> tabs;
    public boolean isFirst;
    @Override
    public boolean canBack() {
        return false;
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }

    public void initData() {
        if (isFirst) return;
        else isFirst = true;
        EventBus.getDefault().register(this);
        setLocalUserInfo(PrefsUtil.getUserInfo(this));
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.getScreenH(context)-DensityUtil.dip2px(context,102+40)));
        stickynavLayout.setOnScrollSizeChangeListener(new StickyNavLayout.OnScrollSizeChangeListener() {
            @Override
            public void onScrollY(int y) {
                if (y >= 0 && y <= 300) {
                    float alpha = y / 300f;
                    mToolbar.setAlpha(alpha);
                } else if (y > 300) {
                    mToolbar.setAlpha(1);
                }
            }
        });
        tabs = new ArrayList<>();
        llMyrecord.setSelected(true);
        tabs.add(llMyrecord);
        tabs.add(llMysong);
        tabs.add(llMylyrics);
        tabs.add(llMyfav);
        pagerAdapter = new MineAdapter(context, getSupportFragmentManager(), PrefsUtil.getUserId(context), PrefsUtil.getUserInfo(context).name);
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(tabs.size());
        container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                changeTabColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            localUserBean.fansnum = userBean.fansnum;
            localUserBean.gznum = userBean.gznum;
            if (userBean.userid > 0) localUserBean.userid = userBean.userid;
            if (StringUtil.isNotBlank(userBean.nickname)) localUserBean.name = userBean.nickname;
            if (StringUtil.isNotBlank(userBean.signature)) localUserBean.descr = userBean.signature;
            if (StringUtil.isNotBlank(userBean.headurl)) localUserBean.headurl = userBean.headurl;
            PrefsUtil.saveUserInfo(context, localUserBean);
            //更新本地我的信息
            setLocalUserInfo(userBean);
            firstLoadUserInfo = true;
        }
    }

    public void setLocalUserInfo(UserBean userBean) {
        loadImage(userBean.headurl, ivHead);
        userFansnum.setText("粉丝:  " + NumberUtil.format(userBean.fansnum));
        userFollownum.setText("关注:  " + NumberUtil.format(userBean.gznum));
        if (StringUtil.isNotBlank(userBean.name)) userTvName.setText(userBean.name);
        if (StringUtil.isNotBlank(userBean.descr)) userTvInfo.setText(userBean.descr);
        if (StringUtil.isNotBlank(userBean.name)) setTitle(userBean.name);
    }

    @OnClick({R.id.user_fansnum, R.id.user_follownum, R.id.ll_myrecord, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav, R.id.rl_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_follownum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.user_fansnum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.ll_myrecord:
                if (currentIndex == 0) {
                    return;
                } else {
                    currentIndex = 0;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mysong:
                if (currentIndex == 1) {
                    return;
                } else {
                    currentIndex = 1;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mylyrics:
                if (currentIndex == 2) {
                    return;
                } else {
                    currentIndex = 2;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_myfav:
                if (currentIndex == 3) {
                    return;
                } else {
                    currentIndex = 3;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.rl_setting:
                startActivity(SettingActivity.class);
                break;
        }
    }

    //在修改个人资料页面发送过来的
    public void onEventMainThread(Event.UpdateUserInfo event) {
        UserBean userBean = PrefsUtil.getUserInfo(context);
        setLocalUserInfo(userBean);
    }

    //在灵感记录的列表 发送过来的
    public void onEventMainThread(Event.UpdataUserBean event) {
        if (event.getType() == 1) {
            setUserInfo(event.getUserBean());
        }
    }

    //灵感记录 歌曲  歌词 发布成功 更新列表数据
    public void onEventMainThread(Event.UpdataWorksList event) {
        WorksData worksData = event.getWorksData();
        int type = event.getType();
        int position = event.getPosition();
        if(event.getChange()==0)
            (pagerAdapter.getFragment(type)).addItem(worksData);
        else
            (pagerAdapter.getFragment(type)).removeItem(position);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
