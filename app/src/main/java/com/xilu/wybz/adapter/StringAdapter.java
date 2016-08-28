package com.xilu.wybz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;

import java.util.List;

public class StringAdapter extends YcBaseAdapter<String> {

    public StringAdapter(Context context, List<String> list) {
        super(context, list);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lrc, null);
        }
        String msg = mList.get(position);
        TextView tv_title = BaseViewHolder.get(convertView, R.id.item_tv_title);
        tv_title.setText((position + 1) + "." + msg);
        return convertView;
    }

}
