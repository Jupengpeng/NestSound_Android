package com.xilu.wybz.ui;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MyPagerAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.BlankActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.lyrics.MakeWordActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.mine.NewMineActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.record.InspireRecordActivity;
import com.xilu.wybz.ui.song.MakeHotActivity;
import com.xilu.wybz.ui.song.NewMakeHotActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.VersionUtil;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.MoreWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/4/28.
 */
public class MainTabActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    IndexViewPager viewpager;
    @Bind(R.id.tv_home)
    CheckedTextView tvHome;
    @Bind(R.id.tv_found)
    CheckedTextView tvFound;
    @Bind(R.id.tv_msg)
    CheckedTextView tvMsg;
    @Bind(R.id.tv_mine)
    CheckedTextView tvMine;
    List<CheckedTextView> checkedTextViewList;
    ArrayList<View> list;
    MoreWindow mMoreWindow;
    int oldIndex;
    int currentIndex;
    long exitTime;
    Intent intent;
    MyPagerAdapter adapter;
    LocalActivityManager manager = null;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_maintab;
    }
    public boolean canBack() {
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        openPush();
        checkAppVersion();
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        checkedTextViewList = new ArrayList<>();
        checkedTextViewList.add(tvHome);
        checkedTextViewList.add(tvFound);
        checkedTextViewList.add(tvMsg);
        checkedTextViewList.add(tvMine);
        initPagerViewer();
    }
    //打开推送
    public void openPush() {
        if (PrefsUtil.getBoolean("isPushOpen", context) && !PushAgent.getInstance(context).isEnabled()) {
            PushAgent.getInstance(context).enable();
        }
    }
    //检测升级
    public void checkAppVersion() {
        new VersionUtil().checkUpdateInfo(this);
    }
    private void initPagerViewer() {
        viewpager.setScanScroll(false);
        list = new ArrayList<>();
        intent = new Intent(this, MainActivity.class);
        list.add(getView("MAIN", intent));
        intent = new Intent(this, FindActivity.class);
        list.add(getView("FIND", intent));
        if(PrefsUtil.getUserId(context)>0) {
            intent = new Intent(this, MsgActivity.class);
            list.add(getView("MSG", intent));
            intent = new Intent(this, MineActivity.class);
            list.add(getView("MINE", intent));
        }else{
            intent = new Intent(this, BlankActivity.class);
            list.add(getView("MSG", intent));
            intent = new Intent(this, BlankActivity.class);
            list.add(getView("MINE", intent));
        }
        adapter = new MyPagerAdapter(list);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(4);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            if(arg0==1){
                ((FindActivity)manager.getActivity("FIND")).initView();
            }else if(arg0==3){
                ((MineActivity)manager.getActivity("MINE")).initData();
            }
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    @OnClick({R.id.rl_main_home, R.id.rl_main_find, R.id.rl_main_publish, R.id.rl_main_msg, R.id.rl_main_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_main_home:
                currentIndex = 0;
                break;
            case R.id.rl_main_find:
                currentIndex = 1;
                break;
            case R.id.rl_main_publish:
                if(SystemUtils.isLogin(context)){
                    if (null == mMoreWindow) {
                        mMoreWindow = new MoreWindow(this);
                    }
                    mMoreWindow.showMoreWindow(MainTabActivity.this, view, onClickListener);
                }
                return;
            case R.id.rl_main_msg:
                currentIndex = 2;
                break;
            case R.id.rl_main_mine:
                currentIndex = 3;
                break;
        }
        if(PrefsUtil.getUserId(context)==0&&(currentIndex==2||currentIndex==3)){
            startActivity(LoginActivity.class);
            return;
        }
        if(oldIndex!=currentIndex) {
            checkedTextViewList.get(oldIndex).setChecked(false);
            checkedTextViewList.get(currentIndex).setChecked(true);
            viewpager.setCurrentItem(currentIndex, false);
            oldIndex = currentIndex;
        }
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_zuoci:
                    mMoreWindow.dismiss();
                    MakeWordActivity.toMakeWordActivity(context,PrefsUtil.getLocalLyrics(context));
                    break;
                case R.id.tv_record:
                    startActivity(InspireRecordActivity.class);
                    mMoreWindow.dismiss();
                    break;
                case R.id.tv_zuoqu:
                    startActivity(NewMakeHotActivity.class);
                    mMoreWindow.dismiss();
                    break;
            }
        }
    };
    public void onEventMainThread(Event.LoginSuccessEvent event){
        showMsg("登陆成功！");
        UserBean ub = event.getUserBean();
        MyApplication.getInstance().setUserid(ub.userid);
        MyApplication.getInstance().setIsLogin(true);
        PrefsUtil.saveUserInfo(context, ub);
        MobclickAgent.onProfileSignIn(ub.userid+"");
        PushAgent.getInstance(context).setAlias(ub.userid+"", "yinchao");
        PushAgent.getInstance(context).setExclusiveAlias(ub.userid+"", "yinchao");
        startActivity(MainTabActivity.class);
        finish();
        overridePendingTransition(R.anim.none,R.anim.none);
    }
    public void onEventMainThread(Event.LoginOutEvent event){
        startActivity(MainTabActivity.class);
        finish();
        overridePendingTransition(R.anim.none,R.anim.none);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showMsg("再按一次退出应用");
                exitTime = System.currentTimeMillis();
                return false;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
