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
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SongListAdapter;
import com.xilu.wybz.bean.GleeDetailBean;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.BitmapCallback;
import com.xilu.wybz.presenter.SongAlbumPresenter;
import com.xilu.wybz.ui.IView.IRecSongView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.main.SongablumMoreActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.YcScrollView;

import org.w3c.dom.Text;

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
    @Bind(R.id.status_bar_view)
    View statusBarView;
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
        tvTitle.setText(songAlbum.getName());
        tvDesc.setText(songAlbum.getDetail());
        recSongPresenter.getMusicList(songAlbum.getId(),1);
    }

    public void initMusicView() {
        int statusbarHeight = 0;
        if(isChenjin) {
            statusbarHeight = DensityUtil.getStatusBarHeight(context);
        }
        statusBarView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusbarHeight));
        int coverPicWith = DensityUtil.dip2px(context, 120*DensityUtil.getScreenDensity(context)/2);
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
                toPlayPos(position);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewSong.setAdapter(songListAdapter);
    }
    private void loadPic(){
        String url = songAlbum.getPic();
        File file = new File(DownLoaderDir.cacheDir);
        if(!file.exists())file.mkdirs();

        String path = DownLoaderDir.cacheDir+"music_blur"+songAlbum.id+".png";
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
                    Bitmap bmp = NativeStackBlur.process(BitmapUtils.zoomBitmap(response, 100), 60);
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
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), songAlbum.id, MyCommon.GEDAN, position);
        }
    }
    @Override
    public void showSongDetail(GleeDetailBean gleeDetailBean) {
        musicBeans.addAll(gleeDetailBean.workList);
        tvCount.setText("(共"+gleeDetailBean.workCount+"首）");
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
