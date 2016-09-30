package com.xilu.wybz.mock;

import com.xilu.wybz.bean.ProductInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */

public class ProductAllListMock implements MockCallback<ProductInfo>{

    @Override
    public List<ProductInfo> getMockList() {

        List<ProductInfo> list = new ArrayList<>();

        long time = System.currentTimeMillis();

        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo2",time));
        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo3",time));
        list.add(new ProductInfo("ProductInfo4",time));
        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo1",time));
        list.add(new ProductInfo("ProductInfo10",time));
        return list;
    }

    @Override
    public ProductInfo getMock() {
        return null;
    }
}
