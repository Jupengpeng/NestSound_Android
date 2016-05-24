package com.xilu.wybz.ui.IView;

/**
 * Created by hujunwei on 16/4/5.
 */
public interface IHotView extends IBaseView{
    void loadHotData(String keyword);

    void loadSuccess(String result);

    void loadFail(String msg);
}
