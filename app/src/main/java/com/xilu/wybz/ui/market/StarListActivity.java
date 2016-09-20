package com.xilu.wybz.ui.market;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.StarBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.StarPresenter;
import com.xilu.wybz.ui.IView.IStarView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/12.
 */
public class StarListActivity extends BaseListActivity<StarBean> implements IStarView {
    StarPresenter starPresenter;


    public static void toStarListActivity(Context context) {
        Intent intent = new Intent(context, StarListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        starPresenter = new StarPresenter(context, this);
        starPresenter.init();
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void initView() {
        setTitle("明星音乐人");
        hideRight();
        recycler.enablePullToRefresh(false);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        starPresenter.getStarList(page++);
    }

    @Override
    public void showData(List<StarBean> startBeanList) {
        if(action== PullRecycler.ACTION_PULL_TO_REFRESH&&mDataList!=null){
            mDataList.clear();
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(true);
        mDataList.addAll(startBeanList);
        adapter.notifyDataSetChanged();
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
        recycler.onRefreshCompleted();
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_star_list, parent, false);
        return new SimpleViewHolder(view);
    }

    public class SimpleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_tag1)
        TextView tvTag1;
        @Bind(R.id.tv_tag2)
        TextView tvTag2;
        @Bind(R.id.tv_tag3)
        TextView tvTag3;
        @Bind(R.id.ll_tag)
        LinearLayout llTag;

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            StarBean starBean = mDataList.get(position);
            if (StringUtils.isNotBlank(starBean.pic))
                ImageLoadUtil.loadImage(MyCommon.getImageUrl(starBean.pic,
                        DensityUtil.dip2px(context,50),DensityUtil.dip2px(context,50)), ivHead);
            if (StringUtils.isNotBlank(starBean.name))
                tvName.setText(starBean.name);
            if (StringUtils.isNotBlank(starBean.ability)) {
                String[] tags = starBean.ability.split("/");
                llTag.setVisibility(tags.length > 0 ? View.VISIBLE : View.GONE);
                if (tags.length == 1) {
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.GONE);
                    tvTag3.setVisibility(View.GONE);
                    tvTag1.setText(tags[0]);
                } else if (tags.length == 2) {
                    tvTag2.setText(tags[0]);
                    tvTag3.setText(tags[1]);
                    tvTag1.setVisibility(View.GONE);
                    tvTag2.setVisibility(View.VISIBLE);
                    tvTag3.setVisibility(View.VISIBLE);
                } else if (tags.length >= 3) {
                    tvTag1.setText("全能");
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.GONE);
                    tvTag3.setVisibility(View.GONE);
                }

            } else {
                llTag.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            StarInfoActivity.toStarInfoActivity(context, mDataList.get(position).id);
        }
    }
}
