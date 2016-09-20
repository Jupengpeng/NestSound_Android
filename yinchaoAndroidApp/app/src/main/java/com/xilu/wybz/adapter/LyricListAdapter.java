package com.xilu.wybz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;

import java.util.List;


public class LyricListAdapter extends YcBaseAdapter<String> {
    List<String> dataList;
    Context mContext;
    String currTitle;
    public LyricListAdapter(Context context, List<String> list) {
        super(context,list);
        this.mContext = context;
        this.dataList = list;
    }
    public void setCurrTitle(String title){
        currTitle = title;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LyricHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_type, null);
            holder = new LyricHolder();
            holder.nameTv = (TextView) convertView
                    .findViewById(R.id.item_tv_title);
            convertView.setTag(holder);
        } else {
            holder = (LyricHolder) convertView.getTag();
        }
        holder.nameTv.setText(dataList.get(position));
        if (currTitle.equals(dataList.get(position))) {
            holder.nameTv.setSelected(true);
        } else {
            holder.nameTv.setSelected(false);
        }
        return convertView;
    }

    public class LyricHolder {
        public TextView nameTv;
    }
}
