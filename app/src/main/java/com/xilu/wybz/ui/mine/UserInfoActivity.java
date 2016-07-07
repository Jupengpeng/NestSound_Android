package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MineAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.OnlyFollowPresenter;
import com.xilu.wybz.ui.IView.IOnlyFollowView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.StickyNavLayout;
import com.xilu.wybz.view.SystemBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/6/2.
 */
public class UserInfoActivity extends ToolbarActivity implements IOnlyFollowView{
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
    ViewPager container;
    int currentIndex;
    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;
    int userId;
    String userName;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    private List<LinearLayout> tabs;
    private boolean isFirst;
    private int isFocus = -1;
    private int[] followIcon = new int[]{R.drawable.ic_user_follow, R.drawable.ic_user_followed, R.drawable.ic_user_each_follow};
    MineAdapter pagerAdapter;


    OnlyFollowPresenter presenter;
    public static void ToUserInfoActivity(Context context, int userId, String userName) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OnlyFollowPresenter(context,this);

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getInt("userId");
            userName = bundle.getString("userName");
        } else {
            finish();
        }
        UserBean userData = PrefsUtil.getUserInfo(context,userId);
        if(userData!=null){
            setUserData(userData);
        }
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.getScreenH(context)-DensityUtil.dip2px(context,102+40)));
        ivSetting.setImageResource(R.drawable.ic_user_follow);
        EventBus.getDefault().register(this);
        tabs = new ArrayList<>();
        llMyrecord.setVisibility(View.GONE);
        llMysong.setSelected(true);
        tabs.add(llMysong);
        tabs.add(llMylyrics);
        tabs.add(llMyfav);
        pagerAdapter = new MineAdapter(context, getSupportFragmentManager(), userId, userName);
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(tabs.size());
        mToolbar.setAlpha(1);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbar.setTitleTextColor(Color.TRANSPARENT);
        stickynavLayout.setOnStickStateChangeListener(new StickyNavLayout.onStickStateChangeListener() {
            @Override
            public void isStick(boolean isStick) {
                if(!isStick) {
                    for (int i = 0; i < 3; i++) {
                        if (i != container.getCurrentItem()) {
                            pagerAdapter.getFragment(i).moveToFirst();
                        }
                    }
                }
            }
            @Override
            public void scrollPercent(float percent) {
                mToolbar.setBackgroundColor(Color.argb((int) (percent*255),0xFF,0xD7,0x05));
                mToolbar.setTitleTextColor(Color.argb((int) (percent*255),0x18,0x18,0x18));
                SystemBarHelper.tintStatusBar(UserInfoActivity.this, Color.argb((int) (percent*255),0xFF,0xD7,0x05));
            }
        });
        container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                },120);
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
        if (isFirst) return;
        isFirst = true;
        PrefsUtil.saveUserInfo(context,userId,userBean);
        setUserData(userBean);
    }
    public void setUserData(UserBean userBean){
        if(StringUtil.isNotBlank(userBean.headurl)&&!userBean.headurl.equals("http://pic.yinchao.cn/uploadfiles/2015/09/14/201509141121211442200881.png")) {
            int headWidth = DensityUtil.dip2px(context,92);
            loadImage(MyCommon.getImageUrl(userBean.headurl,headWidth,headWidth), ivHead);
        }
        if (StringUtil.isNotBlank(userBean.nickname)) userTvName.setText(userBean.nickname);
        if (StringUtil.isNotBlank(userBean.signature)) userTvInfo.setText(userBean.signature);
        if (StringUtil.isNotBlank(userBean.nickname)) mToolbar.setTitle(userBean.nickname);
        userFansnum.setText("粉丝:  " + NumberUtil.format(userBean.fansnum));
        userFollownum.setText("关注:  " + NumberUtil.format(userBean.gznum));
        ivSetting.setImageResource(followIcon[userBean.isFocus]);
        isFocus = userBean.isFocus;
    }
    @OnClick({R.id.user_fansnum, R.id.user_follownum, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav, R.id.rl_setting})
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
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mylyrics:
                if (currentIndex == 1) {
                    return;
                } else {
                    currentIndex = 1;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_myfav:
                if (currentIndex == 2) {
                    return;
                } else {
                    currentIndex = 2;
                }
                container.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.rl_setting:
                FollowUser();
                break;
        }
    }


    private void FollowUser() {
        if (isFocus > -1) {
            showPd("正在请求网络，请稍候");
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
        if (isFocus>=0&&isFocus<=2){
            ivSetting.setImageResource(followIcon[isFocus]);
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

    //在我的歌曲的列表 发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserBean event) {
        if (event.getType() == 2) {
            setUserInfo(event.getUserBean());
        }
    }

    @Override
    protected void onDestroy() {
        cancelPd();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
