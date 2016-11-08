package com.xilu.wybz.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MatchCommentAdapter;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MatchWorkBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.MatchWorkPresenter;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.IView.IMatchWorkView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.FullyLinearLayoutManager;
import com.xilu.wybz.view.ScrollableHelper;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/12.
 */
public class MacthFragment extends BaseListFragment<MatchWorkBean> implements ScrollableHelper.ScrollableContainer, IMatchWorkView {
    private MatchWorkPresenter matchWorkPresenter;
    private String aid;
    private int type;
    private String sort;
    private String from;
    private int playPos = -1;
    private int commentPos = -1;

    public static MacthFragment newInstance() {
        MacthFragment fragment = new MacthFragment();
        return fragment;
    }

    public static MacthFragment newInstance(String aid, int type, String sort) {
        MacthFragment tabFragment = new MacthFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeySet.KEY_ID, aid);
        bundle.putInt(KeySet.KEY_TYPE, type);
        bundle.putString(KeySet.KEY_SORT, sort);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (MyApplication.getInstance().mMainService == null) {
            MyApplication.getInstance().bindMainService();
        }
        if (getArguments() != null) {
            type = getArguments().getInt(KeySet.KEY_TYPE);
            sort = getArguments().getString(KeySet.KEY_SORT);
            aid = getArguments().getString(KeySet.KEY_ID);
        }
        from = sort.equals("0") ? MyCommon.MATCH_NEW : sort.equals("1")?MyCommon.MATCH_HOT:MyCommon.MATCH_HOT;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        matchWorkPresenter = new MatchWorkPresenter(getActivity(), this);
        matchWorkPresenter.init();
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        matchWorkPresenter.loadMatchWorkData(aid, type, sort, page++);
    }

    @Override
    public View getScrollableView() {
        return getRecyclerView();
    }

    public void updateData() {
        action = PullRecycler.ACTION_PULL_TO_REFRESH;
        page = 1;
        matchWorkPresenter.loadMatchWorkData(aid, type, sort, page++);
    }

    @Override
    public void showWorkData(List<MatchWorkBean> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                for (MatchWorkBean matchWorkBean : worksDataList) {
                    String playId = PrefsUtil.getString(MainService.CurrentMusic.PLAY_ID, context);
                    if (playId.equals(matchWorkBean.itemid)) {
                        if (MainService.status != 1)
                            playPos = mDataList.size();
                        matchWorkBean.isPlay = MainService.status == 3;
                    } else {
                        matchWorkBean.isPlay = false;
                    }
                    mDataList.add(matchWorkBean);
                }
                recycler.enableLoadMore(true);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    public class MatchViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_music_name)
        TextView tvMusicName;
        @Bind(R.id.tv_music_author)
        TextView tvMusicAuthor;
        @Bind(R.id.tv_nickname)
        TextView tvNickName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_look_num)
        TextView tvLookNum;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;
        @Bind(R.id.tv_fav_num)
        TextView tvFavNum;
        @Bind(R.id.recycler_view_comment)
        RecyclerView recyclerViewComment;
        @Bind(R.id.tv_more_comment)
        TextView tvMoreComment;
        @Bind(R.id.et_comment)
        EditText etComment;
        @Bind(R.id.space)
        View space;
        @Bind(R.id.view)
        View view;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        List<CommentBean> commentBeanList;
        MatchCommentAdapter adapter;

        public MatchViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            MatchWorkBean worksData = mDataList.get(position);
            if (position == mDataList.size() - 1) {
                view.setBackgroundColor(Color.WHITE);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 48)));
                space.setVisibility(View.GONE);
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.main_bg));
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 10)));
                space.setVisibility(View.VISIBLE);
            }
            ivPlay.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
            if (StringUtils.isNotBlank(worksData.title))
                tvMusicName.setText(worksData.title);
            if (StringUtils.isNotBlank(worksData.nickname)) {
                tvMusicAuthor.setText(worksData.nickname);
                tvNickName.setText(worksData.nickname);
            }
            tvDate.setText(DateTimeUtil.getTime(DateTimeUtil.timestamp2DateTime2(worksData.jointime)));
            tvFavNum.setText(NumberUtil.format(worksData.fovnum));
            tvLookNum.setText(NumberUtil.format(worksData.looknum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
            tvMoreComment.setText("更多评论");
            if (worksData.listComment == null || worksData.listComment.size() == 0) {
                tvMoreComment.setVisibility(View.GONE);
            } else {
                tvMoreComment.setVisibility(View.VISIBLE);
            }
            if (StringUtils.isNotBlank(worksData.headurl))
                ImageLoadUtil.loadImage(worksData.headurl, ivHead, DensityUtil.dip2px(context, 45), DensityUtil.dip2px(context, 45));
            if (StringUtils.isNotBlank(worksData.pic))
                ImageLoadUtil.loadImage(worksData.pic, ivCover, DensityUtil.dip2px(context, 82), DensityUtil.dip2px(context, 82));
            else
                ImageLoadUtil.loadImage(MyHttpClient.QINIU_URL + MyCommon.getLyricsPic().get((int) (Math.random() * 10)), ivCover, DensityUtil.dip2px(context, 82), DensityUtil.dip2px(context, 82));
            etComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentPos = position;
                    CommentActivity.toCommentActivity(context, worksData.itemid, type + 1, true);
                }
            });
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PrefsUtil.getUserId(context) != worksData.userid) {
                        OtherUserCenterActivity.toUserInfoActivity(context, worksData.userid, worksData.nickname);
                    }
                }
            });
            ivPlay.setImageResource(worksData.isPlay ? R.drawable.ic_match_pause : R.drawable.ic_match_play);
            ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApplication.getInstance().getMainService() != null) {
                        String playFrom = PrefsUtil.getString("playFrom", context);
                        if (!playFrom.equals(from) || MainService.ids.size() == 0) {
                            if (MainService.ids.size() > 0)
                                MainService.ids.clear();
                            for (MatchWorkBean workData : mDataList) {
                                MainService.ids.add(workData.itemid);
                            }
                        }
                        if (playPos >= 0 && playPos != position) {//切换伴奏 重置上一首的状态
                            MyApplication.getInstance().mMainService.loadData(worksData.itemid, from, "",0);
                            mDataList.get(position).isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_match_pause);
                        } else if (playPos >= 0 && playPos == position) {//播放当前
                            mDataList.get(playPos).isPlay = !mDataList.get(playPos).isPlay;
                            ivPlay.setImageResource(mDataList.get(playPos).isPlay
                                    ? R.drawable.ic_match_pause : R.drawable.ic_match_play);
                            MyApplication.getInstance().mMainService.doPP(mDataList.get(playPos).isPlay);
                        } else {//初此播放
                            MyApplication.getInstance().mMainService.loadData(worksData.itemid, from, "",0);
                            mDataList.get(position).isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_match_pause);
                        }
                    }
                }
            });
            tvMoreComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentPos = position;
                    CommentActivity.toCommentActivity(context, worksData.itemid, type + 1, false);
                }
            });
            commentBeanList = new ArrayList<>();
            if (worksData.listComment != null) {
                if (worksData.listComment.size() <= 3) {
                    commentBeanList.addAll(worksData.listComment);
                } else if (worksData.listComment.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        commentBeanList.add(worksData.listComment.get(i));
                    }
                }
            }
            recyclerViewComment.setLayoutManager(new FullyLinearLayoutManager(context, 0));
            adapter = new MatchCommentAdapter(context, commentBeanList, 1);
            adapter.setOnItemClickListener(new MatchCommentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CommentBean commentBean = commentBeanList.get(position);
                    if (commentBean.uid != PrefsUtil.getUserId(context)) {
                        CommentActivity.toCommentActivity(context, worksData.itemid, type + 1, commentBean);
                    }
                }
            });
            recyclerViewComment.setAdapter(adapter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            MatchWorkBean worksData = mDataList.get(position);
            if (type == 1) {
                LyricsdisplayActivity.toLyricsdisplayActivity(context, worksData.itemid, worksData.title);
            } else {
                String playFrom = PrefsUtil.getString("playFrom", context);
                if (!playFrom.equals(from) || MainService.ids.size() == 0) {
                    if (MainService.ids.size() > 0)
                        MainService.ids.clear();
                    for (MatchWorkBean workData : mDataList) {
                        MainService.ids.add(workData.itemid);
                    }
                }
                PlayAudioActivity.toPlayAudioActivity(context, worksData.itemid, "", from);
            }
        }

    }

    public void updateCommentItem(List<CommentBean> list) {
        if (mDataList != null && commentPos >= 0 && commentPos < mDataList.size()) {
            mDataList.get(commentPos).listComment = list;
            recycler.getRecyclerView().requestLayout();
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event.PPStatusEvent event) {
        if (isDestroy) return;
        Log.e("status", "PPStatusEvent__" + event.getStatus() + "");
        switch (event.getStatus()) {
            case MyCommon.STARTED://开始
                String playId = PrefsUtil.getString(MainService.CurrentMusic.PLAY_ID, context);
                for (int i = 0; i < mDataList.size(); i++) {
                    if (playId.equals(mDataList.get(i).itemid)) {
                        playPos = i;
                        mDataList.get(i).isPlay = true;
                    } else {
                        mDataList.get(i).isPlay = false;
                    }
                }
                recycler.getRecyclerView().requestLayout();
                adapter.notifyDataSetChanged();
                break;
            case MyCommon.PLAYED://播放
                if (playPos > -1) {
                    if (!mDataList.get(playPos).isPlay) {
                        mDataList.get(playPos).isPlay = true;
                        recycler.getRecyclerView().requestLayout();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case MyCommon.PAUSED://暂停
                if (playPos > -1) {
                    if (mDataList.get(playPos).isPlay) {
                        mDataList.get(playPos).isPlay = false;
                        recycler.getRecyclerView().requestLayout();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}
