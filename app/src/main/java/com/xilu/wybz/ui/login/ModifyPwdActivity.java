package com.xilu.wybz.ui.login;

import android.os.Bundle;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;

/**
 * Created by June on 2016/5/4.
 */
public class ModifyPwdActivity extends ToolbarActivity{
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modify_pwd;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("修改密码");
    }
}
