package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActionBean;
import java.util.List;

/**
 * Created by June on 16/5/6.
 */
public class ActionAdapter extends YcBaseAdapter<ActionBean> {
    public ActionAdapter(Context context, List<ActionBean> list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.ll_actionview,null);
        }
        TextView tv_title = BaseViewHolder.get(convertView,R.id.tv_title);
        String title = mList.get(position).getTitle();
        if(title.equals("删除")) {
            tv_title.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            tv_title.setTextColor(0xff444444);
        }
        tv_title.setText(title);
        return convertView;
    }
}
