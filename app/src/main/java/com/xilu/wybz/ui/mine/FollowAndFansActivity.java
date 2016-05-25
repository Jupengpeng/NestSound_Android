package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.FollowPresenter;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FollowAndFansActivity extends BaseListActivity<String> implements IFollowAndFansView {


    FollowPresenter mFollowPresenter;

    private String type;


    public static void toFollowAndFansActivity(Context context, String type){

        Intent intent = new Intent();
        intent.setClass(context, FollowAndFansActivity.class);
        intent.putExtra(KeySet.KEY_TYPE,type);

        context.startActivity(intent);
    }


    @Override
    protected void initPresenter() {

        mFollowPresenter = new FollowPresenter(context,this);
        mFollowPresenter.init();
    }

    @Override
    public void initView() {
        getIntentData();
        if (KeySet.TYPE_FANS_ACT.equals(type)){
            setTitle("粉丝");
        } else if (KeySet.TYPE_FOLLOW_ACT.equals(type)) {
            setTitle("关注");
        }

    }

    public void getIntentData(){
        if (getIntent() != null){
            type = getIntent().getStringExtra(KeySet.KEY_TYPE);
        }
    }



    @Override
    protected void setUpData() {
        super.setUpData();

        mDataList = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        mDataList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_fans, parent, false);
        return new FollowAndFansViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {


//        recycler.enableLoadMore(true);
//        mDataList.addAll(worksDataList);
//        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();


    }

    public static class FollowAndFansViewHolder extends BaseViewHolder{

        public FollowAndFansViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
