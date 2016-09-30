package com.xilu.wybz.ui.preserve;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2016/9/29.
 */

public class PersonTypeAdapter extends BaseAdapter{

    String [] types = {"词作者","曲作者","词曲作者"};

    int type = 3;
    Context context;

    /**
     * PersonTypeAdapter.
     * @param context
     * @param type
     */
    public PersonTypeAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public Object getItem(int position) {
        if (position > types.length-1 ){
            return null;
        }
        return types[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }




}
