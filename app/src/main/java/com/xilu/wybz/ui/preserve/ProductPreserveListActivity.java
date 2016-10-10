package com.xilu.wybz.ui.preserve;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.PreservationInfo;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.DefaultListPresenter;
import com.xilu.wybz.ui.IView.IDefaultListView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ProductPreserveListActivity extends BaseListActivity<PreservationInfo>
        implements IDefaultListView<PreservationInfo> {

    String nodata = "你还未保过全作品";
    int nodatares = R.drawable.ic_nocomment;

    DefaultListPresenter<PreservationInfo> defaultListPresenter;

    Map<String, String> param = new HashMap<>();


    @Override
    protected void initPresenter() {

        defaultListPresenter = new DefaultListPresenter<>(context, this);
        defaultListPresenter.setUrl(MyHttpClient.getpreservationList());

        param.put("id", ""+PrefsUtil.getUserId(context));
        defaultListPresenter.setParams(param);
        defaultListPresenter.mockAble = false;
        defaultListPresenter.init();
    }

    /**
     * initView.
     */
    @Override
    public void initView() {
        setTitle("保全列表");
        hideRight();
        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#ffffffff"));
        recycler.enablePullToRefresh(true);
        recycler.enableLoadMore(true);

        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
    }

    /**
     * setUpData.
     */
    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);


        defaultListPresenter.getData(page++);

    }


    @Override
    public void onSuccess(List<PreservationInfo> list) {
        recycler.onRefreshCompleted();

        if (list != null && list.size() > 0) {
            mDataList.addAll(list);
            adapter.notifyDataSetChanged();
        } else {

            ToastUtils.toast(context, "没有更多数据");
        }
        checkData();
    }

    @Override
    public void onError(String message) {
        recycler.onRefreshCompleted();
        checkData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        recycler.onRefreshCompleted();
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(ProductAllActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preservation_all, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    class SampleViewHolder extends BaseViewHolder {

        int position;
        PreservationInfo info;

        @Bind(R.id.icon_type)
        ImageView iconType;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.status)
        TextView status;

        public SampleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreserveInfoActivity.start(context,info.id);
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {
            this.position = position;
            info = mDataList.get(position);

            name.setText(info.worksname);
            time.setText(DateFormatUtils.formatX1(info.createtime));

            setTypeIcon(iconType,info.sort_id);
            setStatuText(status,info.statue);

        }

        private void setStatuText(TextView statuView, int statu){
            switch (statu){
                case 1:
                    statuView.setText("保全中");
                    break;
                case 2:
                    statuView.setText("保护中");
                    break;
                case 3:
                    statuView.setText("支付失败");
                default:
            }
        }

        private void setTypeIcon(ImageView typeView, int type){
            switch (type){
                case 1:
                    typeView.setImageResource(R.drawable.ic_preservation_song);
                    break;
                case 2:
                    typeView.setImageResource(R.drawable.ic_preservation_lyric);
                    break;
                case 3:
                    typeView.setImageResource(R.drawable.ic_preservation_total);
                    break;

                default:
            }
        }

        @Override
        public void onItemClick(View view, int position) {
        }
    }

}
