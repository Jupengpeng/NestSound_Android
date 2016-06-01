package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HotListAdapter;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyThreadPool;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.HttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/31.
 */
public class NewMakeHotActivity extends ToolbarActivity implements IHotView, View.OnClickListener {
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.up_float_tab_root)
    LinearLayout titleFloatTab;
    @Bind(R.id.tv_new)
    CheckedTextView tv_float_new;
    @Bind(R.id.tv_hot)
    CheckedTextView tv_float_hot;

    private View titleView;
    private View titleTab;
    private CheckedTextView tv_new;
    private CheckedTextView tv_hot;
    private SimpleDraweeView ivQc;
    private LayoutInflater infater;
    private List<TemplateBean> item;
    private List<TemplateBean> item1;
    private List<TemplateBean> item2;
    private HotListAdapter adapter;
    private int currentType = TYPE_TAB_1;
    public static final int TYPE_TAB_1 = 1;
    public static final int TYPE_TAB_2 = 2;
    private int tab2Pos = 0;
    private int tab2OffsetY = 0;
    private int tab1Pos = 0;
    private int tab1OffsetY = 0;
    private HotPresenter hotPresenter;
    private int page1 = 1;
    private int page2 = 1;
    private boolean hasNextPage1 = true;
    private boolean hasNextPage2 = true;
    private TemplateBean tb;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_newhot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotPresenter = new HotPresenter(context, this);
        hotPresenter.init();
        initEvent();
        initData();
    }

    public void initView() {
        setTitle("伴奏");
        infater = LayoutInflater.from(this);
        titleView = infater.inflate(R.layout.hot_headview, null);
        ivQc = (SimpleDraweeView) titleView.findViewById(R.id.iv_qc);
        loadImage("res:///" + R.drawable.ic_qc_bg, ivQc);
        titleTab = infater.inflate(R.layout.up_float_tab_layout, null);
        tv_new = (CheckedTextView) titleTab.findViewById(R.id.tv_new);
        tv_hot = (CheckedTextView) titleTab.findViewById(R.id.tv_hot);
        listViewAddHeader();
        tv_float_hot.setOnClickListener(this);
        tv_float_new.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_hot.setOnClickListener(this);
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PlayBanZouInstance.getInstance().status == 3) {
                    PlayBanZouInstance.getInstance().pauseMediaPlay();
                    adapter.updateData();
                }
                TemplateBean bean = item.get(position);
                MakeSongActivity.ToMakeSongActivity(context, bean);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < 1) {// 悬浮tab出现时机，listview含有两个header
                    titleFloatTab.setVisibility(View.GONE);
                } else {
                    titleFloatTab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void listViewAddHeader() {
        listView.addHeaderView(titleView);
        listView.addHeaderView(titleTab);
    }

    private void loadData() {
        if (currentType == TYPE_TAB_1) {
            if (hasNextPage1)
                hotPresenter.loadHotData("", TYPE_TAB_1, page1++);
        } else {
            if (hasNextPage2)
                hotPresenter.loadHotData("", TYPE_TAB_2, page2++);
        }
    }

    private void initData() {
        item = new ArrayList<>();
        item1 = new ArrayList<>();
        item2 = new ArrayList<>();
        item.addAll(item1);
        adapter = new HotListAdapter(context, item);
        adapter.setITemplateMusicListener(new ITemplateMusicListener() {
            @Override
            public void onPlayMusic(TemplateBean templateBean) {
                tb = templateBean;
                playTemplateMusic();
            }

            @Override
            public void onStopMusic() {
                PlayBanZouInstance.getInstance().stopMediaPlay();
            }

            @Override
            public void onPauseMusic() {
                PlayBanZouInstance.getInstance().pauseMediaPlay();
            }

            @Override
            public void onResumeMusic() {
                PlayBanZouInstance.getInstance().resumeMediaPlay();
            }
        });
        listView.setAdapter(adapter);
        loadData();
    }

    public void playTemplateMusic() {
        if (PlayMediaInstance.getInstance().status == 3) {
            PlayMediaInstance.getInstance().pauseMediaPlay();
            EventBus.getDefault().post(new Event.PPStatusEvent(4));
        }
        PlayBanZouInstance.getInstance().stopMediaPlay();
        String playPath = FileUtils.getMusicCachePath(MyCommon.TYPE_TEMPLATE + tb.id);
        if (new File(playPath).exists()) {
            PlayBanZouInstance.getInstance().setData(MyCommon.TYPE_TEMPLATE, tb.id);
        } else {
            String filePath = FileUtils.getRootPath() + FileUtils.MUSICCACHEPATH;
            if (!new File(filePath).exists()) {
                new File(filePath).mkdirs();
            }
            hotPresenter.downHot(filePath, MyCommon.TYPE_TEMPLATE + tb.id, tb.mp3);
        }
        PlayBanZouInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                if(adapter!=null){
                    adapter.updatePlayStatus();
                }
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onPlay() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onOver() {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new:
                switchTabtList(true);
                break;
            case R.id.tv_hot:
                if (item2.size() == 0) {
                    loadData();
                }
                switchTabtList(false);
                break;
            default:
                break;
        }
    }

    private void switchTabtList(boolean isTab1) {
        if (isTab1) {
            if (currentType == TYPE_TAB_1) {
                return;// 说明点击的是相同的活动列表，不用改变
            } else {// tab2 switch tab1
                tab2Pos = listView.getFirstVisiblePosition();
                tab2OffsetY = getOffsetY();
                if (titleFloatTab.getVisibility() == View.GONE) {
                    tab1Pos = tab2Pos;
                    tab1OffsetY = tab2OffsetY;
                } else {
                    if (tab1Pos == 0) {
                        tab1Pos = 1;
                        tab1OffsetY = 0;
                    }
                }
                currentType = TYPE_TAB_1;
                item2.clear();
                item2.addAll(item);
                item.clear();
                item.addAll(item1);
            }
        } else {
            if (currentType == TYPE_TAB_2) {
                return;
            } else {// tab1 switch tab2
                tab1Pos = listView.getFirstVisiblePosition();
                tab1OffsetY = getOffsetY();
                if (titleFloatTab.getVisibility() == View.GONE) {
                    tab2Pos = tab1Pos;
                    tab2OffsetY = tab1OffsetY;
                } else {
                    if (tab2Pos == 0) {
                        tab2Pos = 1;
                        tab2OffsetY = 0;
                    }
                }
                currentType = TYPE_TAB_2;
                item1.clear();
                item1.addAll(item);
                item.clear();
                item.addAll(item2);
            }
        }
        updateTabSelectState();
        relocationLastPos();
    }

    private int getOffsetY() {
        View view = listView.getChildAt(0);
        return view != null ? view.getTop() : 0;
    }

    /**
     * 更新tab栏选中状态
     */
    private void updateTabSelectState() {
        boolean isTab1 = (currentType == TYPE_TAB_1);
        tv_new.setChecked(isTab1);
        tv_float_new.setChecked(isTab1);
        tv_hot.setChecked(!isTab1);
        tv_float_hot.setChecked(!isTab1);
    }

    /**
     * 重新定位到上次的位置
     */
    private void relocationLastPos() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (currentType == TYPE_TAB_1) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.setSelectionFromTop(tab1Pos, tab1OffsetY);
                }
            });
        } else {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.setSelectionFromTop(tab2Pos, tab2OffsetY);
                }
            });
        }
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens) {
        if (currentType == TYPE_TAB_1) {
            item1.addAll(templateBeens);
        } else {
            item2.addAll(templateBeens);
        }
        item.addAll(templateBeens);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {
        if (currentType == TYPE_TAB_1) {
            hasNextPage1 = false;
        } else {
            hasNextPage1 = true;
        }
    }

    @Override
    public void downloadSuccess() {
        PlayBanZouInstance.getInstance().setData(MyCommon.TYPE_TEMPLATE, tb.id);
    }

    @Override
    public void loadNoData() {
        if (currentType == TYPE_TAB_1) {
            hasNextPage1 = false;
        } else {
            hasNextPage1 = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                if(PlayBanZouInstance.getInstance().status>1){
                    PlayBanZouInstance.getInstance().stopMediaPlay();
                    adapter.updateData();
                }
                startActivity(SearchHotActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
