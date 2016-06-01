package com.xilu.wybz.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtil;

import java.util.List;

/**
 * Created by hujunwei on 16/4/14.
 */
public class HotListAdapter extends WyBaseAdapter<TemplateBean> {
    private ITemplateMusicListener iml;
    private ImageView currIv;
    private ProgressBar currProgress;
    private int itemWidth;
    private int itemHeight;

    public HotListAdapter(Context context, List<TemplateBean> list) {
        super(context, list);
        itemWidth =  (DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,40))/2;
        itemHeight = itemWidth*172/326;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bz_list_item, null);
        }
        TemplateBean templateBean = mList.get(position);
        RelativeLayout rl_cover = BaseViewHolder.get(convertView, R.id.rl_cover);
        SimpleDraweeView iv_cover = BaseViewHolder.get(convertView, R.id.iv_cover);
        rl_cover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemHeight));
        ImageView ivPlay = BaseViewHolder.get(convertView, R.id.iv_play);
        RelativeLayout rlPlay = BaseViewHolder.get(convertView, R.id.rl_play);
        ProgressBar progress = BaseViewHolder.get(convertView, R.id.progress);
        TextView tvTitle = BaseViewHolder.get(convertView, R.id.tv_title);
        TextView tvAuthor = BaseViewHolder.get(convertView,R.id.tv_author);
        tvTitle.setText(templateBean.title);
        tvAuthor.setText(templateBean.author);
        if(StringUtil.isNotBlank(templateBean.pic)) loadImage(templateBean.pic, iv_cover, itemWidth, itemHeight);
        if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id)&&PlayMediaInstance.getInstance().status==3) {
            currIv = ivPlay;
            ivPlay.setImageResource(R.drawable.ic_bz_pause);
        }else{
            ivPlay.setImageResource(R.drawable.ic_bz_play);
        }
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id)) {
                    if (iml != null) {
                        currIv = ivPlay;
                        if (PlayBanZouInstance.getInstance().status == 3) {
                            currIv.setImageResource(R.drawable.ic_bz_play);
                            iml.onPauseMusic();
                        } else {
                            currIv.setImageResource(R.drawable.ic_bz_pause);
                            iml.onResumeMusic();
                        }
                    }
                } else {
                    if (iml != null) {
                        iml.onPlayMusic(templateBean);
                        if (currIv != null && currIv != ivPlay) {
                            currIv.setImageResource(R.drawable.ic_bz_play);
                        }
                        progress.setVisibility(View.VISIBLE);
                        ivPlay.setVisibility(View.GONE);
                        currProgress = progress;
                        currIv = ivPlay;
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void updateData() {
        if (PlayBanZouInstance.getInstance().status != 3 && currIv != null) {
            currIv.setImageResource(R.drawable.ic_bz_play);
        }
    }
    public void updatePlayStatus(){
        if(currProgress!=null&&currIv!=null) {
            currProgress.setVisibility(View.GONE);
            currIv.setVisibility(View.VISIBLE);
            currIv.setImageResource(R.drawable.ic_bz_pause);
        }
    }
    public void setITemplateMusicListener(ITemplateMusicListener iml) {
        this.iml = iml;
    }
}
