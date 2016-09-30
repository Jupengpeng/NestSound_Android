package com.xilu.wybz.ui.IView;

/**
 * Created by Administrator on 2016/9/30.
 */

public interface ISampleView<T> extends IBaseView {

    void onSuccess(T data);
    void onError(String message);
}
