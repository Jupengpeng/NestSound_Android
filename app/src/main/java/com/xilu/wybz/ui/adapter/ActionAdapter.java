package com.xilu.wybz.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.BaseViewHolder;
import com.xilu.wybz.adapter.WyBaseAdapter;
import com.xilu.wybz.bean.ActionBean;
import java.util.List;

/**
 * Created by June on 16/5/6.
 */
public class ActionAdapter extends WyBaseAdapter<ActionBean> {
    public ActionAdapter(Context context, List<ActionBean> list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.ll_actionview,null);
        }
        TextView tv_title = BaseViewHolder.get(convertView,R.id.tv_title);
        tv_title.setText(mList.get(position).getTitle());
        return convertView;
    }
}
