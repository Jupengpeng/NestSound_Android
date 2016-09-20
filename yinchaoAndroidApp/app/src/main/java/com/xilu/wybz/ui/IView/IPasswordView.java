package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.UserBean;

/**
 * Created by June on 16/4/5.
 */
public interface IPasswordView extends IBaseView{
    void modifyPwdStart();
    void modifyPwdSuccess(UserBean userBean);
    void modifyPwdFail();
    void modifyPwdFinish();

    void SmsCodeStart();
    void SmsCodeSuccess(String result);
    void SmsCodeFail();
    void SmsCodeFinish();
}
