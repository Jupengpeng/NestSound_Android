package com.xilu.wybz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.LibArr;

import java.util.List;

public class StringAdapter extends ArrayAdapter<LibArr> {

    LayoutInflater layoutInflater;
    int itemId;
    List<LibArr> dataList;

    public StringAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.layoutInflater = LayoutInflater.from(context);
        this.itemId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public LibArr getItem(int position) {
        return dataList.get(position);
    }

    public void setData(List<LibArr> addList) {
        this.dataList = addList;
        notifyDataSetChanged();
    }

    public void cleanData() {
        this.dataList = null;
        notifyDataSetChanged();
    }

    public List<LibArr> getData() {
        return dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(itemId, null);
        }
        String msg = getItem(position).lyrics;
        TextView tv_title = BaseViewHolder.get(convertView, R.id.item_tv_title);
        tv_title.setText((position + 1) + "." + msg);
        return convertView;
    }

}
