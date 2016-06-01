package com.xilu.wybz.ui.lyrics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.ImportWordPresenter;
import com.xilu.wybz.ui.IView.IImportWordView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/13.
 */
public class ImportWordActivity extends BaseListActivity<WorksData> implements IImportWordView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无歌词";
    int nodatares = R.drawable.ic_nozan;
    private ImportWordPresenter importWordPresenter;
    @Override
    protected void initPresenter() {
        importWordPresenter = new ImportWordPresenter(context,this);
        importWordPresenter.init();
    }
    @Override
    public void initView() {
        setTitle("我的歌词");
        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
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
        importWordPresenter.loadData(page++);
    }

    @Override
    public void showLyricsData(List<WorksData> worksDataList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
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
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_importword_item, parent, false);
        return new SampleViewHolder(view);
    }
    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            tvName.setText(worksData.title);
            tvAuthor.setText(worksData.author);
            String pic = worksData.getPic();
            loadImage(pic, ivCover);
        }

        @Override
        public void onItemClick(View view, int position) {
            EventBus.getDefault().post(new Event.ImportWordEvent(mDataList.get(position)));
            finish();
        }
    }
}
