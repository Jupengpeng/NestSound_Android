package com.xilu.wybz.ui.mine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/24.
 */
public class DraftAdapter extends BaseAdapter {

    Context mContext;

    public DraftAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_draft, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        WorksData worksData = new WorksData();
        viewHolder.onBindData(worksData,position);

        return viewHolder.rootView;
    }

    static class ViewHolder {

        public View rootView;

        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_warn)
        ImageView ivWarn;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_send)
        TextView tvSend;

        ViewHolder(View view) {
            this.rootView = view;
            ButterKnife.bind(this, view);
        }

        public void onBindData(WorksData worksData, int position){

            if (position == 1){
                setLyricType();
            }
            if (position == 2){
                setSongType();
            }
            if (position == 3){
                setInspirationType();
            }

        }

        public void setLyricType(){
            tvType.setText("歌词");
            tvType.setBackgroundResource(R.drawable.type_blue);
        }
        public void setSongType(){
            tvType.setText("歌曲");
            tvType.setBackgroundResource(R.drawable.type_orang);
        }
        public void setInspirationType(){
            tvType.setText("灵感记录");
            tvType.setBackgroundResource(R.drawable.type_green);
        }


    }
}
