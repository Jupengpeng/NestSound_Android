package com.xilu.wybz.ui.msg;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgZanPresenter;
import com.xilu.wybz.service.MyReceiver;
import com.xilu.wybz.ui.IView.IZanView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgZambiaActivity extends BaseListActivity<ZambiaBean> implements IZanView {
    String nodata = "暂无点赞";
    int nodatares = R.drawable.ic_nozan;
    private MsgZanPresenter zanPresenter;

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        zanPresenter = new MsgZanPresenter(this, this);
        zanPresenter.init();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MyReceiver.cancleNoticeByType(MyCommon.PUSH_TYPE_ZAN);
        onRefresh(PullRecycler.ACTION_PULL_TO_REFRESH);
    }
    @Override
    public void initView() {
        setTitle("点赞");
        MyReceiver.cancleNoticeByType(MyCommon.PUSH_TYPE_ZAN);
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
        super.onRefresh(action);
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
        if (recycler == null) {
            return;
        }
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (recycler == null) {
            return;
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (recycler == null) {
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

        @OnClick(R.id.ll_works)
        void toWorks() {
            ZambiaBean zambiaBean = (ZambiaBean) itemView.getTag();
            if (StringUtils.isNotBlank(zambiaBean.itemid)) {
                if (zambiaBean.type == 1) {
                    PlayAudioActivity.toPlayAudioActivity(context, zambiaBean.itemid, "", MyCommon.MSG_COMMENT);
                } else {
                    LyricsdisplayActivity.toLyricsdisplayActivity(context, zambiaBean.itemid, zambiaBean.title);
                }
            }
        }

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            ZambiaBean zanbiaBean = mDataList.get(position);
            tvTime.setText(DateTimeUtil.timestamp2DateTime(zanbiaBean.add_time));
            tvUserName.setText(zanbiaBean.nickname);
            tvAuthor.setText(zanbiaBean.author);
            if (StringUtils.isEmpty(zanbiaBean.title)) zanbiaBean.title = "未命名";
            tvMusicName.setText(zanbiaBean.title);
            loadImage(zanbiaBean.pic, ivCover);
            String headUrl = zanbiaBean.headerurl;
            loadImage(headUrl, ivHead);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    @Override
    protected void onDestroy() {
        if (zanPresenter != null)
            zanPresenter.cancelRequest();
        EventBus.getDefault().post(new Event.ClearMsgEvent(MyCommon.PUSH_TYPE_ZAN));
        super.onDestroy();

    }
}
