package com.xilu.wybz.ui.msg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgCollectionPresenter;
import com.xilu.wybz.ui.IView.ICollectionView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgFavActivity extends BaseListActivity<CollectionBean> implements ICollectionView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无收藏";
    int nodatares = R.drawable.ic_nofav;
    private MsgCollectionPresenter collectionPresenter;

    @Override
    protected void initPresenter() {
        collectionPresenter = new MsgCollectionPresenter(this, this);
        collectionPresenter.init();
    }
    @Override
    public boolean hasPadding() {
        return false;
    }
    @Override
    public void initView() {
        setTitle("收藏");
        hideRight();
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
        collectionPresenter.loadData(page++);
    }


    @Override
    public void showCollectionData(List<CollectionBean> collectionBeans) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(collectionBeans);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (recycler == null){
            return;
        }
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
        @Bind(R.id.tv_content)
        TextView tvContent;
        @OnClick(R.id.ll_works)
        void toWorks(){
            CollectionBean collectionBean = (CollectionBean)itemView.getTag();
            if(collectionBean.type==1){
                if(collectionBean.workid>0)
                    PlayAudioActivity.toPlayAudioActivity(context, collectionBean.workid, "", MyCommon.MSG_COMMENT);
            }else{
                if(collectionBean.workid>0)
                    LyricsdisplayActivity.toLyricsdisplayActivity(context, collectionBean.workid, 0, collectionBean.title);
            }
        }
        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            CollectionBean collectionBean = mDataList.get(position);
            tvTime.setText(DateTimeUtil.timestamp2DateTime(collectionBean.intabletime));
            tvUserName.setText(collectionBean.nickname);
            tvAuthor.setText(collectionBean.author);
            if(StringUtil.isEmpty(collectionBean.title))collectionBean.title="未命名";
            tvMusicName.setText(collectionBean.title);
            tvContent.setText("收藏了你的作品");
            loadImage(collectionBean.pic, ivCover);
            String headUrl = collectionBean.headerurl;
            loadImage(headUrl, ivHead);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }
        @Override
        public void onItemClick(View view, int position) {

        }
    }
    @Override
    protected void onDestroy() {
        if(collectionPresenter!=null) {
            collectionPresenter.cancelUrl();
        }
        super.onDestroy();
    }
}
