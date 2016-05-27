package com.xilu.wybz.ui.msg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.presenter.MsgZanPresenter;
import com.xilu.wybz.ui.IView.IZanView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgZambiaActivity extends BaseListActivity<ZambiaBean> implements IZanView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无点赞";
    int nodatares = R.drawable.ic_nozan;
    private MsgZanPresenter zanPresenter;

    @Override
    protected void initPresenter() {
        zanPresenter = new MsgZanPresenter(this, this);
        zanPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("点赞");
        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
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
        zanPresenter.loadData(page++);
    }

    @Override
    public void showZambiaData(List<ZambiaBean> zambiaBeanList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(zambiaBeanList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
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

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_zambia, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_username)
        TextView tvUserName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_music_name)
        TextView tvMusicName;

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            ZambiaBean zanbiaBean = mDataList.get(position);
            tvTime.setText(zanbiaBean.getAddtime());
            tvUserName.setText(zanbiaBean.getName());
            tvAuthor.setText(PrefsUtil.getUserInfo(context).name);
            tvMusicName.setText(zanbiaBean.getMusicName());
            loadImage(zanbiaBean.getMusicPic(), ivCover);
            String headUrl = zanbiaBean.getHeadurl();
            if (headUrl.contains("qlogo.cn") && headUrl.contains("wuyuebuzuo.com")) {
                headUrl = headUrl.replace("http://api.wuyuebuzuo.com/api/", "");
            }
            loadImage(headUrl, ivHead);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
