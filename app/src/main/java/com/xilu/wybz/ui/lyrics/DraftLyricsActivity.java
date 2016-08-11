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
import com.xilu.wybz.presenter.ImportWordPresenter;
import com.xilu.wybz.ui.IView.IImportWordView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.LyricsDraftUtils;
import com.xilu.wybz.view.dialog.LrcDraftDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 16/5/13.
 */
public class DraftLyricsActivity extends BaseListActivity<LyricsDraftBean> implements IImportWordView {

    private int page = 1;
    private int action = 0;
    String nodata = "暂无歌词";
    int nodatares = R.drawable.ic_nozan;
    private ImportWordPresenter importWordPresenter;


    @Override
    protected void initPresenter() {
//        importWordPresenter = new ImportWordPresenter(context, this);
//        importWordPresenter.init();
        initView();

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

        recycler.enablePullToRefresh(false);
        recycler.enableLoadMore(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
//        recycler.setRefreshing();

        List<LyricsDraftBean> list = LyricsDraftUtils.getAllDraft();
        if (list == null){
            list = new ArrayList<>();
        }
        mDataList=list;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
//        importWordPresenter.loadData(page++);
    }

    @Override
    public void showLyricsData(List<WorksData> worksDataList) {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
//                    mDataList.clear();
//                }
//                if (isDestroy) {
//                    return;
//                }
//                recycler.enableLoadMore(true);
//                mDataList.addAll(worksDataList);
//                adapter.notifyDataSetChanged();
//                recycler.onRefreshCompleted();
//            }
//        }, 600);
    }

    @Override
    public void loadFail() {
        if (recycler != null) {
            recycler.onRefreshCompleted();
        }
    }

    @Override
    public void loadNoMore() {
        if (recycler != null) {
            recycler.onRefreshCompleted();
            recycler.enableLoadMore(false);
        }
    }

    @Override
    public void loadNoData() {
        if (recycler != null) {
            llNoData.setVisibility(View.VISIBLE);
            recycler.onRefreshCompleted();
            recycler.enableLoadMore(false);
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyrics_draft, parent, false);
        return new SampleViewHolder(view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
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
            tvName.setText(bean.name);
            tvText.setText(bean.text);
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
                            LyricsDraftUtils.delete(bean1.file);
                            if (position < mDataList.size()){
                                mDataList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
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
                    worksData.title = bean1.name;
                    worksData.lyrics = bean1.text.replace(";","\n");
                    EventBus.getDefault().post(new Event.ImportWordEvent(worksData));
                    finish();

                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
//            if (position == -1) {
//                return;
//            }
//            EventBus.getDefault().post(new Event.ImportWordEvent(mDataList.get(position)));
//            finish();
        }
    }


    public void onDestroy() {
        super.onDestroy();
        if (importWordPresenter != null)
            importWordPresenter.cancelRequest();
    }
}
