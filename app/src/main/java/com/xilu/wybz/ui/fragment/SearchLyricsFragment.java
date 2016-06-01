package com.xilu.wybz.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchLyricsFragment extends BaseListFragment<WorksData> implements ISearchView{
    SearchPresenter searchPresenter;
    @Override
    protected void initPresenter() {
        searchPresenter = new SearchPresenter(context,this);
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
    public void loadData(String name){
        if(TextUtils.isEmpty(keyWord)) {
            keyWord = name;
            recycler.setRefreshing();
        }
    }
    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        searchPresenter.searchWorkData(keyWord, 2, page++);
    }
    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void showUserData(List<FansBean> userBeenList) {

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
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }


    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }
    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false));
        return holder;
    }
    class WorksViewHolder extends BaseViewHolder {
        int itemWidth;
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_look_num)
        TextView tvLookNum;
        @Bind(R.id.tv_fov_num)
        TextView tvFovNum;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        public WorksViewHolder(View view) {
            super(view);
            itemWidth = DensityUtil.dip2px(context, 66);
            ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
            ImageLoadUtil.loadImage(url, ivCover);
            tvName.setText(worksData.title);
            tvAuthor.setText(worksData.author);
            tvLookNum.setText(NumberUtil.format(worksData.looknum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
            tvFovNum.setText(NumberUtil.format(worksData.fovnum));
        }

        @Override
        public void onItemClick(View view, int position) {
            LyricsdisplayActivity.toLyricsdisplayActivity(context,mDataList.get(position).getItemid(),0,mDataList.get(position).name);
        }
    }
}
