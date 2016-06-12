package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.FindSongBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchUserFragment extends BaseListFragment<FansBean> implements ISearchView {
    SearchPresenter searchPresenter;
    int column = 4;

    @Override
    protected void initPresenter() {
        searchPresenter = new SearchPresenter(context, this);
        searchPresenter.init();
    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }

    public void loadData(String name) {
        if (TextUtils.isEmpty(keyWord)) {
            keyWord = name;
            recycler.setRefreshing();
        }
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        searchPresenter.searchUserData(keyWord, 3, page++);
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {

    }

    @Override
    public void showUserData(List<FansBean> userBeenList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mDataList.size()==0){
                    EventBus.getDefault().post(new Event.ShowSearchTabEvent());
                }
                recycler.enableLoadMore(true);
                mDataList.addAll(userBeenList);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        },600);

    }

    @Override
    public void loadFail() {
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if(mDataList.size()==0){
            EventBus.getDefault().post(new Event.ShowSearchTabEvent());
        }
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getActivity().getApplicationContext(), column);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(column, dip10, false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        UsersViewHolder holder = new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_search_userlist_item, parent, false));
        return holder;
    }

    class UsersViewHolder extends BaseViewHolder {
        int itemWidth;
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_name)
        TextView tvName;

        public UsersViewHolder(View view) {
            super(view);
            itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
            ivHead.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            FansBean fansBean = mDataList.get(position);
            if(StringUtil.isNotBlank(fansBean.headurl)&&!fansBean.headurl.equals("http://pic.yinchao.cn/uploadfiles/2015/09/14/201509141121211442200881.png")) {
                String url = MyCommon.getImageUrl(fansBean.headurl, itemWidth, itemWidth);
                loadImage(url, ivHead);
            }
            if(StringUtil.isNotBlank(fansBean.nickname))tvName.setText(fansBean.nickname);
        }

        @Override
        public void onItemClick(View view, int position) {
            if (mDataList.get(position).fansid>0) {
                UserInfoActivity.ToUserInfoActivity(context,mDataList.get(position).fansid,mDataList.get(position).fansname);
            }
        }
    }
}
