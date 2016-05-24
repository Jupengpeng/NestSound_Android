package com.xilu.wybz.ui;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MyPagerAdapter;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.find.FindActivity;
import com.xilu.wybz.ui.find.SearchWorksActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.lyrics.MakeWordActivity;
import com.xilu.wybz.ui.main.MainActivity;
import com.xilu.wybz.ui.mine.MineActivity;
import com.xilu.wybz.ui.msg.MsgActivity;
import com.xilu.wybz.ui.record.InspireRecordActivity;
import com.xilu.wybz.ui.song.MakeSongActivity;
import com.xilu.wybz.utils.PrefsUtil;
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
public class MainTabActivity extends BasePlayMenuActivity {
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
    MoreWindow mMoreWindow;
    int oldIndex;
    int currentIndex;
    Intent intent;
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
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        checkedTextViewList = new ArrayList<>();
        checkedTextViewList.add(tvHome);
        checkedTextViewList.add(tvFound);
        checkedTextViewList.add(tvMsg);
        checkedTextViewList.add(tvMine);
        initPagerViewer();
    }
    private void initPagerViewer() {
        viewpager.setScanScroll(false);
        ArrayList<View> list = new ArrayList<>();
        intent = new Intent(this, MainActivity.class);
        list.add(getView("MAIN", intent));
        intent = new Intent(this, FindActivity.class);
        list.add(getView("FIND", intent));
        intent = new Intent(this, MsgActivity.class);
        list.add(getView("MSG", intent));
        intent = new Intent(this, MineActivity.class);
        list.add(getView("MINE", intent));
        viewpager.setAdapter(new MyPagerAdapter(list));
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
            changeToolbar(arg0);
            if(arg0==1){
                ((FindActivity)manager.getActivity("FIND")).initView();
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    @OnClick({R.id.rl_main_home, R.id.rl_main_find, R.id.rl_main_publish, R.id.rl_main_msg, R.id.rl_main_mine, R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_main_home:
                currentIndex = 0;
                break;
            case R.id.rl_main_find:
                currentIndex = 1;
                break;
            case R.id.rl_main_publish:
                if(isLogin){
                    if (null == mMoreWindow) {
                        mMoreWindow = new MoreWindow(this);
                    }
                    mMoreWindow.showMoreWindow(MainTabActivity.this, view, onClickListener);
                }else{
                    startActivity(LoginActivity.class);
                }
                return;
            case R.id.rl_main_msg:
                currentIndex = 2;
                break;
            case R.id.rl_main_mine:
                currentIndex = 3;
                break;
            case R.id.ll_search:
                startActivity(SearchWorksActivity.class);
                return;
        }
        if(!isLogin&&(currentIndex==2||currentIndex==3)){
            startActivity(LoginActivity.class);
            overridePendingTransition(R.anim.activity_open,0);
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
                    startActivity(MakeWordActivity.class);
                    break;
                case R.id.tv_record:
                    startActivity(InspireRecordActivity.class);
                    mMoreWindow.dismiss();
                    break;
                case R.id.tv_zuoqu:
                    startActivity(MakeSongActivity.class);
                    mMoreWindow.dismiss();
                    break;
            }
        }
    };
    private void changeToolbar(int index){
        app_bar_layout.setVisibility(index==3?View.GONE:View.VISIBLE);
        ll_search.setVisibility(index==1?View.VISIBLE:View.GONE);
        switch (index){
            case 0:
                setTitle(getResources().getString(R.string.app_name));
                break;
            case 1:
                setTitle("");
                break;
            case 2:
                setTitle(getResources().getString(R.string.app_msg));
                break;
            case 3:
                setTitle(getResources().getString(R.string.app_mine));
                break;
        }
    }
    public void onEventMainThread(Event.PPStatusEvent event) {
        switch (event.getStatus()) {
            case 1://开始
                startAnimal();
                break;
            case 2://停止
                stopAnimal();
                break;
            case 3://播放
                startAnimal();
                break;
            case 4://暂停
                stopAnimal();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
