package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/3.
 */
public class InspireRecordViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {
    int itemWidth, itemHeight;
    List<WorksData> mDataList;
    Context mContext;
    String COME;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.date_day)
    TextView tvDateDay;
    @Bind(R.id.date_time)
    TextView tvDateTime;
    @Bind(R.id.iv_frequency_icon)
    ImageView ivFrequencyIcon;
    @Bind(R.id.iv_mb)
    SimpleDraweeView ivMb;
    @Bind(R.id.iv_frequency)
    ImageView ivFrequency;
    @Bind(R.id.tv_text)
    TextView tvText;

    public InspireRecordViewHolder(View view, Context context, List<WorksData> worksDataList, String from) {
        super(view);
        mDataList = worksDataList;
        COME = from;
        mContext = context;
        itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 20);
        itemHeight = itemWidth / 3;
        ivCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        flMain.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, itemHeight));
        ivFrequency.setPadding(0, 0, 0, itemHeight / 6);
    }

    @Override
    public void onBindViewHolder(int position) {
        WorksData worksData = mDataList.get(position);
        tvDateDay.setText(DateFormatUtils.prase2DateText(worksData.createdate).getDay());
        tvDateTime.setText(DateFormatUtils.prase2DateText(worksData.createdate).getYearMonth());
        boolean hasAudio = StringUtil.isNotBlank(worksData.audio);
        boolean hasText = StringUtil.isNotBlank(worksData.spirecontent);

        boolean hasPic = false;
        if (StringUtil.isNotBlank(worksData.pics)) {
            String[] pics = worksData.pics.split(",");
            hasPic = pics.length > 0;
        }
        //判断类型
        if (hasText) {//有文字
            ivFrequency.setVisibility(View.GONE);
            tvText.setText(worksData.spirecontent);
            if (hasAudio) {
                ivFrequencyIcon.setVisibility(View.VISIBLE);
            } else {
                ivFrequencyIcon.setVisibility(View.GONE);
            }
        } else {
            ivFrequencyIcon.setVisibility(View.GONE);
            if (hasAudio) {
                ivFrequency.setVisibility(View.VISIBLE);
            } else {
                ivFrequency.setVisibility(View.GONE);
            }
            tvText.setText("");
        }
        if (hasPic) {//修改字体颜色
            ivMb.setVisibility(View.VISIBLE);
            tvDateTime.setTextColor(0xffffffff);
            tvDateDay.setTextColor(0xffffffff);
            tvText.setTextColor(0xffffffff);
            ivFrequencyIcon.setImageResource(R.drawable.ic_user_frequency_white);
            ivFrequency.setImageResource(R.drawable.user_frequency_white);
            String url = MyCommon.getImageUrl(worksData.pics.split(",")[0], itemWidth, itemHeight);
            ImageLoadUtil.loadImage(url, ivCover);
        } else {
            ivMb.setVisibility(View.GONE);
            tvDateTime.setTextColor(0xff999999);
            tvDateDay.setTextColor(0xff999999);
            tvText.setTextColor(0xff666666);
            ivFrequencyIcon.setImageResource(R.drawable.ic_user_frequency);
            ivFrequency.setImageResource(R.drawable.user_frequency_yellow);
            ivCover.setImageResource(R.drawable.transparent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
//        if (mDataList.get(position).status == 1) {
//            toPlayPos(position);
//        } else {
//            LyricsdisplayActivity.toLyricsdisplayActivity(mContext, mDataList.get(position).getItemid(), 0, mDataList.get(position).name);
//        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom", mContext);
            if (!playFrom.equals(COME) || MyApplication.ids.size() == 0) {
                if (MyApplication.ids.size() > 0)
                    MyApplication.ids.clear();
                for (WorksData worksData : mDataList) {
                    MyApplication.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", COME, position);
        }
    }
}
