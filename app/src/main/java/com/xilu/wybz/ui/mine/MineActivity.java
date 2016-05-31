package com.xilu.wybz.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.UserListPresenter;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.mine.view.UserBaseView;
import com.xilu.wybz.ui.mine.view.UserCollectionView;
import com.xilu.wybz.ui.mine.view.UserInspirationView;
import com.xilu.wybz.ui.mine.view.UserLyricView;
import com.xilu.wybz.ui.mine.view.UserSongView;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.TopFloatScrollView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class MineActivity extends BaseActivity implements IUserView {


    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.user_tv_name)
    TextView userTvName;
    @Bind(R.id.user_tv_info)
    TextView userTvInfo;

    @Bind(R.id.user_follownum)
    TextView userFollownum;
    @Bind(R.id.ll_myfollow)
    LinearLayout llMyfollow;

    @Bind(R.id.user_fansnum)
    TextView userFansnum;
    @Bind(R.id.ll_myfans)
    LinearLayout llMyfans;

    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;

    @Bind(R.id.top_float)
    RelativeLayout topFloat;
    @Bind(R.id.user_layout_content)
    FrameLayout contentLayout;
    @Bind(R.id.myScrollView)
    TopFloatScrollView myScrollView;
    @Bind(R.id.tv_center)
    TextView tvCenter;
    @Bind(R.id.iv_draft)
    ImageView ivDraft;
    @Bind(R.id.rl_draft)
    RelativeLayout rlDraft;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.rl_topbar)
    RelativeLayout rlTopbar;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.v_toolbar)

    View vToolbar;
    UserBaseView mCurrentUserView;

    UserInspirationView mUserInspirationView;
    UserSongView mUserSongView;
    UserLyricView mUserLyricView;
    UserCollectionView mUserCollectionView;

    private boolean firstLoadUserInfo;
    private UserPresenter mUserPresenter;
    private int userType = 1;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefsUtil.getUserId(context) > 0) {
            initData();
        }
    }

    public void initData() {
        mUserPresenter = new UserPresenter(context, this, UserPresenter.TYPE_USER_CENTER);
        mUserPresenter.init();
    }

    @Override
    public void initView() {

        myScrollView.initView(vToolbar, tvCenter);

        myScrollView.setScrollBottomListener(new TopFloatScrollView.OnScrollToBottomListener() {
            @Override
            public void scrollBottom() {
                if (mCurrentUserView != null) {
                    mCurrentUserView.onScrollBottom();
                }
            }
        });
        int userId = PrefsUtil.getUserId(context);
        mUserInspirationView = new UserInspirationView(this);
        mUserSongView = new UserSongView(this, userType);
        mUserLyricView = new UserLyricView(this, userType);
        mUserCollectionView = new UserCollectionView(this);


        contentLayout.setMinimumHeight(DensityUtil.getScreenH(context) - DensityUtil.getStatusBarHeight(context) - DensityUtil.dip2px(context, 148));


        contentLayout.addView(mUserInspirationView);
        contentLayout.addView(mUserSongView);
        contentLayout.addView(mUserLyricView);
        contentLayout.addView(mUserCollectionView);


        setCurrentPageView(llMyrecord, 0);
        contentLayout.setVisibility(View.VISIBLE);

        setLocalUserInfo(PrefsUtil.getUserInfo(this));
        mUserSongView.getUserListPresenter().requestListData(userId, 1);
    }

    @OnClick({R.id.iv_setting, R.id.iv_draft, R.id.ll_myfans})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.iv_draft:
                startActivity(DraftActivity.class);
                break;
            case R.id.ll_myfans:
                FollowAndFansActivity.toFollowAndFansActivity(this, KeySet.TYPE_FANS_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.ll_myfollow:
                FollowAndFansActivity.toFollowAndFansActivity(this, KeySet.TYPE_FOLLOW_ACT, PrefsUtil.getUserId(context));
                break;
        }
    }

    @Override
    public void setUserInfo(UserBean userBean) {
        if (!firstLoadUserInfo) {
            //更新本地数据
            UserBean localUserBean = PrefsUtil.getUserInfo(context);
            if (userBean.userid > 0) localUserBean.userid = userBean.userid;
            if (StringUtil.isNotBlank(userBean.nickname)) localUserBean.name = userBean.nickname;
            if (StringUtil.isNotBlank(userBean.signature)) localUserBean.descr = userBean.signature;
            if (StringUtil.isNotBlank(userBean.headurl)) localUserBean.headurl = userBean.headurl;
            PrefsUtil.saveUserInfo(context, localUserBean);
            //更新我的资料
            setLocalUserInfo(userBean);
            firstLoadUserInfo = true;
        }
    }

    public void setLocalUserInfo(UserBean userBean) {
        userBean = checkByDefault(userBean);
        loadImage(userBean.headurl, ivHead);
        userTvName.setText(userBean.name);
        userTvInfo.setText(userBean.descr);
    }

    @Override
    public void setFollowNumber(int number) {

    }

    @Override
    public void setFansNumber(int number) {

    }

    private UserBean checkByDefault(UserBean userBean) {
        if (StringUtil.isBlank(userBean.name)) {
            userBean.name = "音巢音乐";
        }
        if (StringUtil.isBlank(userBean.descr)) {
            userBean.descr = "人人都是作曲家";
        }
        return userBean;
    }

    public void setSeletedTab(View view) {
        llMyrecord.setSelected(false);
        llMysong.setSelected(false);
        llMylyrics.setSelected(false);
        llMyfav.setSelected(false);

        view.setSelected(true);
    }


    public void setContentPage(int id) {
        switch (id) {
            case 0:
                mCurrentUserView = mUserInspirationView;
                break;
            case 1:
                mCurrentUserView = mUserSongView;
                break;
            case 2:
                mCurrentUserView = mUserLyricView;
                break;
            case 3:
                mCurrentUserView = mUserCollectionView;
                break;
        }

        if (!(mCurrentUserView instanceof UserInspirationView)) {
            mUserInspirationView.setVisibility(View.GONE);
        }
        if (!(mCurrentUserView instanceof UserSongView)) {
            mUserSongView.setVisibility(View.GONE);
        }
        if (!(mCurrentUserView instanceof UserLyricView)) {
            mUserLyricView.setVisibility(View.GONE);
        }
        if (!(mCurrentUserView instanceof UserCollectionView)) {
            mUserCollectionView.setVisibility(View.GONE);
        }

        mCurrentUserView.setVisibility(View.VISIBLE);
    }


    public void setCurrentPageView(View view, int id) {

        setSeletedTab(view);
        setContentPage(id);

    }


    @OnClick({R.id.ll_myrecord, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_myrecord:
                setCurrentPageView(view, 0);
                break;
            case R.id.ll_mysong:
                setCurrentPageView(view, 1);
                break;
            case R.id.ll_mylyrics:
                setCurrentPageView(view, 2);
                break;
            case R.id.ll_myfav:
                setCurrentPageView(view, 3);
                break;
        }
    }

}
