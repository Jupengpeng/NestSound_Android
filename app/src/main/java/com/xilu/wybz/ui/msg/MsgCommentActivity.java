package com.xilu.wybz.ui.msg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.presenter.MsgCommentPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.dialog.CommentDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgCommentActivity extends BaseListActivity<MsgCommentBean> implements ICommentView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无评论";
    int nodatares = R.drawable.ic_nocomment;
    private MsgCommentPresenter commentPresenter;
    private CommentDialog commentDialog;
    @Override
    protected void initPresenter() {
        commentPresenter = new MsgCommentPresenter(this, this);
        commentPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("评论");
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
        commentPresenter.loadData(page++);
    }

    @Override
    public void showCommentData(List<CommentBean> commentBeans) {

    }

    @Override
    public void showMsgCommentData(List<MsgCommentBean> commentBeans) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(commentBeans);
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
    public void commentSuccess(int id) {
        commentDialog.dismiss();
        commentDialog.cleanData();
        showMsg("回复成功！");
    }

    @Override
    public void commentFail() {
        commentDialog.dismiss();
        showMsg("回复失败！");
    }

    @Override
    public void delSuccess(int pos) {
        removeItem(pos);
    }

    @Override
    public void delFail() {

    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_comment, parent, false);
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
        @OnClick(R.id.tv_reply)
        void replyClick() {
            showCommentDialog((MsgCommentBean) card.getTag());
        }
        @OnClick(R.id.ll_music)
        void toPlayMusic(){
            showMsg("去歌曲播放页 如果是歌词 去歌词展示页");
        }
        public SampleViewHolder(View itemView) {
            super(itemView);
            card = itemView;
        }
        View card;
        @Override
        public void onBindViewHolder(int position) {
            MsgCommentBean commentBean = mDataList.get(position);
            card.setTag(commentBean);

            tvContent.setText(StringStyleUtil.getCommentStyleStr(commentBean));

            if(commentBean.createdate>0)tvTime.setText(DateTimeUtil.timestamp2Date(commentBean.createdate));
            if (StringUtil.isNotBlank(commentBean.nickname))tvUserName.setText(commentBean.nickname);
            if (StringUtil.isNotBlank(commentBean.headerurl)) loadImage(commentBean.headerurl, ivHead);

            if(commentBean.workid>0) {
                if (StringUtil.isNotBlank(commentBean.author)) tvAuthor.setText(commentBean.author);
                if (StringUtil.isNotBlank(commentBean.title)) tvMusicName.setText(commentBean.title);
                if (StringUtil.isNotBlank(commentBean.pic)) loadImage(commentBean.pic, ivCover);
            }else{

            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }

    }
    public void showCommentDialog(MsgCommentBean inforCommentBean) {
        if (commentDialog == null) {
            commentDialog = new CommentDialog(context, new CommentDialog.ICommentListener() {
                @Override
                public void toSend(String comment) {
                    toSendComment(comment, inforCommentBean);
                }
            });
        }
        if (!commentDialog.isShowing()) {
            commentDialog.showDialog();
        }
    }
    public void toSendComment(String content,MsgCommentBean inforCommentBean) {
        if (content.trim().equals("")) {
            showMsg("评论不能为空！");
            return;
        }
        commentPresenter.sendComment(inforCommentBean.workid, 2, inforCommentBean.type,inforCommentBean.target_uid,content);
    }
}
