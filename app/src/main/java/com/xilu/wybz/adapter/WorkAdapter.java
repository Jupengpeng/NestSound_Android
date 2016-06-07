package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by hujunwei on 16/6/7.
 */
public class WorkAdapter extends WyBaseAdapter<WorksData>{
    public WorkAdapter(Context context, List<WorksData> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item,null);
        }
        return convertView;
    }
}
