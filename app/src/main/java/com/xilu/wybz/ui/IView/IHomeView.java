package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.MainBean;

/**
 * Created by June on 16/4/5.
 */
public interface IHomeView extends IBaseView{
    void showMainData(MainBean mineBean);
    void loadDataFail(String msg);
    void loadDataFinish();
}
