//package com.xilu.wybz.view.viewholder;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.xilu.wybz.R;
//import com.xilu.wybz.adapter.ImageAdapter;
//import com.xilu.wybz.bean.Banner;
//import com.xilu.wybz.util.DensityUtil;
//
//import java.util.List;
//
//
///**
// * Created by cfanr on 2015/12/5.
// */
//public class BannerHeader {
//    Context context;
//    public View view;
//    List<Banner> bannerList;
//    ViewPager mViewPager;
//    ViewGroup group;
//    ImageAdapter imgAdapter;
//    ImageView[] tips;
//    private int currentItem = 0;
//    //请求更新显示的View
//    protected static final int MSG_UPDATE_IMAGE = 1;
//    //请求暂停轮播
//    protected static final int MSG_KEEP_SILENT = 2;
//    //请求恢复轮播
//    protected static final int MSG_BREAK_SILENT = 3;
//    /**
//     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//     */
//    protected static final int MSG_PAGE_CHANGED = 4;
//    //轮播间隔时间
//    protected static final long MSG_DELAY = 3000;
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
//            if (mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
//                mHandler.removeMessages(MSG_UPDATE_IMAGE);
//            }
//            switch (msg.what) {
//                case MSG_UPDATE_IMAGE:
//                    currentItem++;
//                    mViewPager.setCurrentItem(currentItem);
//                    //准备下次播放
//                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_KEEP_SILENT:
//                    //只要不发送消息就暂停了
//                    break;
//                case MSG_BREAK_SILENT:
//                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_PAGE_CHANGED:
//                    //记录当前的页号，避免播放的时候页面显示不正确。
//                    currentItem = msg.arg1;
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    public BannerHeader(Context context, List<Banner> bannerList) {
//        this.context = context;
//        this.bannerList = bannerList;
//        Log.e("bannerList",bannerList.size()+"");
//        view = LayoutInflater.from(context).inflate(R.layout.view_home_banner, null);
//        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_ad);
//        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(DensityUtil.getScreenW(context), DensityUtil.getScreenW(context) * 28 / 75));
//        group = (ViewGroup) view.findViewById(R.id.linear_point);
//        setViewPager();
//    }
//
//    private void setViewPager() {
//        //添加圆点指示器
//        group.removeAllViews();
//        tips = new ImageView[bannerList.size()];
//        for (int i = 0; i < bannerList.size(); i++) {
//            ImageView imageView = new ImageView(context);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(8, 8, 8, 8);// 间隔应该是20
//            lp.gravity = Gravity.CENTER_VERTICAL;
//            imageView.setLayoutParams(lp);
//            tips[i] = imageView;
//            if (i == 0) {
//                tips[i].setBackgroundResource(R.drawable.pages_focused);
//            } else {
//                tips[i].setBackgroundResource(R.drawable.pages_unfocused);
//            }
//            group.addView(imageView);
//        }
//        imgAdapter = new ImageAdapter(context, bannerList);
//        imgAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
//        mViewPager.setAdapter(imgAdapter);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mHandler.sendMessage(Message.obtain(mHandler, MSG_PAGE_CHANGED, position, 0));
//                // 取余后的索引，得到新的page的索引
//                int newPosition = position % bannerList.size();
//                // 根据索引设置图片的描述
//                setImageBackground(newPosition);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                switch (state) {
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
//                        break;
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//
//        mViewPager.setCurrentItem(0);//默认在中间，使用户看不到边界
//        //开始轮播效果
//        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//    }
//
//    /**
//     * 设置选中的tip的背景
//     *
//     * @param selectItems
//     */
//    public void setImageBackground(int selectItems) {
//        try {
//            for (int i = 0; i < tips.length; i++) {
//                if (i == selectItems) {
//                    tips[i].setBackgroundResource(R.drawable.pages_focused);
//                } else {
//                    tips[i].setBackgroundResource(R.drawable.pages_unfocused);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //更新adapter
//    public void updateData(List<Banner> banners) {
//        if(bannerList.size()>0){
//            bannerList.clear();
//        }
//        bannerList.addAll(banners);
//        imgAdapter.notifyDataSetChanged();
//    }
//}
