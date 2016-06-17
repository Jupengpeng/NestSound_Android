package com.xilu.wybz.ui.mine;

import android.content.Context;
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
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.StickyNavLayout;
import com.xilu.wybz.view.SystemBarHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * Created by hujunwei on 16/6/2.
 */
public class UserInfoActivity extends ToolbarActivity {
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
                mToolbar.setAlpha(percent);
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
                        if(position>0){
                            WorksDataFragment worksDataFragment = pagerAdapter.getFragment(position);
                            worksDataFragment.loadData();
                        }
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
        if(StringUtil.isNotBlank(userBean.headurl)&&!userBean.headurl.equals("http://pic.yinchao.cn/uploadfiles/2015/09/14/201509141121211442200881.png")) {
            int headWidth = DensityUtil.dip2px(context,92);
            loadImage(MyCommon.getImageUrl(userBean.headurl,headWidth,headWidth), ivHead);
        }
        if (StringUtil.isNotBlank(userBean.nickname)) userTvName.setText(userBean.nickname);
        if (StringUtil.isNotBlank(userBean.signature)) userTvInfo.setText(userBean.signature);
        if (StringUtil.isNotBlank(userBean.nickname)) setTitle(userBean.nickname);
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
            HttpUtils httpUtils = new HttpUtils(context);
            Map<String, String> params = new HashMap<>();
            params.put("userid", userId + "");
            params.put("fansid", PrefsUtil.getUserId(context) + "");
            httpUtils.post(MyHttpClient.getFanFocusList(), params, new MyStringCallback() {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    isFocus = 1 - isFocus;
                    EventBus.getDefault().post(new Event.UpdateFollowNumEvent(1-isFocus, 0));
                    showMsg(isFocus==0?"取消关注成功！":"关注成功！");
                    ivSetting.setImageResource(followIcon[isFocus]);
                }

                @Override
                public void onError(Call call, Exception e) {
                    super.onError(call, e);
                }
            });
        }
    }

    //在灵感记录的列表 发送过来的
    public void onEventMainThread(Event.UpdataUserBean event) {
        if (event.getType() == 2) {
            setUserInfo(event.getUserBean());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
