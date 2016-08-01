package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsPoster;
import java.util.List;

/**
 * Created by hujunwei on 16/8/1.
 */
public class LyricsPosterAdapter extends YcBaseAdapter<LyricsPoster>{

    public LyricsPosterAdapter(Context context, List<LyricsPoster> list) {
        super(context, list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lrc_poster, null);
        }
        LyricsPoster lyricsPoster = mList.get(position);
        CheckedTextView tvTitle = BaseViewHolder.get(convertView, R.id.tv_title);
        tvTitle.setText(lyricsPoster.lyrics);
        tvTitle.setChecked(lyricsPoster.isChecked);
        return convertView;
    }
}
