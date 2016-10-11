package com.xilu.wybz.view.pull;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

import butterknife.ButterKnife;

/**
 *
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    /**
     *
     * @param itemView
     */
    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public BaseViewHolder(View view, Context context, List<WorksData> worksDataList, String from) {
        super(view);
    }
    public abstract void onBindViewHolder(int position);

    public abstract void onItemClick(View view, int position);

}
