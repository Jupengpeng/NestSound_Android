package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.ui.IView.*;

/**
 * Created by June on 16/4/5.
 */
public interface IForgetPwdView extends com.xilu.wybz.ui.IView.IBaseView {
    void getSmsCodeSuccess(String result);
    void getSmsCodeFail();

    void modifyPwdSuccess(String result);
    void modifyPwdFail();

}
