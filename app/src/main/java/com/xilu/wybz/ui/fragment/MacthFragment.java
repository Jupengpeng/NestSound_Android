package com.xilu.wybz.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.CommentPresenter;
import com.xilu.wybz.presenter.FindMoreWorkPresenter;
import com.xilu.wybz.presenter.MatchWorkPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.ui.IView.IMatchWorkView;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.FullyLinearLayoutManager;
import com.xilu.wybz.view.ScrollableHelper;
import com.xilu.wybz.view.dialog.CommentDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hujunwei on 16/8/12.
 */
public class MacthFragment extends BaseListFragment<MatchWorkBean> implements ScrollableHelper.ScrollableContainer, IMatchWorkView {
    MatchWorkPresenter matchWorkPresenter;
    String aid;
    int type;
    String sort;
    public static MacthFragment newInstance() {
        MacthFragment fragment = new MacthFragment();
        return fragment;
    }

    public static MacthFragment newInstance(String aid,int type,String sort) {
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
        if (getArguments() != null) {
            type = getArguments().getInt(KeySet.KEY_TYPE);
            sort = getArguments().getString(KeySet.KEY_SORT);
            aid = getArguments().getString(KeySet.KEY_ID);
        }
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
        matchWorkPresenter.loadMatchWorkData(aid,type,sort,page++);
    }

    @Override
    public View getScrollableView() {
        return getRecyclerView();
    }


    @Override
    public void showWorkData(List<MatchWorkBean> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDestroy)return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                recycler.enableLoadMore(true);
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
            ivPlay.setVisibility(type==1?View.GONE:View.VISIBLE);
            tvMusicName.setText(worksData.title);
            tvMusicAuthor.setText(worksData.nickname);
            tvNickName.setText(worksData.nickname);
            tvDate.setText(DateTimeUtil.getTime(DateTimeUtil.timestamp2DateTime2(worksData.jointime)));
            tvFavNum.setText(NumberUtil.format(worksData.fovnum));
            tvLookNum.setText(NumberUtil.format(worksData.looknum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
            ImageLoadUtil.loadImage(context,worksData.headurl,ivHead,DensityUtil.dip2px(context,45),DensityUtil.dip2px(context,45));
            ImageLoadUtil.loadImage(context,worksData.pic,ivCover,DensityUtil.dip2px(context,82),DensityUtil.dip2px(context,82));
            etComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tvMoreComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentActivity.ToCommentActivity(context, worksData.workid,type+1);
                }
            });
            commentBeanList = new ArrayList<>();
            for(int i=0;i<3;i++){
                commentBeanList.add(new CommentBean());
            }
            recyclerViewComment.setLayoutManager(new FullyLinearLayoutManager(context,0));
            adapter = new MatchCommentAdapter(context,commentBeanList,1);
            recyclerViewComment.setAdapter(adapter);
        }

        @Override
        public void onItemClick(View view, int position) {

        }

    }
}
