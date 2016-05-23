package com.xilu.wybz.ui.mine.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.view.pull.BaseListAdapter;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/22.
 */
public abstract class UserBaseAdapter extends BaseListAdapter {

    protected Context mContext;

    protected List<WorksData> datas;

    public UserBaseAdapter(Context context) {
        this.mContext = context;
        datas = new ArrayList<WorksData>();
    }

    public UserBaseAdapter(Context context, List<WorksData> data) {
        this.mContext = context;
        this.datas = data;
        if (datas == null) {
            datas = new ArrayList<WorksData>();
        }
    }

    @Override
    protected int getDataCount() {
        return datas.size();
    }

    @Override
    public int getDataViewType(int position) {
        return 0;
    }


    public void updateDatas(List<WorksData> data){
        this.datas = data;
        notifyDataSetChanged();
    }

    public void addDatas(List<WorksData> data){
        this.datas.addAll(data);
        notifyDataSetChanged();
    }


}
