package com.xilu.wybz.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.PasswordPresenter;
import com.xilu.wybz.ui.IView.IPasswordView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.MyCountTimer;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 2016/5/4.
 */
public class PasswordActivity extends ToolbarActivity implements IPasswordView, TextWatcher {
    PasswordPresenter passwordPresenter;
    @Bind(R.id.mpass_phone)
    EditText mpassPhone;
    @Bind(R.id.mpass_phonepass)
    EditText mpassPhonepass;
    @Bind(R.id.mpass_phonebut)
    TextView mpassPhonebut;
    @Bind(R.id.mpass_pass)
    EditText mpassPass;
    @Bind(R.id.mpass_ypass)
    EditText mpassYpass;
    @Bind(R.id.mpass_login)
    TextView mpassLogin;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordPresenter = new PasswordPresenter(context, this);
        passwordPresenter.init();
    }

    @Override
    public void initView() {
        mpassPhone.addTextChangedListener(this);
        mpassPhonepass.addTextChangedListener(this);
        mpassPass.addTextChangedListener(this);
        mpassYpass.addTextChangedListener(this);
    }

    @Override
    public void modifyPwdStart() {
        mpassLogin.setEnabled(false);
    }

    @Override
    public void modifyPwdSuccess(UserBean userBean) {
        if (userBean != null) {
            showMsg("密码修改成功");
            EventBus.getDefault().post(new Event.LoginSuccessEvent(userBean));
            finish();
        } else {
            showMsg("密码修改失败");
        }
    }

    @Override
    public void modifyPwdFail() {
        showNetErrorMsg();
    }

    @Override
    public void modifyPwdFinish() {
        mpassLogin.setEnabled(true);
    }

    @Override
    public void SmsCodeStart() {
        mpassPhonebut.setEnabled(false);
    }

    @Override
    public void SmsCodeSuccess(String result) {
        DataBean mb = ParseUtils.getDataBean(context,result);
        if (mb != null) {
            if (mb.code==200) {
                MyCountTimer timeCount = new MyCountTimer(mpassPhonebut);
                timeCount.start();
                mpassPhonepass.setFocusable(true);
                mpassPhonepass.setFocusableInTouchMode(true);
                mpassPhonepass.requestFocus();
            }
        } else {
            showMsg("验证码发送失败");
        }
    }

    @Override
    public void SmsCodeFail() {
        mpassPhonebut.setEnabled(true);
        showNetErrorMsg();
    }

    @Override
    public void SmsCodeFinish() {

    }


    @OnClick({R.id.mpass_phonebut, R.id.mpass_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mpass_phonebut:
                break;
            case R.id.mpass_login:
                toLogin();
                break;
        }
    }

    private void getSmsCode() {
        String phone = mpassPhone.getText().toString().trim();
        if (!StringUtil.checkPhone(phone)) {
            showMsg("请输入正确手机号码");
        } else {
            passwordPresenter.getSmsCode(phone, "2");
        }
    }

    private void toLogin() {
        String phone = mpassPhone.getText().toString().trim();
        String code = mpassPhonepass.getText().toString().trim();
        String password = MD5Util.getMD5String(mpassPass.getText().toString().trim());
        String repassword = MD5Util.getMD5String(mpassYpass.getText().toString().trim());
        if (StringUtil.isEmpty(phone)) {
            showMsg("手机号不能为空");
            return;
        } else if (StringUtil.isEmpty(code)) {
            showMsg("验证码不能为空");
            return;
        } else if (password.length() < 6) {
            showMsg("密码不能少于6个字符");
            return;
        } else if (!repassword.equals(password)) {
            showMsg("两次密码要输入一致");
            return;
        }
        passwordPresenter.modifyPwd(phone, code, password, repassword);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = mpassPhone.getText().toString().trim();
        String code = mpassPhonepass.getText().toString().trim();
        String pass = mpassPass.getText().toString().trim();
        String ypass = mpassYpass.getText().toString().trim();
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code) || StringUtil.isEmpty(pass) || StringUtil.isEmpty(ypass)) {
            mpassLogin.setEnabled(false);
            mpassLogin.setBackgroundResource(R.drawable.corner_login);
        } else {
            mpassLogin.setEnabled(true);
            mpassLogin.setBackgroundResource(R.drawable.corner_login2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(passwordPresenter!=null)
        passwordPresenter.cancelUrl();
    }
}
