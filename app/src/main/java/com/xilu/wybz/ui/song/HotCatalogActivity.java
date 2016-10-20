package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SelectAdapter;
import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.HotCatalogPresenter;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotCatalogView;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FormatHelper;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.section.SectionData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/8/8.
 */
public class HotCatalogActivity extends BaseSectionListActivity<TemplateBean> implements IHotCatalogView, IHotView {
    @Bind(R.id.ll_select)
    LinearLayout llSelect;
    @Bind(R.id.flow_cover)
    View flowCover;
    public static final String FLASH_TAG = "FLASH_TAG";
    @Bind(R.id.catalog_recyler)
    RecyclerView catalogRecyler;
    @Bind(R.id.order_recyler)
    RecyclerView orderRecyler;
    private int itemWidth;
    private int itemHeight;
    private int playPos = -1;
    private int column = 1;
    private int column2 = 4;
    private int cid = 0;
    private HotCatalogPresenter hotCatalogPresenter;
    private HotPresenter hotPresenter;
    private HotBean hotBean;
    private boolean flash = false;
    private String aid;
    private String type = "new";
    private HotCatalog typeBean;
    private SelectAdapter adapter1, adapter2;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_hotcatalog_list;
    }

    public static void toHotCatalogActivity(Context context, boolean flash) {
        Intent intent = new Intent(context, HotCatalogActivity.class);
        intent.putExtra(FLASH_TAG, flash);
        context.startActivity(intent);
    }

    public static void toHotCatalogActivity(Context context, String aid) {
        Intent intent = new Intent(context, HotCatalogActivity.class);
        intent.putExtra(KeySet.KEY_ID, aid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            flash = intent.getBooleanExtra(FLASH_TAG, false);
            aid = intent.getStringExtra(KeySet.KEY_ID);
        }

        int space10 = DensityUtil.dip2px(context, 10);

        catalogRecyler.setNestedScrollingEnabled(false);
        catalogRecyler.setLayoutManager(new GridLayoutManager(context, column2));
        catalogRecyler.addItemDecoration(new GridSpacingItemDecoration(column2, space10, false));

        orderRecyler.setNestedScrollingEnabled(false);
        orderRecyler.setLayoutManager(new GridLayoutManager(context, column2));
        orderRecyler.addItemDecoration(new GridSpacingItemDecoration(column2, space10, false));
    }

    @Override
    protected void initPresenter() {
        hotCatalogPresenter = new HotCatalogPresenter(this, this);
        hotCatalogPresenter.init();


        hotPresenter = new HotPresenter(context, this);
    }

    @Override
    public void initView() {
        setTitle("原唱伴奏");
        hideRight();
        setUpData();
        recycler.enablePullToRefresh(true);
//        hotBean = PrefsUtil.getHotBean(context);
        if (hotBean != null && hotBean.simplesing != null) {
            mDataList = new ArrayList<>();
            showHotCatalog(hotBean);
        } else {
            hotCatalogPresenter.loadData(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ImportHotEvent event) {
        if (flash) {
            finish();
        }
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bz_list_item, parent, false);
        SampleViewHolder sampleViewHolder = new SampleViewHolder(view);
        return sampleViewHolder;
    }

    public BaseViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.hot_headview, parent, false);
        return new SectionHeaderViewHolder(view);
    }

    @OnClick(R.id.flow_cover)
    public void onCoverClick() {
        hideSelect();
    }

    @OnClick(R.id.tv_submit)
    public void onSubmitClick() {
        hideSelect();
        page = 1;
        recycler.setRefreshing();
    }
    @OnClick(R.id.ll_select)
    public void onSelectClick() {
    }
    public class SectionHeaderViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_qc)
        SimpleDraweeView ivQc;

        public SectionHeaderViewHolder(View view) {
            super(view);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (hotBean != null) {
                loadImage(hotBean.simplesing.pic, ivQc);
            } else {
                loadImage("res:///" + R.drawable.ic_qc_bg, ivQc);
            }
            ivQc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hotBean != null) {
                        if (flash) {
                            EventBus.getDefault().post(new Event.ImportHotEvent(hotBean.simplesing));
                            finish();
                        } else {
                            hotBean.simplesing.aid = aid;
                            MakeSongActivity.toMakeSongActivity(context, hotBean.simplesing);
                        }
                    }
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    protected ILayoutManager getLayoutManager() {
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(context, column);
        return myGridLayoutManager;
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        hotPresenter.loadHotData(cid, type, page++);
    }

    @Override
    public void showHotCatalog(HotBean hotBean) {
        if (isDestroy) return;
        this.hotBean = hotBean;
        PrefsUtil.saveHotBean(context, hotBean);
        if (hotPresenter == null) {
            hotPresenter = new HotPresenter(context, this);
            recycler.setRefreshing();
        }
        if (adapter1 == null) {
            List<HotCatalog> hotCatalogs = new ArrayList<>();
            hotCatalogs.addAll(hotBean.list);
            if (hotCatalogs.size()>0){
                HotCatalog catalog = hotCatalogs.get(0);
                catalog.isCheck = true;

            }

            List<HotCatalog> types = new ArrayList<>();
            typeBean = new HotCatalog();
            typeBean.categoryname = "最新";
            typeBean.isCheck = true;
            types.add(typeBean);

            typeBean = new HotCatalog();
            typeBean.categoryname = "最热";
            typeBean.isCheck = false;
            types.add(typeBean);
            adapter1 = new SelectAdapter(context, hotCatalogs, column2);
            adapter1.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    for (int i = 0; i < hotCatalogs.size(); i++) {
                        hotCatalogs.get(i).isCheck = (i == position);
                    }
                    adapter1.notifyDataSetChanged();
                    cid = hotCatalogs.get(position).id;
                }
            });
            catalogRecyler.setAdapter(adapter1);
            adapter2 = new SelectAdapter(context, types, column2);
            adapter2.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    for (int i = 0; i < types.size(); i++) {
                        types.get(i).isCheck = (i == position);
                    }
                    adapter2.notifyDataSetChanged();
                    type = position == 0 ? "new" : "hot";
                }
            });
            orderRecyler.setAdapter(adapter2);
        }
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        @Bind(R.id.rl_play)
        RelativeLayout rlPlay;
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_times)
        TextView tvTimes;

        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 40)) / 2;
            itemHeight = itemWidth * 172 / 326;
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        }


        @Override
        public void onBindViewHolder(int position) {
            TemplateBean templateBean = mDataList.get(position).t;
            itemView.setTag(templateBean);
            tvTitle.setText(templateBean.title);
            tvAuthor.setText(templateBean.author);
            tvTimes.setText(" " + FormatHelper.formatDuration(templateBean.mp3times));
            tvCount.setText("使用次数:" + NumberUtil.format(templateBean.usenum));
            if (StringUtils.isNotBlank(templateBean.pic)) {
                String imageUrl = MyCommon.getImageUrl(templateBean.pic, itemWidth, itemHeight);
                loadImage(imageUrl, ivCover);
            }
            ivPlay.setImageResource(templateBean.isPlay ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
            rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApplication.getInstance().getMainService() != null) {
                        if (playPos >= 0 && playPos != position) {//切换伴奏 重置上一首的状态
                            MyApplication.getInstance().mMainService.doRelease();
                            MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).t.mp3, type + "_hot");
                            mDataList.get(position).t.isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_bz_pause);
                            playPos = position;//重新赋值当前播放的位置
                        } else if (playPos >= 0 && playPos == position) {//播放当前
                            mDataList.get(playPos).t.isPlay = !mDataList.get(playPos).t.isPlay;
                            ivPlay.setImageResource(mDataList.get(playPos).t.isPlay
                                    ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
                            MyApplication.getInstance().mMainService.doPP(mDataList.get(playPos).t.isPlay);
                        } else {//初此播放
                            MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).t.mp3, type + "_hot");
                            mDataList.get(position).t.isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_bz_pause);
                            playPos = position;//重新赋值当前播放的位置
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            doStop();
            TemplateBean bean = mDataList.get(position).t;
            bean.aid = aid;
            if (flash) {
                EventBus.getDefault().post(new Event.ImportHotEvent(bean));
                finish();
            } else {
                MakeSongActivity.toMakeSongActivity(context, bean);
            }
        }
    }

    public void doStop() {
        if (playPos >= 0) {
            mDataList.get(playPos).t.isPlay = false;
            updateItem(playPos);
            playPos = -1;
        }
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                if (mDataList.size() == 0) {
                    mDataList.add(new SectionData<>(true, 0, "清唱"));
                }
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                for (TemplateBean templateBean : templateBeens) {
                    mDataList.add(new SectionData(templateBean));
                }
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        showNetErrorMsg();
    }

    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.enableLoadMore(false);
    }

    @Override
    public void downloadSuccess() {

    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_catalog:
                if (llSelect.getVisibility() == View.GONE) {
                    showSelect();
                } else {
                    hideSelect();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelect() {
        llSelect.setVisibility(View.VISIBLE);
        llSelect.startAnimation(AnimationUtils.loadAnimation(context, R.anim.top_in_anim));
        flowCover.setVisibility(View.VISIBLE);
        flowCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    private void hideSelect() {
        llSelect.setVisibility(View.GONE);
        llSelect.startAnimation(AnimationUtils.loadAnimation(context, R.anim.top_out_anim));
        flowCover.setVisibility(View.GONE);
        flowCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
    }
}
