package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicBean;
import com.xilu.wybz.utils.DensityUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class AllSongAdapter extends YcBaseAdapter<MusicBean> {
    int itemWidth;

    public AllSongAdapter(Context context, List<MusicBean> list) {
        super(context, list);
        itemWidth = (screenWidth - DensityUtil.dip2px(context, 30)) / 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_home_work_item, null);
        }
        MusicBean recommendBean = mList.get(position);
        SimpleDraweeView mDraweeView = BaseViewHolder.get(convertView, R.id.iv_cover);
        mDraweeView.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        String url = recommendBean.getPic();
        loadImage(url, mDraweeView, itemWidth, itemWidth);
        TextView count = BaseViewHolder.get(convertView, R.id.tv_count);
        count.setText(recommendBean.getLooknum() + "");
        TextView name = BaseViewHolder.get(convertView, R.id.tv_name);
        name.setText(recommendBean.getTitle());
        TextView author = BaseViewHolder.get(convertView, R.id.tv_author);
        author.setText(recommendBean.getAuthor());
        return convertView;
    }
}
