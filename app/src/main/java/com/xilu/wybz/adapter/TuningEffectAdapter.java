package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;

import java.util.List;

public class TuningEffectAdapter extends RecyclerView.Adapter<TuningEffectAdapter.EffectViewHolder> {
    private List<EffectBean> mList;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public TuningEffectAdapter(Context context, List<EffectBean> dataList) {
        this.context = context;
        this.mList = dataList;

        mOnItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for (EffectBean bean:mList){
                    bean.selected = false;
                }
                mList.get(position).selected = true;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public int getSelectedPosition(){
        for (int i=0;i<mList.size();i++){
            EffectBean bean = mList.get(i);
            if (bean.selected){
                return i;
            }
        }
        return -1;
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public EffectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EffectViewHolder holder = new EffectViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song_tuning_effect, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(EffectViewHolder holder, int position){

        EffectBean item = mList.get(position);
        holder.effectIcon.setImageResource(item.iconId);
        holder.effectIcon.setSelected(item.selected);
        holder.effectName.setText(item.name);

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

    /**
     * EffectBean
     */
    public static class EffectBean{
        public String name="";
        public int iconId;
        public boolean selected = false;

        public EffectBean(String name, int iconId) {
            this.name = name;
            this.iconId = iconId;
        }

        public EffectBean(String name, int iconId, boolean selected) {
            this.name = name;
            this.iconId = iconId;
            this.selected = selected;
        }
    }

    class EffectViewHolder extends RecyclerView.ViewHolder {

        public ImageView effectIcon;
        public TextView effectName;

        public EffectViewHolder(View view) {
            super(view);

            effectIcon = (ImageView) view.findViewById(R.id.item_effect_icon);
            effectName = (TextView) view.findViewById(R.id.item_effect_name);
        }

    }
}
