package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.CooperaMessageBean;
import com.xilu.wybz.presenter.CooperaMessagePresenter;
import com.xilu.wybz.ui.IView.ICooperaMessageView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.dialog.ActionMoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CooperaMessageActivity extends ToolbarActivity implements ICooperaMessageView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tv_nodata)
    protected TextView tvNoData;
    @Bind(R.id.iv_nodata)
    protected ImageView ivNoData;
    @Bind(R.id.ll_nodata)
    protected LinearLayout llNoData;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.message_recyclerview)
    RecyclerView message_recyclerview;
    @Bind(R.id.message_commentnum)
    TextView message_commentnum;
    private LinearLayout llFootBar;
    private EditText etContent;
    private ImageView tvSend;
    private String content;
    private CooperaMessagePresenter cooperaMessagePresenter;
    private CooperaMessageAdapter cooperaMessageAdapter;
    private int did;//合作需求ID
    private int page = 1;
    private int commentnum;
    private List<CooperaMessageBean> messageBeanList = new ArrayList<>();
    String comment;
    int comment_type = 1;
    long itemid;
    long target_uid;
    ActionMoreDialog actionMoreDialog;
    String[] actionTitles = new String[]{"删除"};
    String[] actionTypes = new String[]{"del"};
    List<ActionBean> actionBeanList;


    private boolean ishasData= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("留言");
        initData();
        loadFootBar();
        initPresenter();
    }

    private void initData() {
        did = getIntent().getIntExtra("did", 0);
        commentnum = getIntent().getIntExtra("commentnum", 0);
        itemid = getIntent().getLongExtra("itemid", 0);
        message_commentnum.setText("留言" + commentnum);
        actionBeanList = new ArrayList<>();
        for (int i = 0; i < actionTitles.length; i++) {
            ActionBean actionBean = new ActionBean();
            actionBean.setTitle(actionTitles[i]);
            actionBean.setType(actionTypes[i]);
            actionBeanList.add(actionBean);
        }

    }

    private void initPresenter() {
        cooperaMessagePresenter = new CooperaMessagePresenter(context, this);
        cooperaMessagePresenter.init();

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
                tvSend.setEnabled(s.toString().trim().length() > 0);
            }
        });
    }

    private void toSendComment() {
        showPd("正在评论中...");
        cooperaMessagePresenter.sendComment(did, comment_type, 3, target_uid, content);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_coopera_comment;
    }

    @Override
    public void showCooperaMessageList(List<CooperaMessageBean> cooperaMessageBeanList) {
        if (isDestroy) return;
        messageBeanList.addAll(cooperaMessageBeanList);
        cooperaMessageAdapter.notifyDataSetChanged();


    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        message_recyclerview.setLayoutManager(linearLayoutManager);
        cooperaMessageAdapter = new CooperaMessageAdapter(messageBeanList, this);
        message_recyclerview.setAdapter(cooperaMessageAdapter);
        cooperaMessagePresenter.getCooperaMessageList(did, page);
        refreshLayout.setOnRefreshListener(this);
        message_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1 && !refreshLayout.isRefreshing() && ishasData) {
                    cooperaMessageAdapter.onLoadMoreStateChanged(true);
                    page++;
                    cooperaMessagePresenter.getCooperaMessageList(did, page);
                }
            }
        });
        cooperaMessageAdapter.setOnItemClickListener(new CooperaMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, CooperaMessageBean cooperaMessageBean) {
                switch (type) {
                    case 1:
                        if (PrefsUtil.getUserId(context) > 0) {
                            boolean isMe = cooperaMessageBean.getUid() == PrefsUtil.getUserId(context);
                            if (isMe) {
                                actionMoreDialog = new ActionMoreDialog(context, new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                        if (pos == 0) {
//                                            commentPresenter.delComment(commentBean.id, commentBean.itemid, position, type);
                                        }
                                    }
                                }, actionBeanList);
                                if (!actionMoreDialog.isShowing()) {
                                    actionMoreDialog.showDialog();
                                }
                            } else {
                                itemid = cooperaMessageBean.getItemid();
                                comment_type = 2;
                                target_uid = cooperaMessageBean.getUid();
                                comment = etContent.getText().toString().trim();
                                etContent.setHint("回复" + cooperaMessageBean.getNickname());
                                etContent.requestFocus();
                                KeyBoardUtil.showSoftInput(context, etContent);
                            }
                        } else {
                            ToastUtils.logingTip(context, "请登录后再进行回复！");


                        }
                        break;
                    case 2:
                        if (cooperaMessageBean.getUid() != PrefsUtil.getUserId(context)) {
                            OtherUserCenterActivity.toUserInfoActivity(context, cooperaMessageBean.getUid(), cooperaMessageBean.getNickname());
                        }
                        break;

                }
            }
        });
        cooperaMessageAdapter.setOnItemLongClickListener(new CooperaMessageAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, int type, CooperaMessageBean cooperaMessageBean) {
                switch (type) {
                    case 1:
                        showMsg("hahaha ");
                        cooperaMessageAdapter.removeitem(position);

                        break;
                    case 2:

                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent();
//        setResult(200);
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {
        ishasData =false;
        cooperaMessageAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void loadNoData() {
//        tvNoData.setText("暂无留言");
//        refreshLayout.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void commentSuccess(int id) {
        showMsg("评论成功");
        cancelPd();
        llNoData.setVisibility(View.GONE);
        etContent.setText("");
        onRefresh();
        commentnum += 1;
        message_commentnum.setText("留言" + commentnum);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onRefresh() {
        if (messageBeanList.size() > 0) {
            messageBeanList.clear();
            page = 1;
        }
        cooperaMessagePresenter.getCooperaMessageList(did, page);
        refreshLayout.setRefreshing(false);
        ishasData=true;
    }
}
