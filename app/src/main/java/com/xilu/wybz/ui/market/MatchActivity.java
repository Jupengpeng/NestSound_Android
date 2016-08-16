package com.xilu.wybz.ui.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.JoinUserAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.fragment.MacthFragment;
import com.xilu.wybz.ui.main.MusicTalkMoreActivity;
import com.xilu.wybz.ui.main.SongablumMoreActivity;
import com.xilu.wybz.view.FolderTextView;
import com.xilu.wybz.view.ScrollableLayout;
import com.xilu.wybz.view.dialog.ActionMoreDialog;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by hujunwei on 16/8/12.
 */
public class MatchActivity extends BasePlayMenuActivity implements ViewPager.OnPageChangeListener, PtrHandler{
    @Bind(R.id.pfl_root)
    PtrClassicFrameLayout pflRoot;
    @Bind(R.id.sl_root)
    ScrollableLayout slRoot;
    @Bind(R.id.vp_scroll)
    ViewPager vpScroll;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.tv_info)
    FolderTextView tvInfo;
    @Bind(R.id.recycler_user)
    RecyclerView recyclerUser;
    List<UserBean> userBeenList;
    JoinUserAdapter adapter;
    int column = 8;
    ActionMoreDialog actionMoreDialog;
    List<ActionBean> actionBeans;
    private final List<MacthFragment> fragmentList = new ArrayList<>();
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_match;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("专题活动");
        pflRoot.setEnabledNextPtrAtOnce(true);
        pflRoot.setLastUpdateTimeRelateObject(this);
        pflRoot.setPtrHandler(this);
        pflRoot.setKeepHeaderWhenRefresh(true);
        CommonFragementPagerAdapter commonFragementPagerAdapter = new CommonFragementPagerAdapter(getSupportFragmentManager());
        fragmentList.add(MacthFragment.newInstance(1));
        fragmentList.add(MacthFragment.newInstance(2));
        vpScroll.setAdapter(commonFragementPagerAdapter);
        vpScroll.addOnPageChangeListener(this);
        slRoot.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpScroll);
        initJoinUserList();
    }
    public void initJoinUserList(){
        userBeenList = new ArrayList<>();
        for(int i=0;i<8;i++){
            userBeenList.add(new UserBean());
        }
        recyclerUser.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerUser.setLayoutManager(new GridLayoutManager(context,column));
//        recyclerUser.addItemDecoration(new SpacesItemDecoration(DensityUtil.dip2px(context,10)));
        adapter = new JoinUserAdapter(context,userBeenList,column);
        recyclerUser.setAdapter(adapter);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        slRoot.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//        if (vpScroll.getCurrentItem() == 0 && slRoot.isCanPullToRefresh()) {
//            return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//        }
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {

    }
//
//    public void refreshComplete() {
//        if (pflRoot != null) {
//            pflRoot.refreshComplete();
//        }
//    }

    public class CommonFragementPagerAdapter extends FragmentPagerAdapter {
        String titles[] = new String[]{"最新","最热"};
        public CommonFragementPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getCount() > position ? fragmentList.get(position) : null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    @OnClick({R.id.tv_join})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_join:
                if(actionBeans==null){
                    actionBeans = new ArrayList<>();
                    actionBeans.add(new ActionBean("去创作","create"));
                    actionBeans.add(new ActionBean("去投稿","submit"));
                }
                if(actionMoreDialog==null){
                    actionMoreDialog = new ActionMoreDialog(context, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String type = actionBeans.get(position).getType();
                            if(type.equals("create")){

                            }else if(type.equals("submit")){

                            }
                        }
                    },actionBeans);
                }
                actionMoreDialog.showDialog();
                break;
        }
    }
}
