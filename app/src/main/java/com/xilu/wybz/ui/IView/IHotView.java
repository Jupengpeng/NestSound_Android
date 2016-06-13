package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.TemplateBean;

import java.util.List;

/**
 * Created by hujunwei on 16/4/5.
 */
public interface IHotView extends IBaseView {

    void showHotData(List<TemplateBean> templateBeens,int currentType);

    void loadFail();

    void loadNoMore();

    void downloadSuccess();

    void loadNoData();
}
