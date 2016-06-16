package com.xilu.wybz.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.LoginPresenter;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 2016/5/4.
 */
public class LoginActivity extends BaseActivity implements ILoginView,TextWatcher {
    @Bind(R.id.mlogin_user)
    EditText mloginUser;
    @Bind(R.id.mlogin_pass)
    EditText mloginPass;
    @Bind(R.id.mlogin_login)
    TextView mloginLogin;
    LoginPresenter loginPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(this,this);
        loginPresenter.init();
    }

    public void initView() {
        mloginPass.addTextChangedListener(this);
        mloginUser.addTextChangedListener(this);

        mloginPass.setText("");
        mloginUser.setText("");
    }

    @OnClick({R.id.iv_back, R.id.mlogin_login, R.id.mlogin_forget, R.id.mlogin_reg})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mlogin_reg:
                startActivity(RegisterActivity.class);
                break;
            case R.id.mlogin_forget:
                startActivity(PasswordActivity.class);
                break;
            case R.id.mlogin_login:
                toLoin();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    public void toLoin() {
        String mobile = mloginUser.getText().toString();
        String password = MD5Util.getMD5String(mloginPass.getText().toString());
        if (mobile.trim().equals("")) {
            showMsg("请输入手机号");
        } else if (password.trim().equals("")) {
            showMsg("请输入密码");
        }
        loginPresenter.login(mobile,password);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String user = mloginUser.getText().toString();
        String pass = mloginPass.getText().toString();
        if (StringUtil.isEmpty(user) || StringUtil.isEmpty(pass)) {
            mloginLogin.setEnabled(false);
            mloginLogin.setBackgroundResource(R.drawable.corner_login);
        } else {
            mloginLogin.setEnabled(true);
            mloginLogin.setBackgroundResource(R.drawable.corner_login2);
        }

    }    @Override
    public void loginStart() {
        showPd("正在登陆中...");
        mloginLogin.setEnabled(false);
    }

    @Override
    public void loginSuccess(UserBean ub) {
        EventBus.getDefault().post(new Event.LoginSuccessEvent(ub));
        finish();
    }

    @Override
    public void loginFail() {
        showNetErrorMsg();
    }

    @Override
    public void loginFinish() {
        cancelPd();
        mloginLogin.setEnabled(true);
    }

}
