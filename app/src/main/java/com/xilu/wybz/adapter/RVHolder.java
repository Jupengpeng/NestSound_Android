package com.xilu.wybz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RVHolder  extends RecyclerView.ViewHolder {


    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder=ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}