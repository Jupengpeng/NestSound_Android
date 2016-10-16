package com.xilu.wybz.ui.msg;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.utils.Log;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgCommentPresenter;
import com.xilu.wybz.service.MyReceiver;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.dialog.CommentDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgCommentActivity extends BaseListActivity<MsgCommentBean> implements ICommentView {
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clearMsg();
        onRefresh(PullRecycler.ACTION_PULL_TO_REFRESH);
    }
    public void clearMsg(){
        EventBus.getDefault().post(new Event.ClearMsgEvent(MyCommon.PUSH_TYPE_COMMENT));
        Intent mIntent = new Intent("com.xilu.wybz.intent.CLEARNOTICE");
        mIntent.putExtra("type", MyCommon.PUSH_TYPE_COMMENT);
        sendBroadcast(mIntent);
    }
    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void initView() {
        setTitle("评论");
        hideRight();
        clearMsg();
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
        if(action==PullRecycler.ACTION_PULL_TO_REFRESH){
            if(MyReceiver.getHasUnReadMsg(MyCommon.PUSH_TYPE_COMMENT)){
                clearMsg();
            }
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
        for (MsgCommentBean commentBean : commentBeans) {
            mDataList.add(commentBean);
        }
        for(int i =0;i<commentBeans.size();i++){
            Log.e("AAA",commentBeans.get(i).getTarget_uid()+"");
        }
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
            showCommentDialog((MsgCommentBean) itemView.getTag());
        }

        @OnClick(R.id.ll_works)
        void toWorks() {
            MsgCommentBean msgCommentBean = (MsgCommentBean) itemView.getTag();
            if (StringUtils.isNotBlank(msgCommentBean.itemid)) {
                if (msgCommentBean.type == 1) {
                    PlayAudioActivity.toPlayAudioActivity(context, msgCommentBean.itemid, "", MyCommon.MSG_COMMENT);
                } else {
                    LyricsdisplayActivity.toLyricsdisplayActivity(context, msgCommentBean.itemid, msgCommentBean.title);
                }
            }
        }

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            MsgCommentBean commentBean = mDataList.get(position);
            itemView.setTag(commentBean);
            if(commentBean.getComment_type()==2){
                tvContent.setText(StringStyleUtil.getCommentStyleStr(commentBean));
                Log.e("AAA",StringStyleUtil.getCommentStyleStr(commentBean)+"");

            }


            if (commentBean.createdate > 0)
                tvTime.setText(DateTimeUtil.timestamp2Date(commentBean.createdate));
            if (StringUtils.isNotBlank(commentBean.nickname))
                tvUserName.setText(commentBean.nickname);
            if (StringUtils.isNotBlank(commentBean.headerurl))
                loadImage(commentBean.headerurl, ivHead);

            if (StringUtils.isNotBlank(commentBean.itemid)) {
                tvMusicName.setVisibility(View.VISIBLE);
                if (StringUtils.isNotBlank(commentBean.author))
                    tvAuthor.setText(commentBean.author);
                if (StringUtils.isBlank(commentBean.title)) commentBean.title = "未命名";
                tvMusicName.setText(commentBean.title);
                if (StringUtils.isNotBlank(commentBean.pic)) loadImage(commentBean.pic, ivCover);
            } else {
                tvMusicName.setVisibility(View.GONE);
                loadImage("res:///" + R.drawable.ic_nodata_pic, ivCover);
                tvAuthor.setText("抱歉，此作品已被删除！");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });

            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MsgCommentBean bean = mDataList.get(position);
                    if (PrefsUtil.getUserId(context) == bean.getUid()){
                        ToastUtils.toast(context,"你自己");
                        return;
                    }
                    OtherUserCenterActivity.toUserInfoActivity(context,bean.getUid(),bean.getNickname());
                }
            });


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

    public void toSendComment(String content, MsgCommentBean inforCommentBean) {
        if (content.trim().equals("")) {
            showMsg("评论不能为空！");
            return;
        }
        commentPresenter.sendComment(inforCommentBean.itemid, 2, inforCommentBean.type, inforCommentBean.uid, content);
    }

    @Override
    protected void onDestroy() {
        if (commentPresenter != null)
            commentPresenter.cancelRequest();
        super.onDestroy();

    }
}
