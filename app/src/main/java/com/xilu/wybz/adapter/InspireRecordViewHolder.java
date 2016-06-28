package com.xilu.wybz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
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
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.record.ModifyInspireRecordActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

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
    OnDeleteListener onDeleteListener;
    public InspireRecordViewHolder(View view, Context context, List<WorksData> worksDataList, String from,OnDeleteListener onDeleteListener) {
        super(view);
        mDataList = worksDataList;
        COME = from;
        this.onDeleteListener = onDeleteListener;
        mContext = context;
        itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 20);
        itemHeight = itemWidth / 3;
        ivCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight+DensityUtil.dip2px(context, 20)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight+DensityUtil.dip2px(context, 20));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        flMain.setLayoutParams(params);
//        int dip10 = DensityUtil.dip2px(context,10);
//        flMain.setPadding(dip10,dip10,dip10,dip10);
        ivFrequency.setPadding(0, 0, 0, itemHeight / 6);
        if(onDeleteListener== null)return;
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v,getAdapterPosition());
                return false;
            }
        });
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
            tvDateTime.setTextColor(Color.WHITE);
            tvDateDay.setTextColor(Color.WHITE);
            tvText.setTextColor(Color.WHITE);
            ivFrequencyIcon.setImageResource(R.drawable.ic_user_frequency_white);
            ivFrequency.setImageResource(R.drawable.user_frequency_white);
            String pic = worksData.pics.split(",")[0];
            String url = MyCommon.getImageUrl(pic.startsWith("http")?pic: MyHttpClient.QINIU_URL+pic, itemWidth, itemHeight);
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v,position);
                return false;
            }
        });
    }
    public void onItemLongClick(View view, int position) {
        if(onDeleteListener==null)return;
        new MaterialDialog.Builder(mContext)
                .title(R.string.dialog_title)
                .items(R.array.states)
                .itemsColor(Color.RED)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which==0){
                            onDeleteListener.deletePos(position);
                        }
                    }
                }).show();
    }
    @Override
    public void onItemClick(View view, int position) {
        ModifyInspireRecordActivity.toModifyInspireRecordActivity(mContext, mDataList.get(position));
    }

    public interface OnDeleteListener{
        void deletePos(int pos);
    }
}
