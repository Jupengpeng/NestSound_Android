package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.LyricsDraftBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public interface ITempleateListLrcView extends IBaseView{

    void onSuccess(List<LyricsDraftBean> list);
    void onError(String error);

}
