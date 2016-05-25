package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.ui.IView.ICommentView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/25.
 */
public class CommentActivity extends BaseListActivity<String> implements ICommentView {

    protected LinearLayout llFootBar;
    protected EditText etContent;
    protected ImageView tvSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return super.getLayoutRes();
//        return R.layout.activity_draft;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void initView() {
        setTitle(PrefsUtil.getUserInfo(this).name + "评论");

        loadFootBar();
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
        mDataList = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        mDataList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showCommentData(List<InforCommentBean> commentBeans) {

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

    @Override
    public void onRefresh(int action) {

    }

    public static class CommentViewHolder extends BaseViewHolder {

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


            InforCommentBean bean = new InforCommentBean();
            bean.target_nickname = "小妹";
            bean.setComment("小小不在家啊");
            SpannableString s = StringStyleUtil.getWorkCommentStyleStr(bean);
            tvContent.setText(s);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }


}
