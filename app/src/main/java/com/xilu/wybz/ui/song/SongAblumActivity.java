package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.commit451.nativestackblur.NativeStackBlur;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SongListAdapter;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.presenter.SongAlbumPresenter;
import com.xilu.wybz.ui.IView.IRecSongView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.YcScrollView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * Created by June on 2016/3/11.
 * 歌单列表
 */
public class SongAblumActivity extends ToolbarActivity implements IRecSongView {
    @Bind(R.id.recycler_view_songalbum)
    RecyclerView recyclerViewSong;
    @Bind(R.id.status_bar_view)
    View statusBarView;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.iv_top_bg)
    ImageView ivTopBg;
    @Bind(R.id.iv_toolbar_bg)
    ImageView ivToolbarBg;
    @Bind(R.id.myScrollView)
    YcScrollView myScrollView;
    SongListAdapter songListAdapter;
    List<WorksData> musicBeans;
    SongAlbumPresenter recSongPresenter;
    private SongAlbum songAlbum;
    private int baseScrollHeight;
    public static void toSongAblumActivity(Context from, SongAlbum songAlbum) {
        Intent intent = new Intent(from, SongAblumActivity.class);
        intent.putExtra(YinChaoConfig.RECOMMENTSONG, songAlbum);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recSongPresenter = new SongAlbumPresenter(this, this);
        recSongPresenter.init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_songalbum;
    }

    @Override
    public void initView() {
        getIntentData();
        initMusicView();
        myScrollView.setOnScrollListener(new YcScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if(scrollY>baseScrollHeight){
                    scrollY = baseScrollHeight;
                }
                ivToolbarBg.setAlpha((float)(scrollY/(baseScrollHeight*1.0)));
            }
        });
    }

    public void getIntentData() {
        songAlbum = (SongAlbum) getIntent().getSerializableExtra(YinChaoConfig.RECOMMENTSONG);
//        setTitle(songAlbum.getName());
        recSongPresenter.getMusicList(songAlbum.getItemid());
    }

    public void initMusicView() {
        int statusbarHeight = 0;
        if (isChenjin) { //没有沉浸的时候 导航栏不需要上边距
            statusbarHeight =  DensityUtil.getStatusBarHeight(context);
        }
        statusBarView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusbarHeight));
        int coverPicWith = DensityUtil.dip2px(context, 120)*DensityUtil.getScreenW(context)/1080;
        ivCover.setLayoutParams(new LinearLayout.LayoutParams(coverPicWith,coverPicWith));
        baseScrollHeight = DensityUtil.dip2px(context, 25) + coverPicWith;
        rlTop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusbarHeight + DensityUtil.dip2px(context, 48 + 25) + coverPicWith));
        mToolbar.setTitleTextColor(0xFFFFFFFF);
        setTitle("歌单");
        musicBeans = new ArrayList<>();
        loadPic();
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSong.setNestedScrollingEnabled(false);
        songListAdapter = new SongListAdapter(context, musicBeans);
        songListAdapter.setOnItemClickListener(new SongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (musicBeans.size() > 0) {
                    if (MyApplication.ids.size() > 0)
                        MyApplication.ids.clear();
                    for (WorksData worksData : musicBeans) {
                        if (worksData.status == 1) {
                            MyApplication.ids.add(worksData.getItemid());
                        }
                    }
                    WorksData worksData = musicBeans.get(position);
                    if (worksData.status == 2) {
                        LyricsdisplayActivity.toLyricsdisplayActivity(context, worksData.itemid, 0, worksData.title);
                    } else {
                        PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "","tuijian", position);
                    }
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewSong.setAdapter(songListAdapter);
    }
    private void loadPic(){
        String url = "http://p8.qhimg.com/t011f3905c56dc4251f.jpg";
        ImageLoader.getInstance().displayImage(url, ivCover, ZnImageLoader.getInstance().playOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(bitmap, 100), 60);
                ivToolbarBg.setImageBitmap(bmp);
                ivTopBg.setImageBitmap(bmp);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }
    @Override
    public void showMusicList(List<WorksData> musicBeanList) {
        Log.e("musicBeanList", musicBeanList.size() + "");
        musicBeans.addAll(musicBeanList);
        songListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showErrorView() {

    }
}
