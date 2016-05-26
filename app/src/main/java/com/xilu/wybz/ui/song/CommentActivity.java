package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.CommentPresenter;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.view.CircleImageView;
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
    public static void ToCommentActivity(Context context, WorksData worksData){
        Intent intent = new Intent(context,CommentActivity.class);
        intent.putExtra("worksdata",worksData);
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
        commentPresenter = new CommentPresenter(context,this);
        commentPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("评论");
        loadFootBar();
        initData();
    }
    public void initData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            worksData = (WorksData) bundle.getSerializable("worksdata");
        }
    }
    public void loadFootBar() {
        ViewStub stub = (ViewStub) findViewById(R.id.view_footbar_send);
        llFootBar = (LinearLayout) stub.inflate();
        etContent = (EditText) llFootBar.findViewById(R.id.et_content);
        tvSend = (ImageView) llFootBar.findViewById(R.id.iv_send);
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
        if(worksData!=null){
            commentPresenter.getCommentList(worksData.getItemid(), TextUtils.isEmpty(worksData.getPlayurl())?2:1,page++);
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

    }

    @Override
    public void loadNoMore() {

    }

    @Override
    public void loadNoData() {

    }

    @Override
    public void commentSuccess() {

    }

    @Override
    public void commentFail() {

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
            loadImage(bean.headerurl,ivHead);
            tvName.setText(bean.nickname);
            tvDate.setText(DateTimeUtil.timestamp2DateTime(bean.createdate));
            SpannableString s = StringStyleUtil.getWorkCommentStyleStr(bean);
            tvContent.setText(s);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }


}
