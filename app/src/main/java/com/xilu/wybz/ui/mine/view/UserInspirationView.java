package com.xilu.wybz.ui.mine.view;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.mine.Adapter.UserInspirationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserInspirationView extends UserBaseView{


    public UserInspirationView(Context context) {
        super(context);
        initView();

        List<WorksData> data = new ArrayList<WorksData>();

        setTestData(data);

        setAdapter(new UserInspirationAdapter(context,data));
    }


    private void setTestData(List<WorksData> data){
        WorksData worksData;

        worksData = new WorksData();
        worksData.createdate = "2014-02-09";
        worksData.pics = "2014";
        worksData.audio = "2014";
        worksData.spirecontent = "zhe\n is test..";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-02-01";
        worksData.pics = "123";
        worksData.audio = "123";
        worksData.spirecontent = "";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-02-01";
        worksData.pics = "123";
        worksData.audio = "";
        worksData.spirecontent = "123";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-12-09";
        worksData.pics = "2014-02-09";
        worksData.audio = "2014-02-09";
        worksData.spirecontent = "2014-02-09";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-02-09";
        worksData.pics = "";
        worksData.audio = "";
        worksData.spirecontent = "this is test..";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-02-09";
        worksData.pics = "";
        worksData.audio = "123";
        worksData.spirecontent = "123";
        data.add(worksData);

        worksData = new WorksData();
        worksData.createdate = "2014-02-09";
        worksData.pics = "";
        worksData.audio = "123";
        worksData.spirecontent = "";
        data.add(worksData);
    }
}
