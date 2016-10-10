package com.xilu.wybz.mock;

import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.PreserveInfoBean;
import com.xilu.wybz.bean.ProductInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */

public class PreserveInfoMock implements MockCallback<PreserveInfoBean>{

    @Override
    public List<PreserveInfoBean> getMockList() {
        return null;
    }

    @Override
    public PreserveInfoBean getMock() {

        PreserveInfoBean bean = new PreserveInfoBean();

        long time = System.currentTimeMillis();


        bean.productInfo = new ProductInfo();
        bean.personInfo = new PersonInfo();
        bean.productInfo.id = 1;


        return bean;
    }
}
