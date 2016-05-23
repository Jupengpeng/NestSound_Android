package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ISaveWordView extends com.xilu.wybz.ui.IView.IBaseView {
    void saveWordSuccess(String response);
    void saveWordFail();
}
