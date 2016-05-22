package com.xilu.wybz.ui.mine.view;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserInspirationView extends UserBaseView{


    public UserInspirationView(Context context) {
        super(context);

        List<WorksData> data = new ArrayList<WorksData>();
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());
        data.add(new WorksData());

        setAdapter(new UserInspirationAdapter(context,data));
    }
}
