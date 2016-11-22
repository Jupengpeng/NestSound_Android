package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.Invitation;
import com.xilu.wybz.presenter.InvitationPresenter;
import com.xilu.wybz.ui.IView.IInvitationView;
import com.xilu.wybz.ui.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class InvitationActivity extends ToolbarActivity implements IInvitationView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.invitation_recyclerview)
    RecyclerView invitation_recyclerview;
    @Bind(R.id.et_keyword)
    EditText et_keyword;
    private InvitationPresenter invitationPresenter;
    private InvitationAdapter invitationAdapter;
    private List<Invitation> invitationlist;
    private int did;//合作需求ID
    private int page = 1;
    private boolean ishasData = true;

    private String content = "";
    private int currentScrollState;
    private int lastVisibleItemPosition;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_invitation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        invitationlist.addAll(invitationList);
        invitationAdapter.notifyDataSetChanged();
        invitationAdapter.onLoadMoreStateChanged(false);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void sendSuccess() {
        cancelPd();
        showMsg("邀请成功");
//        invitationAdapter.setType(4);
//        invitationAdapter.notifyDataSetChanged();

    }

    @Override
    public void serachSuccess() {
//        page++;
//        invitationPresenter.getInvitationList(did, page, "");
        ll_nodata.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
//        content = null;
    }

    @Override
    public void noData() {
        ll_nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        ishasData = false;
        invitationAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void noSerachData() {
        ishasData = false;
        refreshLayout.setRefreshing(false);
        ll_nodata.setVisibility(View.VISIBLE);

    }

    @Override
    public void noSerachMoreData() {
        ishasData = false;
        refreshLayout.setRefreshing(false);
        invitationAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void initView() {
        did = getIntent().getIntExtra("did", 0);
        invitationlist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        invitation_recyclerview.setLayoutManager(linearLayoutManager);
        invitationAdapter = new InvitationAdapter(invitationlist, this);
        invitation_recyclerview.setAdapter(invitationAdapter);

        invitationPresenter.getInvitationList(did, page, content);

        refreshLayout.setOnRefreshListener(this);

        invitationAdapter.setOnItemClickListener(new InvitationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (invitationlist.size() > 0) {
                    invitationPresenter.sendInvitation(invitationlist.get(position).getUid(), did);
                    showPd("邀请中...");
                }
            }
        });
        invitation_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = (linearLayoutManager)
                        .findLastVisibleItemPosition() + 1;

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentScrollState = newState;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !refreshLayout.isRefreshing() && ishasData) {
                    invitationAdapter.onLoadMoreStateChanged(true);
                    page++;
                    invitationPresenter.getInvitationList(did, page, content);

                }
            }
        });
        et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                content = et_keyword.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(content)) {
                        if (invitationlist.size() > 0) {
                            invitationlist.clear();
                        }
                        page = 1;
                        ishasData = true;
                        invitationPresenter.getInvitationList(did, page, content);
                        refreshLayout.setRefreshing(true);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invitationlist.clear();
                page = 1;
                content = "";
                invitationPresenter.getInvitationList(did, page, content);
                ishasData=true;
            }
        }, 2000);
    }
}
