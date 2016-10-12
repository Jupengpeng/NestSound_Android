package com.xilu.wybz.ui.preserve;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ProductInfo;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.DefaultListPresenter;
import com.xilu.wybz.ui.IView.IDefaultListView;
import com.xilu.wybz.ui.fragment.BaseListFragment;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.DividerItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by June on 16/5/7.
 *
 *
 *
 */
public class ProductsFragment extends BaseListFragment<ProductInfo> implements IDefaultListView<ProductInfo> {

    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;


    int nodatares = R.drawable.ic_nocomment;

    private DefaultListPresenter<ProductInfo> defaultListPresenter;

    Map<String, String> param = new HashMap<>();


    public int viewType = 1;
    private boolean loading = false;

    @Override
    protected void initPresenter() {
        defaultListPresenter = new DefaultListPresenter<>(context, this);
        defaultListPresenter.setUrl(MyHttpClient.getProductAllList());
        defaultListPresenter.resultType = new TypeToken<List<ProductInfo>>(){}.getType();
        param.put("uid", ""+PrefsUtil.getUserId(context));
        param.put("sort_id", ""+viewType);

        defaultListPresenter.setParams(param);
        defaultListPresenter.mockAble = false;
        defaultListPresenter.init();
    }

    /**
     *
     */
    @Override
    public void initView() {
        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#ffffffff"));
        recycler.enableLoadMore(false);
        recycler.enablePullToRefresh(true);

        tvNoData.setText("没有可以保全的作品！");
        ivNoData.setImageResource(nodatares);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        if (viewType == 1){
            recycler.setRefreshing();
        }

    }

    public void loadData(){
        if (!loading || mDataList.size()==0){
            loading = true;
            showDataView();
            recycler.setRefreshing();
        }
    }


    @Override
    public boolean hasPadding() {
        return false;
    }


    @Override
    public void onSuccess(List<ProductInfo> list) {
        recycler.onRefreshCompleted();

        if (list != null && list.size() > 0) {
            mDataList.addAll(list);
            adapter.notifyDataSetChanged();
        } else {
//            ToastUtils.toast(context, "没有更多数据");
        }
        checkData();
    }

    @Override
    public void onError(String message) {
        recycler.onRefreshCompleted();
        checkData();
    }

    @Override
    public void onRefresh(int action) {

        super.onRefresh(action);

        defaultListPresenter.getData(page++);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_all, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {

        int position;

        ProductInfo info;

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;


        public SampleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.type = viewType;
                    ApplyPreserveActivity.start(context,info);
                    getActivity().finish();
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {
            this.position = position;
            this.info = mDataList.get(position);

            name.setText(info.title);
            time.setText(DateFormatUtils.formatX1(info.createTime));
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {

        int split = DensityUtil.dp2px(context, 0.6f);
        return new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, split, Color.parseColor("#e5e5e5"), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (defaultListPresenter != null)
            defaultListPresenter.cancelRequest();
    }

}
