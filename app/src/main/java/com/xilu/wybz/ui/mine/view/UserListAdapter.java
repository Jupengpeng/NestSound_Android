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
public class UserListAdapter extends BaseListAdapter {

    protected Context mContext;

    protected List<WorksData> datas;

    public UserListAdapter(Context context) {
        this.mContext = context;
        datas = new ArrayList<WorksData>();
    }

    public UserListAdapter(Context context,List<WorksData> data) {
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

    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.view_test, parent,false);

        return new UserViewHolder(view);
    }


    public class UserViewHolder extends BaseViewHolder {

        public UserViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
