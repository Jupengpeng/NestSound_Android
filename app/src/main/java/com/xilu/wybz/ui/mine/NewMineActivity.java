package com.xilu.wybz.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class NewMineActivity extends BasePlayMenuActivity implements IUserView {
    @Bind(R.id.iv_head)
    SimpleDraweeView ivHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.iv_modify)
    ImageView ivModify;
    @Bind(R.id.tv_song_num)
    TextView tvSongNum;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.tv_lyrics_num)
    TextView tvLyricsNum;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.tv_follow_num)
    TextView tvFollowNum;
    @Bind(R.id.fans_msg)
    View fansMsg;
    @Bind(R.id.ll_follow)
    LinearLayout llFollow;
    @Bind(R.id.tv_fans_num)
    TextView tvFansNum;
    @Bind(R.id.ll_fans)
    LinearLayout llFans;
    @Bind(R.id.ll_score)
    LinearLayout llScore;
    @Bind(R.id.ll_fav)
    LinearLayout llFav;
    @Bind(R.id.ll_record)
    LinearLayout llRecord;
    @Bind(R.id.ll_bq)
    LinearLayout llBq;
    @Bind(R.id.ll_mine_info)
    LinearLayout llMineInfo;
    @Bind(R.id.ll_share)
    LinearLayout llShare;
    @Bind(R.id.ll_setting)
    LinearLayout llSetting;
    @Bind(R.id.ll_mine_page)
    LinearLayout llMinePage;
    private UserPresenter userPresenter;
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
        UserInfoBean userInfoBean = PrefsUtil.getUserInfoNum(context);
        setUserInfoBean(userInfoBean);
        initUserInfo();
        userPresenter = new UserPresenter(this,this);
        userPresenter.loadData(PrefsUtil.getUserId(context),1,1);
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

    @OnClick({R.id.ll_mine_page, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_follow, R.id.ll_fans, R.id.ll_fav, R.id.ll_record, R.id.ll_bq, R.id.ll_mine_info, R.id.ll_share, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mine_page:
                startActivity(MineActivity.class);
                break;
            case R.id.ll_mysong:
                NewMyWorkActivity.toNewMyWorkActivity(context,1);
                break;
            case R.id.ll_mylyrics:
                NewMyWorkActivity.toNewMyWorkActivity(context,2);
                break;
            case R.id.ll_follow:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT,PrefsUtil.getUserId(context));
                break;
            case R.id.ll_fans:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT,PrefsUtil.getUserId(context));
                break;
            case R.id.ll_fav:
                NewMyWorkActivity.toNewMyWorkActivity(context,3);
                break;
            case R.id.ll_record:
                NewMyWorkActivity.toNewMyWorkActivity(context,4);
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
    public void setUserInfo(UserBean userBean) {

    }

    @Override
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        if(userInfoBean!=null){
            tvSongNum.setText(NumberUtil.format(userInfoBean.worknum));
            tvLyricsNum.setText(NumberUtil.format(userInfoBean.lyricsnum));
            tvFansNum.setText(NumberUtil.format(userInfoBean.fansnum));
            tvFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
        }
    }
    @Override
    public void showWorksData(List<WorksData> worksDataList) {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoData() {

    }

    @Override
    public void loadNoMore() {

    }

    @Override
    public void deleteSuccess() {

    }

    @Override
    public void deleteFail() {

    }

    @Override
    public void updateSuccess() {

    }

    @Override
    public void updateFail() {

    }

    @Override
    public void initView() {

    }
}
