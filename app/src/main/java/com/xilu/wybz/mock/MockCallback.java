package com.xilu.wybz.mock;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */

public interface MockCallback<T> {

    List<T> getMockList();
    T getMock();
}
