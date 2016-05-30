package com.xilu.wybz.ui.mine.view;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.UserListPresenter;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.ui.mine.Adapter.UserSongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserLyricView extends UserBaseView {

    public UserLyricView(Context context,int userType) {
        super(context);
        initView();
        setUserListPresenter(new UserListPresenter(context,(IUserView)context,this, userType, UserPresenter.TYPE_SONG));
        List<WorksData> data = new ArrayList<WorksData>();
        showNoNetView();
        setAdapter(new UserSongAdapter(context,data));
    }
}
