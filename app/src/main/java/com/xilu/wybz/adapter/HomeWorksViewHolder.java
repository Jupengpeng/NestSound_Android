package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.section.SectionData;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/3.
 */
public class HomeWorksViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {
    private List<SectionData<WorksData>> mDataList;
    private Context mContext;
    private int itemWidth;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.iv_type)
    ImageView ivType;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.rl_cover)
    RelativeLayout rlCover;
    public HomeWorksViewHolder(View view, Context context, List<SectionData<WorksData>> sectionDatas, int column) {
        super(view);
        mContext = context;
        mDataList = sectionDatas;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
        ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
    }

    @Override
    public void onBindViewHolder(int position) {
        WorksData worksData = mDataList.get(position).t;
        if(StringUtils.isBlank(worksData.pic)){
            worksData.pic = MyHttpClient.QINIU_URL+MyCommon.getLyricsPic().get((int)(Math.random()*10));
        }
        String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
        ImageLoadUtil.loadImage(url, ivCover);
        if(StringUtils.isNotBlank(worksData.name)) {
            tvName.setText(worksData.name);
        }else{
            tvName.setText(worksData.title);
        }
        tvAuthor.setText(worksData.author);
        tvCount.setText(NumberUtil.format(worksData.looknum));
        ivType.setImageResource(worksData.type == 1 ? R.drawable.ic_song_type : R.drawable.ic_lyric_type);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        if(position>=0&&position<mDataList.size()) {
            if (mDataList.get(position).t.type == 1 || mDataList.get(position).t.type == 0) {
                toPlayPos(position);
            } else if (mDataList.get(position).t.type == 2) {
                LyricsdisplayActivity.toLyricsdisplayActivity(mContext, mDataList.get(position).t.itemid, mDataList.get(position).t.name);
            }
        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            WorksData worksData = mDataList.get(position).t;
            String playFrom = PrefsUtil.getString("playFrom", mContext);
            if (!playFrom.equals(worksData.come) || MainService.ids.size() == 0) {
                if (MainService.ids.size() > 0)
                    MainService.ids.clear();
                for (SectionData<WorksData> sectionData : mDataList) {
                    if (sectionData.t!=null&&sectionData.t.status == 1) {
                        MainService.ids.add(sectionData.t.itemid);
                    }
                }
            }
            PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", worksData.come);
        }
    }
}
