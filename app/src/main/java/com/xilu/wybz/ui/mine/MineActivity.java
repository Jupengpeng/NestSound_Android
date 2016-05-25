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
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.mine.view.UserBaseView;
import com.xilu.wybz.ui.mine.view.UserCollectionView;
import com.xilu.wybz.ui.mine.view.UserInspirationView;
import com.xilu.wybz.ui.mine.view.UserLyricView;
import com.xilu.wybz.ui.mine.view.UserSongView;
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

    private UserPresenter mUserPresenter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserPresenter = new UserPresenter(context, this);
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

        mUserInspirationView = new UserInspirationView(this);
        mUserSongView = new UserSongView(this);
        mUserLyricView = new UserLyricView(this);
        mUserCollectionView = new UserCollectionView(this);


        contentLayout.setMinimumHeight(DensityUtil.getScreenH(context) - DensityUtil.getStatusBarHeight(context) - DensityUtil.dip2px(context, 148));


        contentLayout.addView(mUserInspirationView);
        contentLayout.addView(mUserSongView);
        contentLayout.addView(mUserLyricView);
        contentLayout.addView(mUserCollectionView);


        setCurrentPageView(llMyrecord, 0);
        contentLayout.setVisibility(View.VISIBLE);

        setUserInfo(PrefsUtil.getUserInfo(this));

    }

    @OnClick(R.id.iv_setting)
    public void onClickSetting() {
        startActivity(LoginActivity.class);
//        mUserPresenter.getInspirationList("147",1);
    }

    @OnClick(R.id.iv_draft)
    public void onClickDraft() {
        startActivity(DraftActivity.class);
    }

    @OnClick(R.id.ll_myfans)
    public void onClickFans() {
        FollowAndFansActivity.toFollowAndFansActivity(this, KeySet.TYPE_FANS_ACT);
    }

    @OnClick(R.id.ll_myfollow)
    public void onClickFollow() {
        FollowAndFansActivity.toFollowAndFansActivity(this, KeySet.TYPE_FOLLOW_ACT);
    }

    @OnClick(R.id.iv_head)
    public void onClickHeadImage() {
        mUserPresenter.getInspirationList("147", 1);
    }


    @Override
    public void setUserInfo(UserBean userBean){
        userBean = checkByDefault(userBean);

        loadImage(userBean.headurl,ivHead);
        userTvName.setText(userBean.name);
        userTvInfo.setText(userBean.descr);
    }

    @Override
    public void setFollowNumber(int number) {

    }

    @Override
    public void setFansNumber(int number) {

    }

    private UserBean checkByDefault(UserBean userBean){
        if (StringUtil.isBlank(userBean.name)){
            userBean.name = "音巢音乐";
        }
        if (StringUtil.isBlank(userBean.descr)){
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
