package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.LyricsDraftBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public interface IDraftListView extends IBaseView {

    void onSuccess(List<LyricsDraftBean> list);

    void onError(String message);
}
