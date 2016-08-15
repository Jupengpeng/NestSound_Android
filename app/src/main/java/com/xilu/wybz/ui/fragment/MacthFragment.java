package com.xilu.wybz.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MatchCommentAdapter;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.CommentPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.FullyLinearLayoutManager;
import com.xilu.wybz.view.ScrollableHelper;
import com.xilu.wybz.view.dialog.CommentDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hujunwei on 16/8/12.
 */
public class MacthFragment extends BaseListFragment<WorksData> implements ScrollableHelper.ScrollableContainer, ICommentView {
    private CommentDialog commentDialog;
    private CommentPresenter commentPresenter;

    public static MacthFragment newInstance() {
        MacthFragment fragment = new MacthFragment();
        return fragment;
    }

    public static MacthFragment newInstance(int type) {
        MacthFragment tabFragment = new MacthFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KeySet.KEY_TYPE, type);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        commentPresenter = new CommentPresenter(getActivity(), this);
        commentPresenter.init();
    }


    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        mDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDataList.add(new WorksData());
        }
    }

    @Override
    public View getScrollableView() {
        return getRecyclerView();
    }

    @Override
    public void showCommentData(List<CommentBean> commentBeans) {

    }

    @Override
    public void showMsgCommentData(List<MsgCommentBean> commentBeans) {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {

    }

    @Override
    public void loadNoData() {

    }

    @Override
    public void commentSuccess(int id) {

    }

    @Override
    public void commentFail() {

    }

    @Override
    public void delSuccess(int pos) {

    }

    @Override
    public void delFail() {

    }

    public void showCommentDialog(WorksData worksData) {
        if (commentDialog == null) {
            commentDialog = new CommentDialog(getActivity(), new CommentDialog.ICommentListener() {
                @Override
                public void toSend(String comment) {
                    toSendComment(comment, worksData);
                }
            });
        }
        if (!commentDialog.isShowing()) {
            commentDialog.showDialog();
        }
    }

    public void toSendComment(String content, WorksData worksData) {
        if (content.trim().equals("")) {
            ToastUtils.toast(getActivity(), "评论不能为空！");
            return;
        }
//        commentPresenter.sendComment(inforCommentBean.itemid, 2, inforCommentBean.type,inforCommentBean.target_uid,content);
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
        List<CommentBean> commentBeanList;
        MatchCommentAdapter adapter;
        public MatchViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            if (position == mDataList.size() - 1) {
                view.setBackgroundColor(Color.WHITE);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 48)));
                space.setVisibility(View.GONE);
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.main_bg));
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 10)));
                space.setVisibility(View.VISIBLE);
            }
            etComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentDialog(worksData);
                }
            });
            tvMoreComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentActivity.ToCommentActivity(context,worksData);
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
