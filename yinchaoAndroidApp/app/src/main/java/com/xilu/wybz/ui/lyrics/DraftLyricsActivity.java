package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.DraftListPresenter;
import com.xilu.wybz.presenter.DraftPresenter;
import com.xilu.wybz.ui.IView.IDraftListView;
import com.xilu.wybz.ui.IView.ISimpleView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.LyricsDraftUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.dialog.LrcDraftDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 16/5/13.
 */
public class DraftLyricsActivity extends BaseListActivity<LyricsDraftBean> implements IDraftListView,ISimpleView {

    String nodata = "暂无草稿";
    int nodatares = R.drawable.ic_nocomment;
    private DraftListPresenter draftListPresenter;
    private DraftPresenter draftPresenter;

    private int p = -1;

    @Override
    protected void initPresenter() {
        draftPresenter = new DraftPresenter(context,this);
        draftListPresenter = new DraftListPresenter(context, this);
        draftListPresenter.init();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void initView() {
        setTitle("歌词草稿箱");
        hideRight();
        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);

        recycler.enablePullToRefresh(true);
        recycler.enableLoadMore(true);

//        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#fff8f8f8"));
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();

//        List<LyricsDraftBean> list = LyricsDraftUtils.getAllDraft();
//        if (list == null){
//            list = new ArrayList<>();
//        }
//        mDataList=list;
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        draftListPresenter.getDraftList(page++);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyrics_draft, parent, false);
        return new SampleViewHolder(view);
    }


    @Override
    public void onSuccess(List<LyricsDraftBean> list) {
        if (recycler == null){
            return;
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
            if (list == null || list.size() == 0){
                llNoData.setVisibility(View.VISIBLE);
                recycler.enableLoadMore(false);
            }else {
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
            }
        }
        recycler.onRefreshCompleted();
        mDataList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        ToastUtils.toast(this,message);
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
    }

    @Override
    public void onSuccess(int id, String message) {
        cancelPd();
        if (p >= 0 && p < mDataList.size()){
            mDataList.remove(p);
            adapter.notifyDataSetChanged();
            p = -1;
        }
    }

    @Override
    public void onError(int id, String error) {
        ToastUtils.toast(this,error);
        cancelPd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                new MaterialDialog.Builder(context)
                        .content("是否清空本地草稿？")
                        .positiveText("清空草稿")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteDraft();
                            }
                        }).negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteDraft(){

        LyricsDraftUtils.deleteAll();
        mDataList.clear();
        adapter.notifyDataSetChanged();

    }

    public Context get(){
        return this;
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_text)
        TextView tvText;
        @Bind(R.id.rl_root)
        RelativeLayout rlRoot;

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            LyricsDraftBean bean = mDataList.get(position);
            tvName.setText(bean.title);
            tvText.setText(bean.content.replaceAll("[\\t\\n\\r]",";"));
            tvTime.setText(bean.getFormatTime());
            rlRoot.setTag(position);

            rlRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LrcDraftDialog draftDialog = new LrcDraftDialog(get(),position);
                    draftDialog.setListener(new LrcDraftDialog.OnDeleteListener() {
                        @Override
                        public void delete(int position) {
                            LyricsDraftBean bean1 = mDataList.get(position);
//                            LyricsDraftUtils.delete(bean1.file);
//                            if (position < mDataList.size()){
//                                mDataList.remove(position);
//                                adapter.notifyDataSetChanged();
//                            }
                            p = position;
                            draftPresenter.deleteDraft(bean1.id);
                            showPd("正在删除");
                        }
                    });
                    draftDialog.show();
                    return true;
                }
            });

            rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == -1) {
                        return;
                    }
                    WorksData worksData = new WorksData();
                    LyricsDraftBean bean1 = mDataList.get(position);
                    worksData.title = bean1.title;
                    worksData.lyrics = bean1.content.replaceAll("[\\t\\n\\r]","\n");
                    EventBus.getDefault().post(new Event.ImportWordEvent(worksData));
                    finish();

                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
        }
    }


    public void onDestroy() {
        super.onDestroy();
        if (draftListPresenter != null)
            draftListPresenter.cancelRequest();
    }
}
