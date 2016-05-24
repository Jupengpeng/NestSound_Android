package com.xilu.wybz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsListBean;

import java.util.List;


public class LyricListAdapter extends ArrayAdapter<LyricsListBean> {

    LayoutInflater layoutInflater;
    int itemId;
    List<LyricsListBean> dataList;
    Context mContext;
    String currTitle;

    public LyricListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.layoutInflater = LayoutInflater.from(context);
        this.itemId = textViewResourceId;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public LyricsListBean getItem(int position) {
        return dataList.get(position);
    }

    public void setData(List<LyricsListBean> addList) {
        this.dataList = addList;
        if (currTitle == null && addList != null && addList.size() > 0) {
            currTitle = addList.get(0).getItemtitle();
        }
        notifyDataSetChanged();
    }

    public void addData(List<LyricsListBean> addList) {
        if (this.dataList != null) {
            this.dataList.addAll(addList);
        }

        notifyDataSetChanged();
    }

    public void cleanData() {
        this.dataList = null;
        notifyDataSetChanged();
    }

    public List<LyricsListBean> getData() {
        return dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LyricHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(itemId, null);
            holder = new LyricHolder();
            holder.nameTv = (TextView) convertView
                    .findViewById(R.id.item_tv_title);

            convertView.setTag(holder);
        } else {
            holder = (LyricHolder) convertView.getTag();
        }

        final LyricsListBean bean = getItem(position);

        if (currTitle.equals(bean.getItemtitle())) {
            holder.nameTv.setSelected(true);
        } else {
            holder.nameTv.setSelected(false);
        }

        holder.nameTv.setText(bean.getItemtitle());

        return convertView;
    }

    public class LyricHolder {
        public TextView nameTv;

    }
    public void setCurrTitle(String currTitle) {
        this.currTitle = currTitle;
    }
}
