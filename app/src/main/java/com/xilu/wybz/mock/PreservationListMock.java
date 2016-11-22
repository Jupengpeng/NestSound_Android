package com.xilu.wybz.mock;

import com.xilu.wybz.bean.PreservationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */

public class PreservationListMock  implements MockCallback<PreservationInfo>{

    @Override
    public List<PreservationInfo> getMockList() {

        List<PreservationInfo> list = new ArrayList<>();

        long time = System.currentTimeMillis();
        list.add(new PreservationInfo("name1",time,1,1));
        list.add(new PreservationInfo("name2",time,2,1));
        list.add(new PreservationInfo("name3",time,3,3));
        list.add(new PreservationInfo("name4",time,2,2));
        list.add(new PreservationInfo("name5",time,1,1));

        return list;
    }

    @Override
    public PreservationInfo getMock() {
        return null;
    }
}
