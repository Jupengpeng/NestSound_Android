package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.utils.StringUtil;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public class PlayLyricsAdapter extends WyBaseAdapter<String> {
    public PlayLyricsAdapter(Context context, List<String> list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.ll_lyrics_item,null);
        }
        TextView tv_content = BaseViewHolder.get(convertView,R.id.tv_content);
        if(StringUtil.isNotBlank(mList.get(position))){
            tv_content.setText(mList.get(position).trim());
        }else{
            tv_content.setText("");
        }
        return convertView;
    }
}
