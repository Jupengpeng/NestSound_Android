package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchUserFragment extends BaseListFragment<FansBean> implements ISearchView {
    SearchPresenter searchPresenter;
    int column = 3;

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
        tvNoData.setText("暂无搜索结果！");
        ivNoData.setImageResource(R.drawable.ic_nosearch);
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
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
                if(isDestroy)return;
                if(mDataList.size()==0){
                    EventBus.getDefault().post(new Event.ShowSearchTabEvent(true));
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
        if(isDestroy)return;
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if(isDestroy)return;
        if(mDataList.size()==0){
            EventBus.getDefault().post(new Event.ShowSearchTabEvent(false));
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
            if(StringUtil.isNotBlank(fansBean.headurl)&&!fansBean.headurl.equals(MyCommon.defult_head)) {
                String url = MyCommon.getImageUrl(fansBean.headurl, itemWidth, itemWidth);
                loadImage(url, ivHead);
            }
            if(StringUtil.isNotBlank(fansBean.nickname))tvName.setText(fansBean.nickname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if (mDataList.get(position).uid>0&&mDataList.get(position).uid!= PrefsUtil.getUserId(context)) {
                UserInfoActivity.ToUserInfoActivity(context,mDataList.get(position).uid,mDataList.get(position).fansname);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(searchPresenter!=null)
            searchPresenter.cancelUrl();
    }
}
