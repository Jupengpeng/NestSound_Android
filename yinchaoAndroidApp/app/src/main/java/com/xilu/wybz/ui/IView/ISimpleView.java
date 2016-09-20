package com.xilu.wybz.ui.IView;

/**
 * Created by Administrator on 2016/8/13.
 */
public interface ISimpleView extends IBaseView{

    void onSuccess(int id, String message);
    void onError(int id, String error);
}
