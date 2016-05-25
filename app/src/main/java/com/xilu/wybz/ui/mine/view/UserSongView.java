package com.xilu.wybz.ui.mine.view;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.mine.Adapter.UserSongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserSongView extends UserBaseView {

    public UserSongView(Context context) {
        super(context);

        initView();


        List<WorksData> data = new ArrayList<WorksData>();
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());

        setAdapter(new UserSongAdapter(context,data));
    }
}
