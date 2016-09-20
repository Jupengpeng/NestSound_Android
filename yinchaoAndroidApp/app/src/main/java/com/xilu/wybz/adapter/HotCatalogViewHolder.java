package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.song.MakeHotActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.view.pull.section.SectionData;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/3.
 */
public class HotCatalogViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {
    private List<SectionData<HotCatalog>> mDataList;
    private Context mContext;
    private int itemWidth;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    public boolean flash = false;
    String aid;
    public HotCatalogViewHolder(View view, Context context, List<SectionData<HotCatalog>> sectionDatas, int column, String aid) {
        super(view);
        mContext = context;
        this.aid = aid;
        mDataList = sectionDatas;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
        ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
    }

    @Override
    public void onBindViewHolder(int position) {
        HotCatalog hotCatalog = mDataList.get(position).t;
        if (hotCatalog != null) {
            String url = MyCommon.getImageUrl(hotCatalog.categorypic, itemWidth, itemWidth);
            ImageLoadUtil.loadImage(url, ivCover, itemWidth, itemWidth);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String type = "";
        if (position==1) {
            type = "new";
        } else if (position==2) {
            type = "hot";
        } else {
            type = "";
        }
        MakeHotActivity.toMakeHotActivity(mContext, mDataList.get(position).t, flash, aid, type);
    }

}
