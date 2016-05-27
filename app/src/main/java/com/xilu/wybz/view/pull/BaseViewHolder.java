package com.xilu.wybz.view.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        onItemClick(itemView, getAdapterPosition());
    }
    public abstract void onBindViewHolder(int position);

    public abstract void onItemClick(View view, int position);
}
