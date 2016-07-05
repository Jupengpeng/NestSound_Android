package com.xilu.wybz.ui.record;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.menuitem.SelectPicProvider;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by hujunwei on 16/6/30.
 */
public class NewSelectPicActivity extends BaseListActivity<PhotoBean> {
    int column;
    List<PhotoBean> mList;
    SelectPicProvider selectPicProvider;
    ArrayList<String> picList;
    @Override
    protected void initPresenter() {
        setTitle("选择照片");
        picList = new ArrayList<>();
        mList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            picList = bundle.getStringArrayList("pics");
            column = bundle.getInt("column");
        }
        getPics();
        hideRight();
        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        loadData();
    }

    public void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.onRefreshCompleted();
                if (mList.size() > 0) {
                    if (mList.size() <= column * 8) {  //<=24 分页加载完毕
                        for (int i = 0; i < mList.size(); i++) {
                            mDataList.add(mList.get(0));
                            mList.remove(0);
                        }
                        recycler.enableLoadMore(false);
                    } else {//完整取一页
                        for (int i = 0; i < column * 8; i++) {
                            mDataList.add(mList.get(0));
                            mList.remove(0);
                        }
                        recycler.enableLoadMore(true);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (mDataList.size() == 0)
                        llNoData.setVisibility(View.VISIBLE);
                    else
                        recycler.enableLoadMore(false);
                }
            }
        }, 600);
    }

    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getApplicationContext(), column);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(column, dip10, false);
    }

    // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
    private void getPics() {
        mList = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            String filepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            File file = new File(filepath);
            PhotoBean photoBean = new PhotoBean();
            photoBean.path = file.getAbsolutePath();
            if (picList!=null&&picList.size() > 0)//恢复选中的状态
                photoBean.isCheched = picList.contains(photoBean.path);
            mList.add(photoBean);
            cursor.moveToPrevious();
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_pic, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_select);
        selectPicProvider = (SelectPicProvider) MenuItemCompat.getActionProvider(menuItem);
        selectPicProvider.onCreateActionView();
        if (picList!=null&&picList.size() > 0) {
            selectPicProvider.setCount(picList.size());
        }
        selectPicProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picList!=null&&picList.size() > 0) {
                    List<PhotoBean> photoBeens = new ArrayList<>();
                    for (String str : picList) {
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.path = str;
                        photoBeens.add(photoBean);
                    }
                    EventBus.getDefault().post(new Event.SelectPicEvent(photoBeens));
                    finish();
                }
            }
        });
        return true;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_selectpic_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    public class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_check)
        ImageView ivChecked;
        int itemWidth;

        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
            rlCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            PhotoBean photoBean = mDataList.get(position);
            ImageLoadUtil.loadImage("file:///" + photoBean.path, ivCover, itemWidth, itemWidth);
            ivChecked.setImageResource(photoBean.isCheched ? R.drawable.ic_checked : R.drawable.ic_check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(itemView, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if(!mDataList.get(position).isCheched){
                if(picList.size()==9){
                    showMsg("最多只能选择9张图片");
                    return;
                }
            }
            PhotoBean photoBean = mDataList.get(position);
            photoBean.isCheched = !photoBean.isCheched;
            if(photoBean.isCheched){
                picList.add(photoBean.path);
            }else if(picList.contains(photoBean.path)){
                picList.remove(photoBean.path);//移除
            }
            if(selectPicProvider!=null){
                selectPicProvider.setCount(picList.size());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
