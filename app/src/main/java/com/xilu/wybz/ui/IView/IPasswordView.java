package com.xilu.wybz.ui.IView;

/**
 * Created by June on 16/4/5.
 */
public interface IPasswordView extends IBaseView{
    void modifyPwdStart();
    void modifyPwdSuccess(String result);
    void modifyPwdFail();
    void modifyPwdFinish();

    void SmsCodeStart();
    void SmsCodeSuccess(String result);
    void SmsCodeFail();
    void SmsCodeFinish();
}
