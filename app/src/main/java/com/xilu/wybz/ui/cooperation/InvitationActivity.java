package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.Invitation;
import com.xilu.wybz.presenter.InvitationPresenter;
import com.xilu.wybz.ui.IView.IInvitationView;
import com.xilu.wybz.ui.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class InvitationActivity extends ToolbarActivity implements IInvitationView {
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.invitation_recyclerview)
    RecyclerView invitation_recyclerview;
    @Bind(R.id.et_keyword)
    EditText et_keyword;
    private InvitationPresenter invitationPresenter;
    private InvitationAdapter invitationAdapter;
    private List<Invitation> invitationlist;
    private int did;//合作需求ID

    private int page = 1;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_invitation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("邀请");
        initPresenter();
    }

    private void initPresenter() {
        invitationPresenter = new InvitationPresenter(this, this);
        invitationPresenter.init();
    }

    @Override
    public void showInvitationList(List<Invitation> invitationList) {
        if (isDestroy) return;
        if (invitationlist == null) invitationList = new ArrayList<>();
        if (invitationlist.size() > 0) invitationlist.clear();
        invitationlist.addAll(invitationList);
        invitationAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendSuccess() {
        cancelPd();
        showMsg("邀请成功");
    }

    @Override
    public void initView() {
        did = getIntent().getIntExtra("did", 0);
        invitationlist = new ArrayList<>();
        invitation_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        invitationAdapter = new InvitationAdapter(invitationlist, this);
        invitation_recyclerview.setAdapter(invitationAdapter);
        invitationPresenter.getInvitationList(did, page, "");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                invitationPresenter.getInvitationList(page);
                refreshLayout.setRefreshing(false);
//                invitationAdapter.notifyDataSetChanged();
            }
        });
        invitationAdapter.setOnItemClickListener(new InvitationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (invitationlist.size() > 0) {

                    invitationPresenter.sendInvitation(invitationlist.get(position).getUid(), did);
                    showPd("邀请中...");
                }
            }
        });

        et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = et_keyword.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(content)) {
                        showMsg("hahah");
//                        invitationPresenter.getInvitationList(did, page, content);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
