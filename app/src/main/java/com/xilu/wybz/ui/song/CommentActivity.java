package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.CommentPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.dialog.ActionMoreDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/5/25.
 */
public class CommentActivity extends BaseListActivity<CommentBean> implements ICommentView {
    private LinearLayout llFootBar;
    private EditText etContent;
    private ImageView tvSend;
    private CommentPresenter commentPresenter;
    private WorksData worksData;
    private int type;
    private int commentType = 1;
    private int targetUid;
    private String targetName;
    private String content;

    String[] actionTitles = new String[]{"删除"};
    String[] actionTypes = new String[]{"del"};
    List<ActionBean> actionBeanList;
    ActionMoreDialog actionMoreDialog;

    public static void ToCommentActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("worksdata", worksData);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return super.getLayoutRes();
    }

    @Override
    protected void initPresenter() {
        commentPresenter = new CommentPresenter(context, this);
        commentPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("评论");
        tvNoData.setText("暂无评论内容！");
        ivNoData.setImageResource(R.drawable.ic_nocomment);
        hideRight();
        loadFootBar();
        initData();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksdata");
        }
        actionBeanList = new ArrayList<>();
        for (int i = 0; i < actionTitles.length; i++) {
            ActionBean actionBean = new ActionBean();
            actionBean.setTitle(actionTitles[i]);
            actionBean.setType(actionTypes[i]);
            actionBeanList.add(actionBean);
        }
    }

    public void loadFootBar() {
        ViewStub stub = (ViewStub) findViewById(R.id.view_footbar_send);
        llFootBar = (LinearLayout) stub.inflate();
        etContent = (EditText) llFootBar.findViewById(R.id.et_content);
        tvSend = (ImageView) llFootBar.findViewById(R.id.iv_send);
        tvSend.setEnabled(false);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtils.isLogin(context)) {
                    content = etContent.getText().toString().trim();
                    toSendComment();
                }
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSend.setEnabled(s.toString().length() > 0);
            }
        });
    }

    private void toSendComment() {
        commentPresenter.sendComment(worksData.itemid, commentType, type, targetUid, content);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        if (isDestroy){
            return;
        }
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        if (worksData != null) {
            type = TextUtils.isEmpty(worksData.getPlayurl()) ? 2 : 1;
            commentPresenter.getCommentList(worksData.getItemid(), type, page++);
        }
    }

    @Override
    public void showCommentData(List<CommentBean> commentBeans) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                if (isDestroy){
                    return;
                }
                recycler.enableLoadMore(true);
                mDataList.addAll(commentBeans);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    @Override
    public void showMsgCommentData(List<MsgCommentBean> commentBeans) {

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
        if (isDestroy){
            return;
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (isDestroy){
            return;
        }
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void commentSuccess(int id) {
        EventBus.getDefault().post(new Event.UpdataCommentNumEvent(type, 1));
        if (isDestroy){
            return;
        }
        KeyBoardUtil.closeKeybord(etContent, context);
        CommentBean commentBean = new CommentBean();
        commentBean.setUid(PrefsUtil.getUserId(context));
        commentBean.setTarget_uid(targetUid);
        commentBean.setType(type);
        commentBean.setId(id);
        commentBean.setComment_type(commentType);
        commentBean.setTarget_nickname(targetName);
        commentBean.setComment(content);
        commentBean.setHeaderurl(PrefsUtil.getUserInfo(context).headurl);
        commentBean.setComment_type(commentType);
        commentBean.setTarget_uid(targetUid);
        commentBean.setCreatedate(System.currentTimeMillis());
        commentBean.setNickname(PrefsUtil.getUserInfo(context).name);
        addItem(commentBean);
        if (mDataList.size() == 1) {
            llNoData.setVisibility(View.GONE);
        }
        targetUid = 0;
        commentType = 1;
        etContent.setText("");
    }

    @Override
    public void commentFail() {
        if(NetWorkUtil.isNetworkAvailable(context)){
            showLocationMsg("评论失败！",llMain);
        }else{
            showLocationMsg("网络无法连接！",llMain);
        }
    }

    @Override
    public void delSuccess(int pos) {
        EventBus.getDefault().post(new Event.UpdataCommentNumEvent(type, -1));
        if (isDestroy){
            return;
        }
        removeItem(pos);
        if (mDataList.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
        }
        if (actionMoreDialog != null)
            actionMoreDialog.dismiss();
    }

    @Override
    public void delFail() {
        if(NetWorkUtil.isNetworkAvailable(context)){
            showLocationMsg("删除失败！",llMain);
        }else{
            showLocationMsg("网络无法连接！",llMain);
        }
        if (actionMoreDialog != null)
            actionMoreDialog.dismiss();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    public class CommentViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_head)
        CircleImageView ivHead;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_content)
        TextView tvContent;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            CommentBean bean = mDataList.get(position);
            loadImage(bean.headerurl, ivHead);
            tvName.setText(bean.nickname);
            tvDate.setText(DateTimeUtil.timestamp2DateTime(bean.createdate));
            SpannableString s = StringStyleUtil.getWorkCommentStyleStr(context,bean);
            tvContent.setText(s);
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
//            itemView.setOnClickListener(null);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if (PrefsUtil.getUserId(context) > 0) {
                CommentBean commentBean = mDataList.get(position);
                boolean isMe = commentBean.uid == PrefsUtil.getUserId(context);
                if (isMe) {
                    actionMoreDialog = new ActionMoreDialog(context, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos == 0) {
                                commentPresenter.delComment(commentBean.id, commentBean.itemid, position, type);
                            }
                        }
                    }, actionBeanList);
                    if (!actionMoreDialog.isShowing()) {
                        actionMoreDialog.showDialog();
                    }
                } else {
                    targetUid = commentBean.uid;
                    targetName = commentBean.nickname;
                    commentType = 2;//回复别人
                    etContent.setHint("回复" + commentBean.nickname);
                    etContent.requestFocus();
                    KeyBoardUtil.openKeybord(etContent, context);
                }
            } else {
                ToastUtils.logingTip(context, "请登录后再进行回复！");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(commentPresenter!=null) {
            commentPresenter.cancelRequest();
        }
        super.onDestroy();
    }
}
