package com.xilu.wybz.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ImageAdapter;
import com.xilu.wybz.adapter.MusicTalkAdapter;
import com.xilu.wybz.adapter.SongAlbumAdapter;
import com.xilu.wybz.adapter.WorksAdapter;
import com.xilu.wybz.bean.Banner;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MainPresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IHomeView;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.SpacesItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements IHomeView {
    @Bind(R.id.recycler_view_recommend)
    RecyclerView recyclerViewRecommend;
    @Bind(R.id.recycler_view_songalbum)
    RecyclerView recyclerViewSongalbum;
    @Bind(R.id.recycler_view_newwork)
    RecyclerView recyclerViewNewwork;
    @Bind(R.id.recycler_view_musictalk)
    RecyclerView recyclerViewMusictalk;
    @Bind(R.id.viewpager_ad)
    ViewPager mViewPager;
    @Bind(R.id.linear_point)
    ViewGroup group;
    @Bind(R.id.tv_recommendwork)
    TextView tvRecommendwork;
    @Bind(R.id.tv_songablum)
    TextView tvSongablum;
    @Bind(R.id.tv_newwork)
    TextView tvNewwork;
    @Bind(R.id.tv_musictalk)
    TextView tvMusictalk;
    @Bind(R.id.tv_musictalk_more)
    TextView tvMusictalkMore;
    @Bind(R.id.tv_songablum_more)
    TextView tvSongablumMore;
    private MainPresenter presenter;
    private WorksAdapter worksAdapter;
    private WorksAdapter newworksAdapter;
    private SongAlbumAdapter songAlbumAdapter;
    private MusicTalkAdapter musicTalkAdapter;
    public List<Banner> bannerList;    //banner
    public List<WorksData> recommendWorkList;//推荐作品
    public List<SongAlbum> songAlbumList;//歌单
    public List<WorksData> newWorkList;//最新作品
    public List<MusicTalk> musicTalkList;//乐说
    ImageAdapter imgAdapter;
    ImageView[] tips;//圆点指示器
    private int currentItem = 0;
    //请求更新显示的View
    protected static final int MSG_UPDATE_IMAGE = 1;
    //请求暂停轮播
    protected static final int MSG_KEEP_SILENT = 2;
    //请求恢复轮播
    protected static final int MSG_BREAK_SILENT = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED = 4;
    //轮播间隔时间
    protected static final long MSG_DELAY = 3000;
    private int column = 3;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        presenter = new MainPresenter(this, this);
        presenter.init();
    }

    @Override
    public void initView() {
        tvRecommendwork.setVisibility(View.GONE);
        tvSongablum.setVisibility(View.GONE);
        tvNewwork.setVisibility(View.GONE);
        tvMusictalk.setVisibility(View.GONE);
        tvMusictalkMore.setVisibility(View.GONE);
        tvSongablumMore.setVisibility(View.GONE);

        int space10 = DensityUtil.dip2px(context, 10);

        recyclerViewRecommend.setNestedScrollingEnabled(false);
        recyclerViewRecommend.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewRecommend.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));

        recyclerViewNewwork.setNestedScrollingEnabled(false);
        recyclerViewNewwork.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewNewwork.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));

        recyclerViewSongalbum.setNestedScrollingEnabled(false);
        recyclerViewSongalbum.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewSongalbum.addItemDecoration(new GridSpacingItemDecoration(2, space10, false));

        recyclerViewMusictalk.setNestedScrollingEnabled(false);
        recyclerViewMusictalk.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewMusictalk.addItemDecoration(new SpacesItemDecoration(space10));

        recommendWorkList = new ArrayList<>();
        bannerList = new ArrayList<>();
        songAlbumList = new ArrayList<>();
        newWorkList = new ArrayList<>();
        musicTalkList = new ArrayList<>();
        //Banner
        mViewPager.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.getScreenW(context),
                DensityUtil.getScreenW(context) * 28 / 75));
        //推荐作品
        worksAdapter = new WorksAdapter(context, recommendWorkList, column , MyCommon.TUIJIAN);
        worksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (recommendWorkList.size() > 0) {
                    if (MyApplication.ids.size() > 0)
                        MyApplication.ids.clear();
                    for (WorksData worksData : recommendWorkList) {
                        if (worksData.status == 1) {
                            MyApplication.ids.add(worksData.getItemid());
                        }
                    }
                    WorksData worksData = recommendWorkList.get(position);
                    if (worksData.status == 2) {
                        LyricsdisplayActivity.toLyricsdisplayActivity(context, worksData.itemid, 0, worksData.getTitle());
                    } else {
                        PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", MyCommon.TUIJIAN, position);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewRecommend.setAdapter(worksAdapter);
        //推荐歌单
        songAlbumAdapter = new SongAlbumAdapter(context, songAlbumList);
        songAlbumAdapter.setOnItemClickLitener(new SongAlbumAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                SongAblumActivity.toSongAblumActivity(context, songAlbumList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewSongalbum.setAdapter(songAlbumAdapter);
        //最新作品
        newworksAdapter = new WorksAdapter(context, newWorkList, column, MyCommon.ZUIXIN);
        newworksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewNewwork.setAdapter(newworksAdapter);
        //乐说
        musicTalkAdapter = new MusicTalkAdapter(context, musicTalkList);
        musicTalkAdapter.setOnItemClickListener(new MusicTalkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PlayAudioActivity.toPlayAudioActivity(context, musicTalkList.get(position).itemid, "", "yueshuo", position);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerViewMusictalk.setAdapter(musicTalkAdapter);
        presenter.getHomeData();
    }

    @Override
    public void loadDataStart() {

    }

    @Override
    public void loadDataSuccess(String result) {
        Log.e("result", result);
        try {
            JSONObject infoJSONObject = new JSONObject(new JSONObject(result).getString("data"));
            //banner
            String banner = infoJSONObject.getString("bannerList");
            List<Banner> banners = new Gson().fromJson(banner,
                    new TypeToken<List<Banner>>() {
                    }.getType());
            bannerList.addAll(banners);
            if (bannerList.size() > 0)
                setViewPager();
            //推荐作品
            String worksData = infoJSONObject.getString("mTuijianList");
            Log.e("worksData", worksData);
            List<WorksData> worksDatas = new Gson().fromJson(worksData,
                    new TypeToken<List<WorksData>>() {
                    }.getType());
            if (recommendWorkList.size() > 0) {
                recommendWorkList.clear();
            }
            recommendWorkList.addAll(worksDatas);
            if (recommendWorkList.size() > 0) {
                tvRecommendwork.setVisibility(View.VISIBLE);
                worksAdapter.notifyDataSetChanged();
            }
            //推荐歌单
            String songAlbum = infoJSONObject.getString("recommendsonglist");
            List<SongAlbum> songAlbums = new Gson().fromJson(songAlbum,
                    new TypeToken<List<SongAlbum>>() {
                    }.getType());
            if (songAlbumList.size() > 0) {
                songAlbumList.clear();
            }
            songAlbumList.addAll(songAlbums);
            if (songAlbumList.size() > 0) {
                tvSongablum.setVisibility(View.VISIBLE);
                tvSongablumMore.setVisibility(View.VISIBLE);
                worksAdapter.notifyDataSetChanged();
            }
            //最新作品
            String newWorks = infoJSONObject.getString("newList");
            List<WorksData> newWorkList = new Gson().fromJson(newWorks,
                    new TypeToken<List<WorksData>>() {
                    }.getType());
            newWorkList.addAll(newWorkList);
            if (newWorkList.size() > 0) {
                tvNewwork.setVisibility(View.VISIBLE);
            }
            newworksAdapter.notifyDataSetChanged();
            //乐说
            String musicTalk = infoJSONObject.getString("yueshuoList");
            List<MusicTalk> musicTalks = new Gson().fromJson(musicTalk,
                    new TypeToken<List<MusicTalk>>() {
                    }.getType());
            if (musicTalks.size() == 4) {
                musicTalks.remove(3);
                musicTalks.remove(2);
            }
            musicTalkList.addAll(musicTalks);
            if (musicTalkList.size() > 0) {
                tvMusictalk.setVisibility(View.VISIBLE);
                tvMusictalkMore.setVisibility(View.VISIBLE);
                musicTalkAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    private void setViewPager() {
        //添加圆点指示器
        group.removeAllViews();
        tips = new ImageView[bannerList.size()];
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8, 20, 8, 20);// 间隔应该是20
            lp.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(lp);
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.pages_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.pages_unfocused);
            }
            group.addView(imageView);
        }
        imgAdapter = new ImageAdapter(context, bannerList);
        imgAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int pos = position % bannerList.size();
                if (bannerList.get(pos).getType() == 0) {
                    MyApplication.ids.clear();
                    for (Banner bannerListBean : bannerList) {
                        if (bannerListBean.getType() == 0 && !TextUtils.isEmpty(bannerListBean.getItemid()))
                            MyApplication.ids.add(bannerListBean.getItemid());
                    }
                    PlayAudioActivity.toPlayAudioActivity(context, bannerList.get(pos).getItemid(), "", "banner", pos);
                } else {
                    BrowserActivity.toBrowserActivity(context, bannerList.get(pos).getPlayurl());
                }
            }
        });
        mViewPager.setAdapter(imgAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHandler.sendMessage(Message.obtain(mHandler, MSG_PAGE_CHANGED, position, 0));
                // 取余后的索引，得到新的page的索引
                int newPosition = position % bannerList.size();
                // 根据索引设置图片的描述
                setImageBackground(newPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.setCurrentItem(0);//默认在中间，使用户看不到边界
        //开始轮播效果
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    public void setImageBackground(int selectItems) {
        try {
            for (int i = 0; i < tips.length; i++) {
                if (i == selectItems) {
                    tips[i].setBackgroundResource(R.drawable.pages_focused);
                } else {
                    tips[i].setBackgroundResource(R.drawable.pages_unfocused);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataFail(String msg) {

    }

    @Override
    public void loadDataFinish() {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                mHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    mViewPager.setCurrentItem(currentItem);
                    //准备下次播放
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick({R.id.tv_songablum_more, R.id.tv_musictalk_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_songablum_more:
                startActivity(SongablumMoreActivity.class);
                break;
            case R.id.tv_musictalk_more:
                break;
        }
    }
    public void onEventMainThread(Event.PPStatusEvent event) {
        switch (event.getStatus()) {
            case 1://开始
                String playFrom = PrefsUtil.getString("playFrom",context);
                String itemid = PrefsUtil.getString("playId",context);
                if(playFrom.equals(MyCommon.TUIJIAN)) {
                    for (int pos = 0; pos < recommendWorkList.size(); pos++) {
                        if (itemid.equals(recommendWorkList.get(pos).itemid)) {
                            for (int id = 0; id < recommendWorkList.size(); id++) {
                                recommendWorkList.get(pos).isPlay = (id==pos);
                            }
                            worksAdapter.updatePlayStatus(pos);
                            break;
                        }
                    }
                }else if (playFrom.equals(MyCommon.NEWS)){
                    for (int pos = 0; pos < newWorkList.size(); pos++) {
                        if (itemid.equals(newWorkList.get(pos).itemid)) {
                            for (int id = 0; id < newWorkList.size(); id++) {
                                newWorkList.get(pos).isPlay = (id==pos);
                            }
                            newworksAdapter.updatePlayStatus(pos);
                            break;
                        }
                    }
                }
                break;
            case 2://停止

                break;
            case 3://播放

                break;
            case 4://暂停
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
