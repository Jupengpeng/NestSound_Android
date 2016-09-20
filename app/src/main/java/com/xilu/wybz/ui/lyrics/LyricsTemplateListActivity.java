package com.xilu.wybz.ui.lyrics;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.presenter.TemplateLrcListPresenter;
import com.xilu.wybz.ui.IView.ITempleateListLrcView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class LyricsTemplateListActivity extends BaseListActivity<LyricsDraftBean> implements ITempleateListLrcView {

    TemplateLrcListPresenter presenter;
    @Override
    protected void initPresenter() {

        presenter = new TemplateLrcListPresenter(context,this);
        presenter.init();
    }

    @Override
    public void initView() {
        setTitle("模板");
        hideRight();
        tvNoData.setText("没有数据");

        recycler.enablePullToRefresh(true);
        recycler.enableLoadMore(true);
//        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#fff8f8f8"));
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        presenter.getTemplateList(page++);

    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void onSuccess(List<LyricsDraftBean> list) {
        if (recycler == null){
            return;
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.onRefreshCompleted();
        mDataList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        ToastUtils.toast(this,error);
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lrc_template, parent, false);
        return new SampleViewHolder(view);
    }



    class SampleViewHolder extends BaseViewHolder {

        LinearLayout root;
        TextView text;
        ImageView icon;
        public SampleViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout)itemView.findViewById(R.id.lrc_template_root);
            text = (TextView)itemView.findViewById(R.id.lrc_template_name);
            icon = (ImageView)itemView.findViewById(R.id.lrc_template_icon);
        }

        @Override
        public void onBindViewHolder(int position) {
            LyricsDraftBean bean = mDataList.get(position);
            text.setText(bean.title);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LyricsDraftBean bean = mDataList.get(position);
                    MakeWordByTempleateActivity.toMakeWordByTempleateActivity(LyricsTemplateListActivity.this,bean);
                }
            });
            if (StringUtils.isNotBlank(bean.mp3)){
                icon.setVisibility(View.VISIBLE);
            }else {
                icon.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
