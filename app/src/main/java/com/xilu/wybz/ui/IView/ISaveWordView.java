package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CollectionBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ISaveWordView extends IBaseView {
    void saveWordSuccess(String response);
    void saveWordFail();
    void onFinish();
}
