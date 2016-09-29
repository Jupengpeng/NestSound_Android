package com.xilu.wybz.ui.IView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public interface IDefaultListView<T> extends IBaseView{

    void onSuccess(List<T> list);

    void onError(String message);

}
