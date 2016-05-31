package com.xilu.wybz.ui.msg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.presenter.MsgCollectionPresenter;
import com.xilu.wybz.ui.IView.ICollectionView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgFavActivity extends BaseListActivity<CollectionBean> implements ICollectionView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无收藏";
    int nodatares = R.drawable.ic_nofav;
    private MsgCollectionPresenter collectionPresenter;

    @Override
    protected void initPresenter() {
        collectionPresenter = new MsgCollectionPresenter(this, this);
        collectionPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("收藏");
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
        collectionPresenter.loadData(page++);
    }


    @Override
    public void showCollectionData(List<CollectionBean> collectionBeans) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(collectionBeans);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_zambia, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_username)
        TextView tvUserName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_music_name)
        TextView tvMusicName;
        @Bind(R.id.tv_content)
        TextView tvContent;

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            final CollectionBean zanbiaBean = mDataList.get(position);
            tvTime.setText(DateTimeUtil.timestamp2DateTime(zanbiaBean.intabletime));
            tvUserName.setText(zanbiaBean.nickname);
            tvAuthor.setText(zanbiaBean.author);
            if(StringUtil.isEmpty(zanbiaBean.title))zanbiaBean.title="未命名";
            tvMusicName.setText(zanbiaBean.title);
            tvContent.setText("收藏了你的作品");
            loadImage(zanbiaBean.pic, ivCover);
            String headUrl = zanbiaBean.headerurl;
            loadImage(headUrl, ivHead);
        }
        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
