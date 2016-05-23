package com.xilu.wybz.ui.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;

import butterknife.ButterKnife;
public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        context = getActivity();
        userId = PrefsUtil.getUserId(context);
        ButterKnife.bind(this, view);
        initPresenter();
        return view;
    }

    protected abstract int getLayoutResId();

    protected abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    protected void showMsg(String msg){
        ToastUtils.toast(context,msg);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}