package com.xilu.wybz.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

/**
 * Created by hujunwei on 16/4/14.
 */
public class HotListAdapter extends WyBaseAdapter<TemplateBean> {
    ITemplateMusicListener iml;
    ImageView currIv;
    int itemWidth;
    int itemHeight;
    int playCount;//通过看它的值是否大于0来判断搜索页 有没有播放新的歌曲

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
        ImageView iv_play = BaseViewHolder.get(convertView, R.id.iv_play);
        TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_title);
        TextView tv_author = BaseViewHolder.get(convertView,R.id.tv_author);
        tv_title.setText(templateBean.title);
        tv_author.setText(templateBean.author);
        loadImage(templateBean.pic, iv_cover, itemWidth, itemHeight);
        if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id) && PlayMediaInstance.getInstance().status == 3) {
            currIv = iv_play;
            iv_play.setImageResource(R.drawable.ic_bz_pause);
        }
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id)) {
                    if (iml != null) {
                        currIv = iv_play;
                        if (PlayBanZouInstance.getInstance().status == 3) {
                            currIv.setImageResource(R.drawable.ic_bz_play);
                            iml.onPauseMusic();
                        } else {
                            currIv.setImageResource(R.drawable.ic_bz_pause);
                            iml.onResumeMusic();
                        }
                    }
                } else {
                    iv_play.setImageResource(R.drawable.ic_bz_pause);
                    MyApplication.musicId = templateBean.id;
                    if (iml != null) {
                        playCount++;
                        iml.onPlayMusic(templateBean);
                        if (currIv != null && currIv != iv_play) {
                            currIv.setImageResource(R.drawable.ic_bz_play);
                        }
                        currIv = iv_play;
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void updateData() {
        if (PlayBanZouInstance.getInstance().status != 3 && currIv != null) {
            currIv.setImageResource(R.drawable.ic_bz_play);
        }
    }

    public void setITemplateMusicListener(ITemplateMusicListener iml) {
        this.iml = iml;
    }
}
