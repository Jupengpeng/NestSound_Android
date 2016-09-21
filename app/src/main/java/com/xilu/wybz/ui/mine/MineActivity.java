package com.xilu.wybz.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.preservation.ProductPreservListActivity;
import com.xilu.wybz.ui.setting.ModifyUserInfoActivity;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.SystemBarHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/9/14.
 */
public class MineActivity extends BasePlayMenuActivity implements IUserView {
    @Bind(R.id.iv_head)
    SimpleDraweeView ivHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.tv_song_num)
    TextView tvSongNum;
    @Bind(R.id.tv_lyrics_num)
    TextView tvLyricsNum;
    @Bind(R.id.tv_follow_num)
    TextView tvFollowNum;
    @Bind(R.id.fans_msg)
    View fansMsg;
    @Bind(R.id.tv_fans_num)
    TextView tvFansNum;
    private UserPresenter userPresenter;
    private UserInfoBean userInfoBean;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_mine_page;
    }

    @Override
    public boolean canBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我");
        SystemBarHelper.tintStatusBar(this, Color.argb(255, 0xFF, 0xD7, 0x05));
        userInfoBean = PrefsUtil.getUserInfoBean(context,PrefsUtil.getUserId(context));
        setUserInfoBean(userInfoBean);
        initUserInfo();
        userPresenter = new UserPresenter(this,this);
        userPresenter.getUserInfo(PrefsUtil.getUserId(context));
    }
    //在修改个人资料页面发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateUserInfo event) {
        initUserInfo();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.NoticeMsgEvent event) {
        switch (event.getType()) {
            case MyCommon.PUSH_TYPE_FOCUS:
                fansMsg.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ClearMsgEvent event) {
        switch (event.getType()) {
            case MyCommon.PUSH_TYPE_FOCUS:
                fansMsg.setVisibility(View.GONE);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        showMsg("登陆成功！");
        UserBean ub = event.getUserBean();
        PrefsUtil.saveUserInfo(context, ub);
        initUserInfo();
    }
    public void initUserInfo() {
        UserBean userInfo = PrefsUtil.getUserInfo(context);
        if(StringUtils.isNotBlank(userInfo.headurl)){
            ImageLoadUtil.loadImage(userInfo.headurl,ivHead);
        }
        if(StringUtils.isNotBlank(userInfo.nickname)){
            tvName.setText(userInfo.nickname);
        }
        if(StringUtils.isNotBlank(userInfo.signature)){
            tvSign.setText(userInfo.signature);
        }
    }
    //修改粉丝数量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateFollowNumEvent event) {
        userInfoBean = PrefsUtil.getUserInfoBean(context,PrefsUtil.getUserId(context));
        int type = event.getType();
        if (type == 0) {
            userInfoBean.gznum -= 1;
            if (userInfoBean.gznum < 0) userInfoBean.gznum = 0;
        } else {
            userInfoBean.gznum += 1;
        }
        tvFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
        PrefsUtil.saveUserInfoBean(context,userInfoBean,PrefsUtil.getUserId(context));
    }
    @OnClick({R.id.ll_mine_page, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_follow, R.id.ll_fans, R.id.ll_fav, R.id.ll_record, R.id.ll_bq, R.id.ll_mine_info, R.id.ll_share, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mine_page:
                startActivity(UserCenterActivity.class);
                break;
            case R.id.ll_mysong:
                MineWorkActivity.toNewMyWorkActivity(context,1);
                break;
            case R.id.ll_mylyrics:
                MineWorkActivity.toNewMyWorkActivity(context,2);
                break;
            case R.id.ll_follow:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT,PrefsUtil.getUserId(context));
                break;
            case R.id.ll_fans:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT,PrefsUtil.getUserId(context));
                break;
            case R.id.ll_fav:
                MineWorkActivity.toNewMyWorkActivity(context,3);
                break;
            case R.id.ll_record:
                MineWorkActivity.toNewMyWorkActivity(context,4);
                break;
            case R.id.ll_bq:
                startActivity(ProductPreservListActivity.class);
                break;
            case R.id.ll_mine_info:
                startActivity(ModifyUserInfoActivity.class);
                break;
            case R.id.ll_share:
                break;
            case R.id.ll_setting:
                startActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        if(userInfoBean!=null){
            tvSongNum.setText(NumberUtil.format(userInfoBean.worknum));
            tvLyricsNum.setText(NumberUtil.format(userInfoBean.lyricsnum));
            tvFansNum.setText(NumberUtil.format(userInfoBean.fansnum));
            tvFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
            if(userInfoBean.msgCount>0){
                fansMsg.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void initView() {

    }
}
