package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by hujunwei on 16/4/5.
 * 用户主页 也可以是我的主页
 */
public interface IUserView extends IBaseView {

    void setUserInfo(UserBean userBean);
    void setFollowNumber(int number);
    void setFansNumber(int number);

}
