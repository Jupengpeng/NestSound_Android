package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SongListAdapter;
import com.xilu.wybz.bean.GleeDetailBean;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.presenter.SongAlbumPresenter;
import com.xilu.wybz.ui.IView.IRecSongView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.YcScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by June on 2016/3/11.
 * 歌单列表
 */
public class SongAblumActivity extends ToolbarActivity implements IRecSongView {
    @Bind(R.id.recycler_view_songalbum)
    RecyclerView recyclerViewSong;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.iv_top_bg)
    ImageView ivTopBg;
    @Bind(R.id.iv_toolbar_bg)
    ImageView ivToolbarBg;
    @Bind(R.id.ll_detail)
    LinearLayout ll_detail;
    @Bind(R.id.toolbar_bg_cover)
    View topbarBgCover;
    @Bind(R.id.myScrollView)
    YcScrollView myScrollView;
    SongListAdapter songListAdapter;
    List<WorksData> musicBeans;
    SongAlbumPresenter recSongPresenter;
    private SongAlbum songAlbum;
    private int baseScrollHeight;
    int coverPicWith;
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
        myScrollView.setOnScrollListener(new YcScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if(scrollY>baseScrollHeight){
                    scrollY = baseScrollHeight;
                }
                ivToolbarBg.setAlpha((float)(scrollY/(baseScrollHeight*1.0)));
                topbarBgCover.setAlpha((float)(scrollY/(baseScrollHeight*1.0)));
            }
        });
    }

    public void getIntentData() {
        songAlbum = (SongAlbum) getIntent().getSerializableExtra(YinChaoConfig.RECOMMENTSONG);
        updateHeaderView();
        recSongPresenter.getMusicList(songAlbum.getId(),1);
        initMusicView();
    }

    public void initMusicView() {
        int statusbarHeight = 0;
        if(isChenjin) {
            statusbarHeight = DensityUtil.getStatusBarHeight(context);
        }
        coverPicWith = DensityUtil.getScreenW(context)/3;
        ivCover.setLayoutParams(new LinearLayout.LayoutParams(coverPicWith,coverPicWith));
        ll_detail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,coverPicWith));
        baseScrollHeight = DensityUtil.dip2px(context, 25) + coverPicWith;
        rlTop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusbarHeight + DensityUtil.dip2px(context, 48 + 25) + coverPicWith));
        mToolbar.setTitleTextColor(Color.WHITE);
        setTitle("歌单");
        musicBeans = new ArrayList<>();
        loadPic();
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSong.setNestedScrollingEnabled(false);
        songListAdapter = new SongListAdapter(context, musicBeans);
        songListAdapter.setOnItemClickListener(new SongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toPlayPos(position);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewSong.setAdapter(songListAdapter);
    }
    private void loadPic(){
        String url = MyCommon.getImageUrl(songAlbum.getPic(),coverPicWith,coverPicWith);
        loadImage(url,ivCover);
        File file = new File(FileDir.blurPic);
        if(!file.exists())file.mkdirs();
        String path = FileDir.blurPic+ MD5Util.getMD5String(url);
        if(new File(path).exists()){//加载本地
            ivToolbarBg.setImageBitmap(BitmapUtils.getSDCardImg(path));
            ivTopBg.setImageBitmap(BitmapUtils.getSDCardImg(path));
        }else{//下载并保存到本地
            HttpUtils httpUtils = new HttpUtils(context);
            httpUtils.getImage(url, new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }
                @Override
                public void onResponse(Bitmap response) {
                    if(isDestroy)return;
                    Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(response, 100), 80);
                    FileUtils.saveBmp(path,bmp);
                    ivToolbarBg.setImageBitmap(bmp);
                    ivTopBg.setImageBitmap(bmp);
                }
            });
        }
    }
    @OnClick({R.id.iv_play_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play_all:
                toPlayPos(0);
                break;
        }
    }
    public void toPlayPos(int position){
        if (musicBeans.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom",context);
            String playGedanId = PrefsUtil.getString("playGedanId",context);
            if(!playFrom.equals(MyCommon.GEDAN)||MyApplication.ids.size()==0||!playGedanId.equals(songAlbum.id)){
                if (MyApplication.ids.size() > 0)
                    MyApplication.ids.clear();
                for (WorksData worksData : musicBeans) {
                    MyApplication.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = musicBeans.get(position);
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), songAlbum.id, MyCommon.GEDAN);
        }
    }
    @Override
    public void showSongDetail(GleeDetailBean gleeDetailBean) {
        if (tvCount == null){
            return;
        }
        musicBeans.addAll(gleeDetailBean.workList);
        tvCount.setText("(共"+gleeDetailBean.workCount+"首)");
        if(gleeDetailBean.recommedSong!=null)
        songAlbum = gleeDetailBean.recommedSong;
        updateHeaderView();
        songListAdapter.notifyDataSetChanged();
    }
    public void updateHeaderView(){
        if(StringUtil.isNotBlank(songAlbum.name))
            tvTitle.setText(songAlbum.name);
        if(StringUtil.isNotBlank(songAlbum.detail))
            tvDesc.setText(songAlbum.detail);
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

    @Override
    protected void onDestroy() {
        if (recSongPresenter != null){
            recSongPresenter.cancelRequest();
        }
        super.onDestroy();
    }
}
