package com.xilu.wybz.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ActAdapter;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.presenter.ActPresenter;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.ui.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 16/5/7.
 */
public class ActFragment extends BaseFragment implements IActView {
    @Bind(R.id.recycler_view_act)
    RecyclerView recyclerViewAct;
    private ActAdapter actAdapter;
    private List<ActBean> actBeans;
    private ActPresenter findPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_act;
    }

    @Override
    protected void initPresenter() {
        findPresenter = new ActPresenter(context, this);
        findPresenter.init();
    }

    @Override
    public void showActList(List<ActBean> actBeanList) {
        actBeans.addAll(actBeanList);
        actAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {

    }
    @Override
    public void initView() {
        recyclerViewAct.setLayoutManager(new LinearLayoutManager(context));
        actBeans = new ArrayList<>();
        actAdapter = new ActAdapter(context, actBeans);
        recyclerViewAct.setAdapter(actAdapter);
        findPresenter.getActList();
        actAdapter.setOnItemClickListener(new ActAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BrowserActivity.toBrowserActivity(context, actBeans.get(position).getUrl());
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
}
