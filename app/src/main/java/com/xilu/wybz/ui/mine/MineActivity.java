package com.xilu.wybz.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.mine.view.UserBaseView;
import com.xilu.wybz.ui.mine.view.UserCollectionView;
import com.xilu.wybz.ui.mine.view.UserInspirationView;
import com.xilu.wybz.ui.mine.view.UserLyricView;
import com.xilu.wybz.ui.mine.view.UserSongView;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.TopFloatScrollView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class MineActivity extends BaseActivity implements IUserView {
    @Bind(R.id.id_bg)
    ImageView idBg;
    @Bind(R.id.user_layout_top)
    RelativeLayout userLayoutTop;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.user_tv_name)
    TextView userTvName;
    @Bind(R.id.user_tv_info)
    TextView userTvInfo;
    @Bind(R.id.user_fans)
    TextView userFans;
    @Bind(R.id.user_follownum)
    TextView userFollownum;
    @Bind(R.id.ll_myfollow)
    LinearLayout llMyfollow;
    @Bind(R.id.user_follow)
    TextView userFollow;
    @Bind(R.id.user_fansnum)
    TextView userFansnum;
    @Bind(R.id.ll_myfans)
    LinearLayout llMyfans;
    @Bind(R.id.user_layout_cmenu)
    LinearLayout userLayoutCmenu;
    @Bind(R.id.user_layout_center)
    LinearLayout userLayoutCenter;
    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;
    @Bind(R.id.user_layout_menu)
    LinearLayout userLayoutMenu;
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
    private UserPresenter mUserPresenter;


    UserInspirationView mUserInspirationView;
    UserSongView mUserSongView;
    UserLyricView mUserLyricView;
    UserCollectionView mUserCollectionView;




    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }

    @Override
    public void initView() {

//        mUserPresenter = new UserPresenter(context, this);
//        mUserPresenter.init();

        myScrollView.initView(vToolbar, tvCenter);

        myScrollView.setScrollBottomListener(new TopFloatScrollView.OnScrollToBottomListener() {
            @Override
            public void scrollBottom() {

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


        show(llMyrecord,0);
        contentLayout.setVisibility(View.VISIBLE);

    }


    public void setSeletedTab(View view) {
        llMyrecord.setSelected(false);
        llMysong.setSelected(false);
        llMylyrics.setSelected(false);
        llMyfav.setSelected(false);

        view.setSelected(true);
    }

    public void setContentPage(int id) {

        UserBaseView view = mUserInspirationView;

        switch (id) {
            case 0:
                view = mUserInspirationView;
                break;
            case 1:
                view = mUserSongView;
                break;
            case 2:
                view = mUserLyricView;
                break;
            case 3:
                view = mUserCollectionView;
                break;
        }

        if (!(view instanceof UserInspirationView) ){
            mUserInspirationView.setVisibility(View.GONE);
        }
        if (!(view instanceof UserSongView) ){
            mUserSongView.setVisibility(View.GONE);
        }
        if (!(view instanceof UserLyricView) ){
            mUserLyricView.setVisibility(View.GONE);
        }
        if (!(view instanceof UserCollectionView) ){
            mUserCollectionView.setVisibility(View.GONE);
        }

        view.setVisibility(View.VISIBLE);
    }


    public void show(View view, int id){

        setSeletedTab(view);
        setContentPage(id);

    }


    @OnClick({R.id.ll_myrecord, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_myrecord:
                show(view ,0);
                break;
            case R.id.ll_mysong:
                show(view ,1);
                break;
            case R.id.ll_mylyrics:
                show(view ,2);
                break;
            case R.id.ll_myfav:
                show(view ,3);
                break;
        }
    }


    @Override
    public void setUserInfo() {

    }

    @Override
    public void loadUserInfoSuccess(String result) {

    }

    @Override
    public void loadFocusFansCountSuccess(String result) {

    }

    @Override
    public void loadUserInfoFail(String msg) {

    }

    @Override
    public void loadFocusFansCountFail(String msg) {

    }

    @Override
    public void delSuccess(String result) {

    }

    @Override
    public void delFail(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

}
