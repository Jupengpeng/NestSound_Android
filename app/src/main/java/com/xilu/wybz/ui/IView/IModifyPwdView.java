package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.UserBean;

import java.util.List;

/**
 * Created by hujunwei on 16/4/5.
 */
public interface IModifyPwdView extends IBaseView {
    void modifyPwdStart();
    void modifyPwdSuccess();
    void modifyPwdFail();
    void modifyPwdFinish();
}
