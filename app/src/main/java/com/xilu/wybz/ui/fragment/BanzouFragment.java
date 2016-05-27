package com.xilu.wybz.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HotListAdapter;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyThreadPool;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.song.MakeHotActivity;
import com.xilu.wybz.ui.song.MakeSongActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.HttpUtils;
import com.xilu.wybz.utils.ParseUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import de.greenrobot.event.EventBus;
/**
 * Created by hujunwei on 16/4/14.
 */
public class BanzouFragment extends BaseFragment implements IHotView {
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    @Bind(R.id.iv_nodata)
    ImageView iv_nodata;
    HotListAdapter adapter;
    List<TemplateBean> templateBeans;
    View footerView;
    HotPresenter hotPresenter;
    int type = 2;
    int page = 0;
    boolean hasNextPage;
    boolean canPlay;
    String keyWord = "";
    public BanzouFragment() {

    }
    public void setType(int which) {
        type = which == 0 ? 2 : (which == 1 ? 1 : 0);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initPresenter() {
        hotPresenter = new HotPresenter(context,this);
        hotPresenter.init();
        initEvent();
        initData();
    }

    @Override
    public void initView() {
        hotPresenter = new HotPresenter(context, this);
        if (type > 0) {
            tv_nodata.setText(type == 2 ? "暂无最新伴奏" : "暂无最热伴奏");
        } else {
            tv_nodata.setText("没有搜索结果");
            iv_nodata.setImageResource(R.drawable.ic_nosearch);
        }
        footerView = LayoutInflater.from(context).inflate(R.layout.widget_pull_to_refresh_footer, null);
    }

    public void initEvent() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PlayBanZouInstance.getInstance().status == 3) {
                    PlayBanZouInstance.getInstance().pauseMediaPlay();
                    adapter.updateData();
                }
                TemplateBean bean = templateBeans.get(position);
                MakeSongActivity.ToMakeSongActivity(context,bean);
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (hasNextPage) {
                        hasNextPage = false;
                        loadHotData(keyWord);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void loadMore() {
        if (hasNextPage) {
            hasNextPage = false;
            loadHotData(keyWord);
        }
    }

    public void initData() {
        templateBeans = new ArrayList<>();
        adapter = new HotListAdapter(context, templateBeans);
        listview.setAdapter(adapter);
        adapter.setITemplateMusicListener(new ITemplateMusicListener() {
            @Override
            public void onPlayMusic(TemplateBean tb) {
                canPlay = true;
                playTemplateMusic(tb);
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
        if (type > 0) {
            loadHotData(keyWord);
        }
    }


    public void playTemplateMusic(final TemplateBean tb) {
        if (PlayMediaInstance.getInstance().status == 3) {
            PlayMediaInstance.getInstance().pauseMediaPlay();
            EventBus.getDefault().post(new Event.PPStatusEvent(4));
        }
        PlayBanZouInstance.getInstance().stopMediaPlay();
        MyThreadPool.getInstance().doTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String playurl = FileUtils.getMusicCachePath(MyCommon.TYPE_TEMPLATE + tb.id);
                    if (FileUtils.saveFile(playurl, HttpUtils.getInputStreamFormUrl(tb.mp3))) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, tb.id));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (msg.obj != null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (canPlay) {
                            PlayBanZouInstance.getInstance().setData(MyCommon.TYPE_TEMPLATE, msg.obj.toString());
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void loadHotData(String keyword) {
        if (!keyword.equals(keyWord)) {
            templateBeans.clear();
            keyWord = keyword;
            page = 0;
        }
        hotPresenter.loadHotData(keyWord, type, ++page);
    }

    @Override
    public void loadSuccess(String result) {
        List<TemplateBean> mList = ParseUtils.parseTemplateList(context,result);
        if (mList.size() > 0) {
            hasNextPage = true;
            ll_nodata.setVisibility(View.GONE);
            if (templateBeans.size() == 0) {
                if (mList.size() < 10) {
                    hasNextPage = false;
                } else {
                    listview.addFooterView(footerView);
                }
            }
            templateBeans.addAll(mList);
            adapter.notifyDataSetChanged();
        } else {
            hasNextPage = false;
            if (templateBeans.size() > 0) {
                listview.removeFooterView(footerView);
            } else {
                ll_nodata.setVisibility(View.VISIBLE);
            }
        }
    }

    public int getCount() {
        return adapter.getPlayCount();
    }

    @Override
    public void onStart() {
        super.onStart();
        canPlay = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        canPlay = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (PlayBanZouInstance.getInstance().status == 3)
            PlayBanZouInstance.getInstance().pauseMediaPlay();
    }

    public void updateAdapter() {
        if (adapter != null && getCount() > 0)
            adapter.updateData();
    }

    @Override
    public void loadFail(String msg) {
    }
}
