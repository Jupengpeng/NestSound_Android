package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.ui.IView.*;

/**
 * Created by June on 16/4/5.
 */
public interface ILoginView extends com.xilu.wybz.ui.IView.IBaseView {
    void loginStart();
    void loginSuccess(String result);
    void loginFail();
    void loginFinish();

}
