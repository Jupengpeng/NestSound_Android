package com.xilu.wybz.ui.lyrics;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.RhymeBean;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/9.
 */
public class LyricsRhymeActivity extends BaseListActivity<RhymeBean> {

    List<RhymeBean> list;

    String nodata = "暂无歌词";
    int nodatares = R.drawable.ic_nozan;



    @Override
    protected void initPresenter() {
        initView();
    }

    public void initView() {
        setTitle("韵脚");
        hideRight();
        recycler.enablePullToRefresh(false);
        recycler.enableLoadMore(false);

        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpData() {
        super.setUpData();

        try {
            InputStreamReader inputReader = new InputStreamReader(getAssets().open("rhyme-new.json"));
            list = new Gson().fromJson(inputReader,new TypeToken<List<RhymeBean>>(){}.getType());
        }catch (Exception e){
            list = new ArrayList<>();
        }

        mDataList = list;
    }

    @Override
    public boolean hasPadding() {
        return super.hasPadding();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.onRefreshCompleted();
            }
        }, 800);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lrc_rhyme, parent, false);
        return new SampleViewHolder(view);
    }


    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.rhyme_name)
        TextView rhymeName;
        @Bind(R.id.rhyme_content)
        TextView rhymeContent;
        @Bind(R.id.rhyme_more)
        ImageView rhymeMore;

        public SampleViewHolder(View itemView) {
            super(itemView);
            rhymeContent.setTag(false);
            rhymeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((boolean)rhymeContent.getTag()){
                        rhymeContent.setMaxLines(2);
                        rhymeContent.setTag(false);
                    } else {
                        rhymeContent.setMaxLines(10);
                        rhymeContent.setTag(true);
                    }
                }
            });

        }

        @Override
        public void onBindViewHolder(int position) {
            RhymeBean bean = mDataList.get(position);
            rhymeName.setText(bean.yunjiao);
            rhymeContent.setText(bean.text);
        }

        @Override
        public void onItemClick(View view, int position) {
        }
    }
}
