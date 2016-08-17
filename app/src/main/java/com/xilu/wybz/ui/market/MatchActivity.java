package com.xilu.wybz.ui.market;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.JoinUserAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.ActivityDetail;
import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.MatchBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.presenter.MatchPresenter;
import com.xilu.wybz.ui.IView.IMatchView;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.fragment.MacthFragment;
import com.xilu.wybz.ui.main.MusicTalkMoreActivity;
import com.xilu.wybz.ui.main.SongablumMoreActivity;
import com.xilu.wybz.ui.mine.MyWorkActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.FolderTextView;
import com.xilu.wybz.view.ScrollableLayout;
import com.xilu.wybz.view.dialog.ActionMoreDialog;

import org.w3c.dom.Text;

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
public class MatchActivity extends BasePlayMenuActivity implements ViewPager.OnPageChangeListener, PtrHandler, IMatchView {
    @Bind(R.id.pfl_root)
    PtrClassicFrameLayout pflRoot;
    @Bind(R.id.sl_root)
    ScrollableLayout slRoot;
    @Bind(R.id.vp_scroll)
    ViewPager vpScroll;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.tv_join)
    TextView tvJoin;
    @Bind(R.id.recycler_user)
    RecyclerView recyclerUser;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_work_num)
    TextView tvWorkNum;
    @Bind(R.id.tv_look_num)
    TextView tvLookNum;
    @Bind(R.id.tv_desc)
    FolderTextView tvDesc;
    @Bind(R.id.tv_join_num)
    TextView tvJoinNum;
    List<JoinUserBean> joinUserBeanList;
    JoinUserAdapter adapter;
    int column = 8;
    ActionMoreDialog actionMoreDialog;
    List<ActionBean> actionBeans;
    MatchPresenter matchPresenter;
    String id;
    int type;
    String status;
    int isJoin;
    private final List<MacthFragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_match;
    }

    // status: ing/end
    public static void toMatchActivity(Context context, String id,int type, String status) {
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("status", status);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("专题活动");
        matchPresenter = new MatchPresenter(context, this);
        matchPresenter.init();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            type = bundle.getInt("type");
            status = bundle.getString("status", "");
            if (status.equals("end")) {
                tvJoin.setVisibility(View.GONE);
            }
        }
        pflRoot.setEnabledNextPtrAtOnce(true);
        pflRoot.setLastUpdateTimeRelateObject(this);
        pflRoot.setPtrHandler(this);
        pflRoot.setKeepHeaderWhenRefresh(true);
        CommonFragementPagerAdapter commonFragementPagerAdapter = new CommonFragementPagerAdapter(getSupportFragmentManager());
        fragmentList.add(MacthFragment.newInstance(id,type,"0"));
        fragmentList.add(MacthFragment.newInstance(id,type,"1"));
        vpScroll.setAdapter(commonFragementPagerAdapter);
        vpScroll.addOnPageChangeListener(this);
        slRoot.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpScroll);
        initJoinUserList();


        matchPresenter.getMatchInfo(id);
        matchPresenter.getUserList(id, 1);
    }

    public void initJoinUserList() {
        joinUserBeanList = new ArrayList<>();
        recyclerUser.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerUser.setLayoutManager(new GridLayoutManager(context, column));
        adapter = new JoinUserAdapter(context, joinUserBeanList, column);
        adapter.setOnItemClickListener(new JoinUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfoActivity.ToNewUserInfoActivity(context, joinUserBeanList.get(position).id,
                        joinUserBeanList.get(position).nickname);
            }
        });
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

    @Override
    public void showMatchData(MatchBean matchBean) {
        ActivityDetail activityDetail = matchBean.activityDetail;
        isJoin = matchBean.isJoin;
        if(activityDetail!=null) {
            try {
                tvJoinNum.setText("参加人数"+NumberUtil.format(activityDetail.joinnum)+"人");
                tvWorkNum.setText(NumberUtil.format(activityDetail.worknum));
                tvLookNum.setText(NumberUtil.format(activityDetail.looknum));
                tvTitle.setText(activityDetail.title);
                tvDate.setText(DateTimeUtil.timestamp2Date(activityDetail.begindate)
                        + "-" + DateTimeUtil.timestamp2Date(activityDetail.enddate));
                tvDesc.setText(activityDetail.description);
                ImageLoadUtil.loadImage(context, activityDetail.pic, ivCover,
                        DensityUtil.dip2px(context, 135), DensityUtil.dip2px(context, 86));
            }catch(Exception e){

            }
        }
    }

    @Override
    public void showJoinData(List<JoinUserBean> joinUserBeanList) {
        if (joinUserBeanList.size() <= 8) {
            joinUserBeanList.addAll(joinUserBeanList);
        } else {
            for (int i = 0; i < 7; i++) {
                joinUserBeanList.add(joinUserBeanList.get(i));
            }
            JoinUserBean joinUserBean = new JoinUserBean();
            joinUserBean.isMore = true;
            joinUserBeanList.add(joinUserBean);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadOver() {

    }

    @Override
    public void loadFail() {

    }

//
//    public void refreshComplete() {
//        if (pflRoot != null) {
//            pflRoot.refreshComplete();
//        }
//    }

    public class CommonFragementPagerAdapter extends FragmentPagerAdapter {
        String titles[] = new String[]{"最新", "最热"};

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
                if (!SystemUtils.isLogin(context)) {
                    return;
                }
                if (actionBeans == null) {
                    actionBeans = new ArrayList<>();
                    actionBeans.add(new ActionBean("去创作", "create"));
                    actionBeans.add(new ActionBean("去投稿", "submit"));
                }
                if (actionMoreDialog == null) {
                    actionMoreDialog = new ActionMoreDialog(context, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String type = actionBeans.get(position).getType();
                            if (type.equals("create")) {

                            } else if (type.equals("submit")) {
                                MyWorkActivity.toMyWorkActivity(context, 2);
                            }
                        }
                    }, actionBeans);
                }
                actionMoreDialog.showDialog();
                break;
        }
    }
}
