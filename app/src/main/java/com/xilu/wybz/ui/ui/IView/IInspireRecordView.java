package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.ui.IView.*;

/**
 * Created by June on 16/4/5.
 */
public interface IInspireRecordView extends com.xilu.wybz.ui.IView.IBaseView {
    void pubStart();
    void pubSuccess(String result);
    void pubFail();
}
