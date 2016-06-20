package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.ActPresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.DividerItemDecoration;
import com.xilu.wybz.view.pull.PullRecycler;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
/**
 * Created by June on 16/5/7.
 */
public class ActFragment extends BaseListFragment<ActBean> implements IActView {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    long time;
    int itemWidth,itemHeight;
    private ActPresenter findPresenter;
    @Override
    protected void initPresenter() {
        findPresenter = new ActPresenter(context, this);
        showLoading(ll_loading);
        findPresenter.init();
    }
    @Override
    public void initView() {
        time = System.currentTimeMillis();
        findPresenter.getActList(page++);
    }
    @Override
    public void showActList(List<ActBean> actBeanList) {
        if(mDataList==null)mDataList = new ArrayList<>();
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(actBeanList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }
//    @Override
//    protected void setUpData() {
//        super.setUpData();
//        recycler.onRefresh();
//    }

    @Override
    public void showNoData() {
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void showNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disMissLoading(ll_loading);
            }
        },1000-(System.currentTimeMillis()-time));
    }

    @Override
    public void showErrorView() {
        recycler.onRefreshCompleted();
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
        findPresenter.getActList(page++);
    }
    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_find_actlist_item, parent, false);
        return new SampleViewHolder(view);
    }
    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView mDraweeView;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.fl_main)
        RelativeLayout fl_main;
        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 20);
            itemHeight = itemWidth * 135 / 330 ;
            int statusHeight = itemHeight*56/270;
            mDraweeView.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemHeight));
            fl_main.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(itemWidth, statusHeight);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tvStatus.setLayoutParams(layoutParams);
        }

        @Override
        public void onBindViewHolder(int position) {
            ActBean actBean = mDataList.get(position);
            int status = mDataList.get(position).status;
            if(status==1){
                tvStatus.setText("正在进行中  "+actBean.begindate+"~"+actBean.enddate);
            }else if(status==2){
                tvStatus.setText("活动已结束！");
            }else if(status==3){
                tvStatus.setText("活动尚未开始！");
            }
            String url = actBean.pic;
            ImageLoadUtil.loadImage(MyCommon.getImageUrl(url,itemWidth,itemHeight), mDraweeView);
        }

        @Override
        public void onItemClick(View view, int position) {
            int status = mDataList.get(position).status;
            if(status==1){
                BrowserActivity.toBrowserActivity(context,mDataList.get(position).url);
            }else if(status==2){
                showMsg("活动已结束！");
            }else if(status==3){
                showMsg("活动尚未开始！");
            }
        }
    }
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(findPresenter!=null)
            findPresenter.cancelUrl();
    }
}
