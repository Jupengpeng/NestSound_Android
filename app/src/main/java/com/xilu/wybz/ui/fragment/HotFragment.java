package com.xilu.wybz.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.song.MakeSongActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

public class HotFragment extends BaseListFragment<TemplateBean> implements IHotView {
    public static final String TYPE = "type";
    private int type;
    private HotPresenter hotPresenter;
    private SampleViewHolder sampleViewHolder;
    private int itemWidth;
    private int itemHeight;
    private int playPos = -1;
    private Intent serviceIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
        }
    }
    public void loadData(String name){
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
            adapter.notifyDataSetChanged();
        }
        keyWord = name;
        page = 1;
        llNoData.setVisibility(View.GONE);
        recycler.setRefreshing();
    }
    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        hotPresenter = new HotPresenter(context, this);
        hotPresenter.init();
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
        tvNoData.setText("暂无搜索结果！");
        ivNoData.setImageResource(R.drawable.ic_nosearch);
    }

    public static HotFragment newInstance(int type) {
        HotFragment tabFragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        if (type ==0||type==1) {
            recycler.setRefreshing();
        }
    }

    @Override
    public void onRefresh(int action) {
        hotPresenter.loadHotData(keyWord, type + 1, page++);
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens,int currentType) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDestroy)return;
                if(mDataList==null)
                mDataList = new ArrayList<>();
                if(mDataList.size()==0){
                    EventBus.getDefault().post(new Event.HideKeyboardEvent());
                }
                if (recycler == null){
                    return;
                }
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                mDataList.addAll(templateBeens);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        },600);
    }

    @Override
    public void loadFail() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void downloadSuccess() {

    }

    @Override
    public void loadNoData() {
        if(isDestroy)return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bz_list_item, parent, false);
        sampleViewHolder = new SampleViewHolder(view);
        return sampleViewHolder;
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
        @Bind(R.id.pb_play)
        ProgressBar pbPlay;


        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 40)) / 2;
            itemHeight = itemWidth * 172 / 326;
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        }


        @Override
        public void onBindViewHolder(int position) {
            TemplateBean templateBean = mDataList.get(position);
            itemView.setTag(templateBean);
            tvTitle.setText(templateBean.title);
            tvAuthor.setText(templateBean.author);
            if (StringUtil.isNotBlank(templateBean.pic)) {
                String imageUrl = MyCommon.getImageUrl(templateBean.pic, itemWidth, itemHeight);
                loadImage(imageUrl, ivCover);
            }
            if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id)) {
                templateBean.playStatus = PlayMediaInstance.getInstance().status;
            }
            ivPlay.setImageResource(templateBean.playStatus == 3 ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
            ivPlay.setVisibility(templateBean.playStatus == 1 ? View.GONE : View.VISIBLE);
            pbPlay.setVisibility(templateBean.playStatus != 1 ? View.GONE : View.VISIBLE);
            rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    musicBinder.toPPMusic();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if (PlayMediaInstance.getInstance().status == 3) {
                PlayMediaInstance.getInstance().stopMediaPlay();
                adapter.notifyItemChanged(position);
            }
            TemplateBean bean = mDataList.get(position);
            MakeSongActivity.toMakeSongActivity(context, bean);
        }

        public void updatePlayStatus() {

        }
    }

    public void playTemplateMusic() {
//        if (serviceIntent == null) {
//            serviceIntent = new Intent(getActivity(), PlayService.class);
//            getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(hotPresenter!=null)
            hotPresenter.cancelRequest();
    }
}
