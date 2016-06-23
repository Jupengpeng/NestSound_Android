package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HotListAdapter;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/31.
 */
public class NewMakeHotActivity extends ToolbarActivity implements IHotView, View.OnClickListener {
    @Bind(R.id.listview_new)
    ListView listViewNew;
    @Bind(R.id.listview_hot)
    ListView listViewHot;
    @Bind(R.id.up_float_tab_root)
    LinearLayout titleFloatTab;
    @Bind(R.id.tv_new)
    CheckedTextView tv_float_new;
    @Bind(R.id.tv_hot)
    CheckedTextView tv_float_hot;

    private View titleView;
    private View titleTab;
    private View footViewNew;
    private View footViewHot;
    private CheckedTextView tv_new;
    private CheckedTextView tv_hot;
    private SimpleDraweeView ivQc;
    private LayoutInflater infater;
    private List<TemplateBean> item1;
    private List<TemplateBean> item2;
    private HotListAdapter newAdapter;
    private HotListAdapter hotAdapter;
    private int currentType = TYPE_TAB_1;
    public static final int TYPE_TAB_1 = 1;
    public static final int TYPE_TAB_2 = 2;
    private HotPresenter hotPresenter;
    private int page1 = 1;
    private int page2 = 1;
    private boolean hasNextPage1 = true;
    private boolean isOnClick1 = true;
    private boolean isOnClick2 = true;
    private boolean hasNextPage2 = true;
    private TemplateBean tb;
    private String playPath;
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
        footViewNew = infater.inflate(R.layout.widget_pull_to_refresh_footer, null);
        footViewHot = infater.inflate(R.layout.widget_pull_to_refresh_footer, null);
        tv_new = (CheckedTextView) titleTab.findViewById(R.id.tv_new);
        tv_hot = (CheckedTextView) titleTab.findViewById(R.id.tv_hot);
        listViewAddHeader();
        tv_float_hot.setOnClickListener(this);
        tv_float_new.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_hot.setOnClickListener(this);
    }

    private void initEvent() {
        ivQc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateBean templateBean = new TemplateBean();
                templateBean.id = "108";
                templateBean.title = "清唱";
                templateBean.mp3="http://7xsw6y.com2.z0.glb.qiniucdn.com/empty_hot_temp.mp3";
                templateBean.mp3times = 706;
                MakeSongActivity.ToMakeSongActivity(context,templateBean);
            }
        });
        listViewNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PlayBanZouInstance.getInstance().status == 3) {
                    PlayBanZouInstance.getInstance().pauseMediaPlay();
                    newAdapter.updateData();
                }
                TemplateBean bean = item1.get(position-2);
                MakeSongActivity.ToMakeSongActivity(context, bean);
            }
        });
        listViewHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PlayBanZouInstance.getInstance().status == 3) {
                    PlayBanZouInstance.getInstance().pauseMediaPlay();
                    hotAdapter.updateData();
                }
                TemplateBean bean = item2.get(position-2);
                MakeSongActivity.ToMakeSongActivity(context, bean);
            }
        });
        listViewNew.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){//停止的时候 联动另一个listview
                    if(titleFloatTab.getVisibility()==View.GONE){//联动位置
                        listViewHot.post(new Runnable() {
                            @Override
                            public void run() {
                                listViewHot.setSelectionFromTop(0, getOffsetY());
                            }
                        });
                    }else{
                        if(listViewHot.getFirstVisiblePosition()==0){
                            listViewHot.setSelectionFromTop(1, 0);
                        }
                    }
                    isOnClick2 = true;
                }else{
                    isOnClick2 = false;
                }
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
        listViewHot.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){
                    isOnClick1 = true;
                    if(titleFloatTab.getVisibility()==View.GONE){//联动位置
                        listViewNew.post(new Runnable() {
                            @Override
                            public void run() {
                                listViewNew.setSelectionFromTop(0, getOffsetY());
                            }
                        });
                    }else{
                        if(listViewNew.getFirstVisiblePosition()==0){
                            listViewNew.setSelectionFromTop(1, 0);
                        }
                    }
                }else{
                    isOnClick1 = false;
                }
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
        listViewNew.addHeaderView(titleView);
        listViewHot.addHeaderView(titleView);
        listViewNew.addHeaderView(titleTab);
        listViewHot.addHeaderView(titleTab);
        listViewNew.addFooterView(footViewNew,null,false);
        listViewHot.addFooterView(footViewHot,null,false);
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
        item1 = new ArrayList<>();
        item2 = new ArrayList<>();
        newAdapter = new HotListAdapter(context, item1);
        hotAdapter = new HotListAdapter(context, item2);
        newAdapter.setITemplateMusicListener(new ITemplateMusicListener() {
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
        hotAdapter.setITemplateMusicListener(new ITemplateMusicListener() {
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
        listViewNew.setAdapter(newAdapter);
        listViewHot.setAdapter(hotAdapter);
        hotPresenter.loadHotData("", TYPE_TAB_1, page1++);
        hotPresenter.loadHotData("", TYPE_TAB_2, page2++);
    }

    public void playTemplateMusic() {
        PlayBanZouInstance.getInstance().stopMediaPlay();
        String filePath = FileDir.hotDir;
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        String fileName = MD5Util.getMD5String(tb.mp3);
        playPath = filePath+fileName;
        if (new File(playPath).exists()) {
            PlayBanZouInstance.getInstance().setData(playPath, tb.id);
        } else {
            hotPresenter.downHot(filePath, fileName, tb.mp3);
        }
        PlayBanZouInstance.getInstance().setIMediaPlayerListener(new IMediaPlayerListener() {
            @Override
            public void onStart() {
                if (PlayMediaInstance.getInstance().status == 3) {
                    PlayMediaInstance.getInstance().pauseMediaPlay();
                    EventBus.getDefault().post(new Event.PPStatusEvent(4));
                }
                if(currentType==TYPE_TAB_1) {
                    if (newAdapter != null) {
                        newAdapter.updatePlayStatus();
                    }
                }else{
                    if (hotAdapter != null) {
                        hotAdapter.updatePlayStatus();
                    }
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
                if(isOnClick1)
                switchTabtList(true);
                break;
            case R.id.tv_hot:
                if(isOnClick2)
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
                currentType = TYPE_TAB_1;
            }
        } else {
            if (currentType == TYPE_TAB_2) {
                return;
            } else {// tab1 switch tab2
                currentType = TYPE_TAB_2;
            }
        }
        updateTabSelectState();
        relocationLastPos();
    }

    private int getOffsetY() {
        View view = null;
        if(currentType==TYPE_TAB_1){
            view = listViewNew.getChildAt(0);
        }else{
            view = listViewHot.getChildAt(0);
        }
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
        listViewNew.setVisibility(currentType == TYPE_TAB_1?View.VISIBLE:View.GONE);
        listViewHot.setVisibility(currentType == TYPE_TAB_2?View.VISIBLE:View.GONE);
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens, int currentType) {
        if (currentType == TYPE_TAB_1) {
            item1.addAll(templateBeens);
            newAdapter.notifyDataSetChanged();
        } else {
            item2.addAll(templateBeens);
            hotAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {
        if (isDestroy){
            return;
        }
        if (currentType == TYPE_TAB_1) {
            hasNextPage1 = false;
            listViewNew.removeFooterView(footViewNew);
        } else {
            hasNextPage2 = false;
            listViewHot.removeFooterView(footViewHot);
        }
    }

    @Override
    public void downloadSuccess() {
        PlayBanZouInstance.getInstance().setData(playPath, tb.id);
    }

    @Override
    public void loadNoData() {
        if (isDestroy){
            return;
        }
        if (currentType == TYPE_TAB_1) {
            hasNextPage1 = false;
            listViewNew.removeFooterView(footViewNew);
        } else {
            hasNextPage2 = false;
            listViewHot.removeFooterView(footViewHot);
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
                //关闭播放
                stopPlayBz();
                startActivity(SearchHotActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void stopPlayBz(){
        if(PlayBanZouInstance.getInstance().status>1){
            PlayBanZouInstance.getInstance().stopMediaPlay();
            if(currentType==TYPE_TAB_1) {
                if (newAdapter != null) {
                    newAdapter.updateData();
                }
            }else{
                if (hotAdapter != null) {
                    hotAdapter.updateData();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭播放
        stopPlayBz();
        if(hotPresenter!=null){
            hotPresenter.cancelUrl();
        }
    }
}
