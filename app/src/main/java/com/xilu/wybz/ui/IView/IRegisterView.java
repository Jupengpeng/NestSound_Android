package com.xilu.wybz.ui.IView;

/**
 * Created by June on 16/4/5.
 */
public interface IRegisterView extends IBaseView{
    void registerStart();
    void registerSuccess(String result);
    void registerFail();
    void registerFinish();

    void SmsCodeStart();
    void SmsCodeSuccess(String result);
    void SmsCodeFail();
    void SmsCodeFinish();
}
