package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchSongFragment extends BaseListFragment<WorksData> implements ISearchView {
    SearchPresenter searchPresenter;

    @Override
    protected void initPresenter() {
        searchPresenter = new SearchPresenter(context, this);
        searchPresenter.init();
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
        tvNoData.setText("暂无搜索结果！");
        ivNoData.setImageResource(R.drawable.ic_nosearch);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        searchPresenter.searchWorkData(keyWord, 1, page++);
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (mDataList.size() == 0) {
                    EventBus.getDefault().post(new Event.ShowSearchTabEvent(true));
                }
                recycler.enableLoadMore(true);
                mDataList.addAll(worksDataList);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    @Override
    public void showUserData(List<FansBean> userBeenList) {

    }

    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        if (mDataList.size() == 0) {
            EventBus.getDefault().post(new Event.ShowSearchTabEvent(false));
        }
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false));
        return holder;
    }

    class WorksViewHolder extends BaseViewHolder {
        int itemWidth;
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_look_num)
        TextView tvLookNum;
        @Bind(R.id.tv_fov_num)
        TextView tvFovNum;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;

        public WorksViewHolder(View view) {
            super(view);
            itemWidth = DensityUtil.dip2px(context, 66);
            ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
            ImageLoadUtil.loadImage(url, ivCover);
            tvName.setText(worksData.title);
            if (worksData.type == 3) {
                tvAuthor.setText("合作作品");
            } else {
                tvAuthor.setText(worksData.author);
            }

            tvLookNum.setText(NumberUtil.format(worksData.looknum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
            tvFovNum.setText(NumberUtil.format(worksData.fovnum));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            WorksData worksData = mDataList.get(position);
            if (worksData.type == 3) {
                toPlayPoss(position);
            } else {
                toPlayPos(position);
            }
        }
    }

    public void toPlayPoss(int position) {
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom", context);
            if (!playFrom.equals("hezuo") || MainService.ids.size() == 0) {
                if (MainService.ids.size() > 0)
                    MainService.ids.clear();
                for (WorksData worksData : mDataList) {
                    MainService.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", "hezuo");
        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom", context);
            if (!playFrom.equals(MyCommon.SEARCH) || MainService.ids.size() == 0) {
                if (MainService.ids.size() > 0)
                    MainService.ids.clear();
                for (WorksData worksData : mDataList) {
                    MainService.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", MyCommon.SEARCH);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchPresenter != null)
            searchPresenter.cancelRequest();
    }
}
