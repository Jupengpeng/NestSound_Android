package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaMessageBean;
import com.xilu.wybz.presenter.CooperaMessagePresenter;
import com.xilu.wybz.ui.IView.ICooperaMessageView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.UserCenterActivity;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CooperaMessageActivity extends ToolbarActivity implements ICooperaMessageView {

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

    private List<CooperaMessageBean> messageBeanList = new ArrayList<>();
    String comment;
    int comment_type;
    long itemid;
    long target_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("留言");
        loadFootBar();
        initPresenter();
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
        cooperaMessagePresenter.sendComment(itemid, comment_type, 2, target_uid, content);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_coopera_comment;
    }

    @Override
    public void showCooperaMessageList(List<CooperaMessageBean> cooperaMessageBeanList) {
        if (isDestroy) return;
        if (messageBeanList == null) cooperaMessageBeanList = new ArrayList<>();
        if (messageBeanList.size() > 0) messageBeanList.clear();
        messageBeanList.addAll(cooperaMessageBeanList);
        cooperaMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        messageBeanList = new ArrayList<>();
        message_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cooperaMessageAdapter = new CooperaMessageAdapter(messageBeanList, this);
        message_recyclerview.setAdapter(cooperaMessageAdapter);
        cooperaMessagePresenter.getCooperaMessageList();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cooperaMessagePresenter.getCooperaMessageList();
                refreshLayout.setRefreshing(false);
            }
        });
        cooperaMessageAdapter.setOnItemClickListener(new CooperaMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, CooperaMessageBean cooperaMessageBean) {
                switch (type) {
                    case 1:
                        itemid = cooperaMessageBean.getItemid();
                        comment_type = cooperaMessageBean.getComment_type();
                        target_uid = cooperaMessageBean.getTarget_uid();
                        comment = etContent.getText().toString().trim();
                        if (comment_type == 2) {
                            etContent.setHint("回复" + position);
                        }else {
                            etContent.setHint("来~说点什么");
                        }
                        KeyBoardUtil.showSoftInput(context, etContent);

                        break;
                    case 2:
                        if (cooperaMessageBean.getUid() != PrefsUtil.getUserId(context)) {
                            startActivity(UserCenterActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
