package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.Banner;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;

import java.util.List;

/**
 * Created by June on 2015/12/4.
 */
public class ImageAdapter extends PagerAdapter {
    public Context context;
    public List<Banner> mList;
    int itemWidth, itemHeight;

    public ImageAdapter(Context context, List<Banner> list) {
        this.context = context;
        mList = list;
        itemWidth = DensityUtil.getScreenW(context);
        itemHeight = DensityUtil.getScreenW(context) * 28 / 75;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Warning：不要在这里调用removeView
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        if(mList!=null&&mList.size()>0) {
            position %= mList.size();
            if (position < 0) {
                position = mList.size() + position;
            }
        }
        View view = LayoutInflater.from(context).inflate(R.layout.view_home_banner_item, null);
        SimpleDraweeView mDraweeView = (SimpleDraweeView) view.findViewById(R.id.iv_cover);
        mDraweeView.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        String url = mList.get(position).pic;
        ImageLoadUtil.loadImage(url, mDraweeView);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        final int positionId = position;
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = positionId;
                    onItemClickListener.onItemClick(view, pos);
                }
            });
        }
        return view;
    }
}