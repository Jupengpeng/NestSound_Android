package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.CommentPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.dialog.ActionMoreDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
/**
 * Created by Administrator on 2016/5/25.
 */
public class CommentActivity extends BaseListActivity<CommentBean> implements ICommentView {

    private LinearLayout llFootBar;
    private EditText etContent;
    private ImageView tvSend;
    private CommentPresenter commentPresenter;
    private WorksData worksData;
    private int delPos;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        rl_right.setVisibility(View.GONE);
        loadFootBar();
        initData();
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable("worksdata");
        }
        actionBeanList = new ArrayList<>();
        for(int i = 0;i<actionTitles.length;i++) {
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
                if(SystemUtils.isLogin(context)) {
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
        if (worksData != null) {
            type = TextUtils.isEmpty(worksData.getPlayurl()) ? 2 : 1;
            commentPresenter.getCommentList(worksData.getItemid(), type, page++);
        }
    }

    @Override
    public void showCommentData(List<CommentBean> commentBeans) {
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
    public void commentSuccess() {
        CommentBean commentBean = new CommentBean();
        commentBean.setUid(PrefsUtil.getUserId(context));
        commentBean.setTarget_uid(targetUid);
        commentBean.setType(type);
        commentBean.setComment_type(commentType);
        commentBean.setTarget_nickname(targetName);
        commentBean.setComment(content);
        commentBean.setHeaderurl(PrefsUtil.getUserInfo(context).headurl);
        commentBean.setComment_type(commentType);
        commentBean.setTarget_uid(targetUid);
        commentBean.setCreatedate(System.currentTimeMillis());
        commentBean.setNickname(PrefsUtil.getUserInfo(context).name);
        addItem(commentBean);
        if(mDataList.size()==1){
            llNoData.setVisibility(View.GONE);
        }
        targetUid = 0;
        commentType = 1;
        etContent.setText("");
    }

    @Override
    public void commentFail() {
        showMsg("评论失败！");

    }

    @Override
    public void delSuccess() {
        removeItem(delPos);
        if(mDataList.size()==0){
            llNoData.setVisibility(View.VISIBLE);
        }
        actionMoreDialog.dismiss();
    }

    @Override
    public void delFail() {
        showMsg("删除失败！");
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
            super.onBindViewHolder(position);
            CommentBean bean = mDataList.get(position);
            loadImage(bean.headerurl, ivHead);
            tvName.setText(bean.nickname);
            tvDate.setText(DateTimeUtil.timestamp2DateTime(bean.createdate));
            SpannableString s = StringStyleUtil.getWorkCommentStyleStr(bean);
            tvContent.setText(s);
        }

        @Override
        public void onItemClick(View view, int position) {
            Log.e("onItemClick position",position+"");
            if(PrefsUtil.getUserId(context)>0) {
                CommentBean commentBean = mDataList.get(position);
                boolean isMe = commentBean.uid == PrefsUtil.getUserId(context);
                if (isMe) {
                    if (actionMoreDialog == null) {
                        actionMoreDialog = new ActionMoreDialog(context, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                if (pos == 0) {
                                    delPos = position;
                                    commentPresenter.delComment(commentBean.id, type);
                                }
                            }
                        }, actionBeanList);
                    }
                    if (!actionMoreDialog.isShowing()) {
                        actionMoreDialog.showDialog();
                    }
                } else {
                    targetUid = commentBean.target_uid;
                    targetName = commentBean.target_nickname;
                    commentType = 2;//回复别人
                    etContent.setHint("回复" + commentBean.nickname);
                    etContent.requestFocus();
                    KeyBoardUtil.openKeybord(etContent,context);
                }
            }else{
                ToastUtils.logingTip(context,"请登录后再进行回复！");
            }
        }
    }
}
