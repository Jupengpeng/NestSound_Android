package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.UserBean;

/**
 * Created by June on 16/4/5.
 */
public interface ILoginView extends IBaseView{
    void loginStart();
    void loginSuccess(UserBean userBean);
    void loginFail();
    void loginFinish();

}
