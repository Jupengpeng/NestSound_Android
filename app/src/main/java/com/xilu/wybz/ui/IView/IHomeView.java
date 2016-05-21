package com.xilu.wybz.ui.IView;

/**
 * Created by June on 16/4/5.
 */
public interface IHomeView extends IBaseView{
    void loadDataStart();
    void loadDataSuccess(String result);
    void loadDataFail(String msg);
    void loadDataFinish();
}
