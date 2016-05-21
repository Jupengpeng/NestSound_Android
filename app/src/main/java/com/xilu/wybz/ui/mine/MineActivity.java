package com.xilu.wybz.ui.mine;

import android.os.Bundle;

import com.xilu.wybz.R;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.base.BaseActivity;

/**
 * Created by June on 16/4/29.
 */
public class MineActivity extends BaseActivity implements IUserView{
    private UserPresenter mUserPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_mine;
    }
    @Override
    public void initView() {
        mUserPresenter = new UserPresenter(context,this);
        mUserPresenter.init();
    }
    @Override
    public void setUserInfo() {

    }

    @Override
    public void loadUserInfoSuccess(String result) {

    }

    @Override
    public void loadFocusFansCountSuccess(String result) {

    }

    @Override
    public void loadUserInfoFail(String msg) {

    }

    @Override
    public void loadFocusFansCountFail(String msg) {

    }

    @Override
    public void delSuccess(String result) {

    }

    @Override
    public void delFail(String msg) {

    }
}
