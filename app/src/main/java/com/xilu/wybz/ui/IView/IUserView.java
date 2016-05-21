package com.xilu.wybz.ui.IView;

/**
 * Created by hujunwei on 16/4/5.
 * 用户主页 也可以是我的主页
 */
public interface IUserView extends IBaseView {
    void setUserInfo();

    void loadUserInfoSuccess(String result);

    void loadFocusFansCountSuccess(String result);

    void loadUserInfoFail(String msg);

    void loadFocusFansCountFail(String msg);

    void delSuccess(String result);

    void delFail(String msg);
}
