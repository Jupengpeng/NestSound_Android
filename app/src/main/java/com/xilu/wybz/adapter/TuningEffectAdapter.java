package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xilu.wybz.R;

import java.util.ArrayList;
import java.util.List;

public class TuningEffectAdapter extends RecyclerView.Adapter<TuningEffectAdapter.EffectViewHolder> {
    private List<EffectBean> mList;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    List<EffectViewHolder> holders = new ArrayList<>();

    public TuningEffectAdapter(Context context, List<EffectBean> dataList) {
        this.context = context;
        this.mList = dataList;

        mOnItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                for (EffectBean bean:mList){
//                    bean.selected = false;
//                }
//                mList.get(position).selected = true;
//                notifyDataSetChanged();
//                showProgressbar(position);
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setSelected(int position){

        if (position < 0 && position >= mList.size() ){
            return;
        }
        for (EffectBean bean:mList){
            bean.selected = false;
        }
        mList.get(position).selected = true;
        notifyDataSetChanged();
    }

    public void showProgressbar(int position){
        if (position < 0 && position >= holders.size() ){
            return;
        }
        holders.get(position).ivPlayProgressbar.setVisibility(View.VISIBLE);

    }

    public void hideProgressbar(int position){
        if (position < 0 && position >= holders.size() ){
            return;
        }
        holders.get(position).ivPlayProgressbar.setVisibility(View.GONE);
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
        holders.add(holder);
        Log.e("url","onCreateViewHolder:"+holder.hashCode());
        return holder;
    }

    @Override
    public void onBindViewHolder(EffectViewHolder holder, int position){

        EffectBean item = mList.get(position);
        holder.effectIcon.setImageResource(item.iconId);
        holder.effectIcon.setSelected(item.selected);
        holder.effectName.setText(item.name);
        holder.poseition = position;
        holder.itemView.setTag(holder);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);

//                    ViewPropertyAnimator anim1 = holder.ivPlayProgressbar.animate();
//                    if (mList.get(position).anim){
//                        mList.get(position).anim = false;
//                        holder.ivPlayProgressbar.setVisibility(View.GONE);
//                    } else {
//                        mList.get(position).anim = true;
//                        holder.ivPlayProgressbar.setVisibility(View.VISIBLE);
//                    }

                    Log.e("url","setOnClickListener:"+holder.hashCode());
//                    ObjectAnimator anim = ObjectAnimator//
//                            .ofFloat(holder.effectIcon, "rotation", 0.0F, 360.0F)//
//                            .setDuration(2000);
//                    anim.setRepeatCount(Animation.INFINITE);
//                    anim.setInterpolator(new LinearInterpolator());
//                    anim.setRepeatMode(Animation.RESTART);
//                    anim.start();

//                    ainm = AnimationUtils.loadAnimation(content,R.anim.rotates);
//                    ainm.setRepeatCount(1000);
//                    ainm.setRepeatMode(Animation.RESTART);
//                    ainm.setRepeatMode(Animation.INFINITE);
//                    holder.effectIcon.startAnimation(ainm);
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
        public boolean anim = false;


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
        public ProgressBar ivPlayProgressbar;
        public int poseition;

        public EffectViewHolder(View view) {
            super(view);

            effectIcon = (ImageView) view.findViewById(R.id.item_effect_icon);
            effectName = (TextView) view.findViewById(R.id.item_effect_name);
            ivPlayProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        }

    }
}
