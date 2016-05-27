package com.xilu.wybz.view.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
    public void onBindViewHolder(int position){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, position);
            }
        });
    };
    public abstract void onItemClick(View view, int position);
}
