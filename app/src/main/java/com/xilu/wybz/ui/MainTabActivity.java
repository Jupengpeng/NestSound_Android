package com.xilu.wybz.ui;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MyPagerAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.interfaces.OnTabActivityResultListener;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.lyrics.MakeWordActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.record.InspireRecordActivity;
import com.xilu.wybz.ui.song.HotCatalogActivity;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.VersionUtil;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.MoreWindow;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
    @Bind(R.id.iv_tip)
    ImageView ivTip;
    List<CheckedTextView> checkedTextViewList;
    ArrayList<View> list;
    MoreWindow mMoreWindow;
    int oldIndex;
    int currentIndex;
    long exitTime;
    Intent intent;
    MyPagerAdapter adapter;
    LocalActivityManager manager = null;
    public static boolean isForeground = false;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_maintab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        openPush();
        checkAppVersion();
        if (PermissionUtils.checkSdcardPermission(this)) {
        }
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
        if (PrefsUtil.getBoolean(KeySet.KEY_PUSH_OPEN, context)) {
            JPushInterface.resumePush(getApplicationContext());
        }
        JPushInterface.requestPermission(context);
        if(JPushInterface.getConnectionState(context)){
            Log.e("RegistrationID",JPushInterface.getRegistrationID(getApplicationContext()));
        }else{
            Log.e("un RegistrationID",JPushInterface.getRegistrationID(getApplicationContext()));
        }
        if(PrefsUtil.getUserId(context)>0) {
            JPushInterface.setAliasAndTags(getApplicationContext(), PrefsUtil.getUserId(context) + "", null, mAliasCallback);
        }
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    break;
                default:
                    logs = "Failed with errorCode = " + code;

            }
            Log.e("TagAliasCallback", logs);
        }
    };
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
        intent = new Intent(this, MsgActivity.class);
        list.add(getView("MSG", intent));
        intent = new Intent(this, MineActivity.class);
        list.add(getView("MINE", intent));
        adapter = new MyPagerAdapter(list);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(4);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if (manager != null) {
            ((MainActivity) manager.getActivity("MAIN")).onResume();
            ((FindActivity) manager.getActivity("FIND")).onResume();
        }
    }
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 1) {
                ((FindActivity) manager.getActivity("FIND")).initView();
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
                if (null == mMoreWindow) {
                    mMoreWindow = new MoreWindow();
                    RelativeLayout parent = (RelativeLayout) findViewById(R.id.window);
                    mMoreWindow.init(this, parent, onClickListener);
                }
                mMoreWindow.showByAnimation();
                return;
            case R.id.rl_main_msg:
                currentIndex = 2;
                break;
            case R.id.rl_main_mine:
                currentIndex = 3;
                break;
        }
        Log.e("getUserId", PrefsUtil.getUserId(context) + "");
        if (PrefsUtil.getUserId(context) == 0 && (currentIndex == 2 || currentIndex == 3)) {
            startActivity(LoginActivity.class);
            return;
        }
        changeTab();
    }

    public void changeTab() {
        if (oldIndex != currentIndex) {
            checkedTextViewList.get(oldIndex).setChecked(false);
            checkedTextViewList.get(currentIndex).setChecked(true);
            if (currentIndex == 0) {
                MainActivity mainActivity = ((MainActivity) manager.getActivity("MAIN"));
                if (mainActivity != null)
                    mainActivity.onResume();//刷新MainActivity播放状态
            } else if (currentIndex == 1) {
                FindActivity findActivity = ((FindActivity) manager.getActivity("FIND"));
                if (findActivity != null)
                    findActivity.onResume();
            } else if (currentIndex == 2) {
                MsgActivity msgActivity = ((MsgActivity) manager.getActivity("MSG"));
                if (msgActivity != null)
                    msgActivity.onResume();
            }
            viewpager.setCurrentItem(currentIndex, false);
            oldIndex = currentIndex;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(SystemUtils.isLogin(context)) {
                switch (v.getId()) {
                    case R.id.tv_zuoci:
                        MakeWordActivity.toMakeWordActivity(context, PrefsUtil.getLocalLyrics(context));
                        mMoreWindow.closeByAnimation();
                        break;
                    case R.id.tv_record:
                        startActivity(InspireRecordActivity.class);
                        mMoreWindow.closeByAnimation();
                        break;
                    case R.id.tv_zuoqu:
                        startActivity(HotCatalogActivity.class);
                        mMoreWindow.closeByAnimation();
                        break;
                }
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        showMsg("登陆成功！");
        UserBean ub = event.getUserBean();
        ub.nickname = ub.name;
        ub.signature = ub.descr;
        PrefsUtil.saveUserInfo(context, ub);
        JPushInterface.setAliasAndTags(getApplicationContext(), PrefsUtil.getUserId(context) + "", null, mAliasCallback);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.MsgTipEvent event) {
        ivTip.setVisibility(event.isShow()?View.VISIBLE:View.GONE);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginOutEvent event) {
        JPushInterface.setAliasAndTags(getApplicationContext(), "", null, mAliasCallback);
        if (viewpager.getCurrentItem() > 1) {
            viewpager.setCurrentItem(0);
            currentIndex = 0;
            changeTab();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取当前活动的Activity实例
        if (currentIndex == 3) {
            Activity subActivity = manager.getActivity("MINE");
            //判断是否实现返回值接口
            if (subActivity instanceof OnTabActivityResultListener) {
                //获取返回值接口实例
                OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
                //转发请求到子Activity
                listener.onTabActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMoreWindow != null) {
            mMoreWindow.destroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMoreWindow != null && mMoreWindow.isShow) {
                mMoreWindow.closeByAnimation();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                if (MainService.ids.size() > 0) {
                    String ids = "";
                    for (int i = 0; i < MainService.ids.size(); i++) {
                        ids += MainService.ids.get(i) + (i < MainService.ids.size() - 1 ? "," : "");
                    }
                    PrefsUtil.putString(PrefsUtil.getString("playFrom", context), ids, context);
                }
                PrefsUtil.saveHotBean(context, null);
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                finish();
                return true;

            } else {
                showMsg("再按一次退出应用");
                exitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
