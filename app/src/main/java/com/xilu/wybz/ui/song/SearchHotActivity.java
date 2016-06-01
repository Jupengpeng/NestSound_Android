package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/19.
 */
public class SearchHotActivity extends BaseListActivity<TemplateBean> implements IHotView {
    @Bind(R.id.et_keyword)
    EditText etKeyword;
    @Bind(R.id.iv_cancle)
    ImageView ivCancle;
    HotPresenter hotPresenter;
    String keyword;
    TemplateBean tb;
    SampleViewHolder sampleViewHolder;
    int itemWidth;
    int itemHeight;
    int oldPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initPresenter() {
        hotPresenter = new HotPresenter(context, this);
        hotPresenter.init();
    }
    @Override
    protected void setUpData() {
        super.setUpData();
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search_hot;
    }


    public void initView() {
        initEvent();
    }

    private void initEvent() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString().trim();
                if (keyword.equals("")) {
                    ivCancle.setVisibility(View.GONE);
                } else {
                    ivCancle.setVisibility(View.VISIBLE);
                }
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (keyword.equals("")) {
                        showMsg("关键字不能为空");
                        return false;
                    } else {
                        recycler.setRefreshing();
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.iv_cancle, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancle:
                KeyBoardUtil.openKeybord(etKeyword, context);
                etKeyword.setText("");
                mDataList.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.rl_right:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        hotPresenter.loadHotData(keyword, 1, page++);
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(templateBeens);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }


    @Override
    public void downloadSuccess() {
        PlayBanZouInstance.getInstance().setData(MyCommon.TYPE_TEMPLATE, tb.id);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bz_list_item, parent, false);
        sampleViewHolder = new SampleViewHolder(view);
        return sampleViewHolder;
    }
    public void playTemplateMusic() {
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
                if(sampleViewHolder!=null){
                    sampleViewHolder.updatePlayStatus();
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
    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        @Bind(R.id.rl_play)
        RelativeLayout rlPlay;
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.progress)
        ProgressBar progress;
        private void toPlayMusic(TemplateBean templateBean,int pos){
            if (templateBean.playStatus>1) {
                if (iml != null) {
                    if (templateBean.playStatus == 3) {
                        iml.onPauseMusic();
                        templateBean.playStatus=2;
                    } else {
                        iml.onResumeMusic();
                        templateBean.playStatus=3;
                    }
                }
            } else {
                if (iml != null) {
                    if(oldPos!=pos){//如果播放新的歌曲 把上一次播放的状态更新下
                        mDataList.get(oldPos).playStatus=0;
                        adapter.notifyItemChanged(oldPos);
                    }
                    iml.onPlayMusic(templateBean);
                    templateBean.playStatus=1;
                    oldPos = pos;
                }
            }
            adapter.notifyItemChanged(pos);
        }
        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth =  (DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,40))/2;
            itemHeight = itemWidth*172/326;
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth,itemHeight));
        }
        ITemplateMusicListener iml = new ITemplateMusicListener() {
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
        };
        @Override
        public void onBindViewHolder(int position) {
            TemplateBean templateBean = mDataList.get(position);
            itemView.setTag(templateBean);
            tvTitle.setText(templateBean.title);
            tvAuthor.setText(templateBean.author);
            if(StringUtil.isNotBlank(templateBean.pic)) loadImage(templateBean.pic, ivCover);
            if (!TextUtils.isEmpty(MyApplication.musicId) && MyApplication.musicId.equals(templateBean.id)) {
                templateBean.playStatus = PlayBanZouInstance.getInstance().status;
            }
            ivPlay.setImageResource(templateBean.playStatus==3?R.drawable.ic_bz_pause:R.drawable.ic_bz_play);
            ivPlay.setVisibility(templateBean.playStatus==1?View.GONE:View.VISIBLE);
            progress.setVisibility(templateBean.playStatus!=1?View.GONE:View.VISIBLE);
            rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPlayMusic(templateBean,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if (PlayBanZouInstance.getInstance().status == 3) {
                PlayBanZouInstance.getInstance().pauseMediaPlay();
                adapter.notifyItemChanged(position);
            }
            TemplateBean bean = mDataList.get(position);
            MakeSongActivity.ToMakeSongActivity(context, bean);
        }
        public void updatePlayStatus(){
            mDataList.get(oldPos).playStatus=3;
            adapter.notifyItemChanged(oldPos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(PlayBanZouInstance.getInstance().status>1){
            PlayBanZouInstance.getInstance().stopMediaPlay();
        }
    }
}
