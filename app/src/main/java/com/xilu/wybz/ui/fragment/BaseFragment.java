package com.xilu.wybz.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.AnimImageView;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected MaterialDialog materialDialog;
    protected ArrayList<Integer> resourceIdList;
    protected AnimImageView animImageView;
    protected ImageView ivLoading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        initPresenter();
        return view;
    }
    protected void showPd(String msg) {
        showIndeterminateProgressDialog(msg);
    }

    protected void cancelPd() {
        if (materialDialog != null && materialDialog.isShowing())
            materialDialog.dismiss();
    }

    protected void showIndeterminateProgressDialog(String msg) {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .content(msg)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .show();
    }
    protected abstract int getLayoutResId();

    protected abstract void initPresenter();

    protected void showLoading(View ll_loading) {
        if (resourceIdList == null) {
            resourceIdList = new ArrayList<>();
            resourceIdList.add(R.drawable.loading_1);
            resourceIdList.add(R.drawable.loading_2);
            resourceIdList.add(R.drawable.loading_3);
            resourceIdList.add(R.drawable.loading_4);
        }
        ivLoading = (ImageView) ll_loading.findViewById(R.id.iv_loading_anim);
        if (animImageView == null)
            animImageView = new AnimImageView();
        animImageView.setAnimation(ivLoading, resourceIdList);
        animImageView.start(true, 120);
        ll_loading.setVisibility(View.VISIBLE);
    }

    protected void disMissLoading(View ll_loading) {
        if (ll_loading != null) {
            ll_loading.setVisibility(View.GONE);
            animImageView.stop();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    protected void showMsg(String msg){
        ToastUtils.toast(getActivity(),msg);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}