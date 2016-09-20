package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtils;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder>  {
    private List<HotCatalog> mList;
    private Context context;
    private int itemWidth;
    public SelectAdapter(Context context, List<HotCatalog> hotCatalogs, int column) {
        this.context = context;
        this.mList = hotCatalogs;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectViewHolder holder = new SelectViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_catalog, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(SelectViewHolder holder, int position) {
        HotCatalog hotCatalog = mList.get(position);
        if(StringUtils.isNotBlank(hotCatalog.categoryname))
        holder.tvName.setText(hotCatalog.categoryname);
        holder.tvName.setBackgroundResource(hotCatalog.isCheck?R.drawable.corner_yellow5:R.drawable.corner_e5e5e5);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SelectViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_catalog)
        TextView tvName;


        public SelectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, DensityUtil.dip2px(context,36)));
        }
    }
}
