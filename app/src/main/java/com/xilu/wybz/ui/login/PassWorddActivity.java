package com.xilu.wybz.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.PasswordPresenter;
import com.xilu.wybz.ui.IView.IPasswordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.MyCountTimer;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
/**
 * Created by June on 16/3/25.
 */
public class PassWorddActivity extends ToolbarActivity implements IPasswordView,TextWatcher {
    PasswordPresenter passwordPresenter;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_sms_code)
    EditText etSmsCode;
    @Bind(R.id.tv_getsmscode)
    TextView tvGetsmscode;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd2)
    EditText etPwd2;
    @Bind(R.id.tv_ok)
    TextView tvOk;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordPresenter = new PasswordPresenter(this, this);
        passwordPresenter.init();
    }

    public void initView() {
        setTitle("忘记密码");
        etPhone.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etPwd2.addTextChangedListener(this);
        etSmsCode.addTextChangedListener(this);
    }

    @OnClick({R.id.tv_getsmscode, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getsmscode:
                getSmsCode();
                break;
            case R.id.tv_ok:
                tvOk.setEnabled(false);
                toLogin();
                break;
        }
    }
    private void getSmsCode() {
        String phone = etPhone.getText().toString().trim();
        if (StringUtils.isBlank(phone)) {
            showMsg("请输入手机号码");
        } else {
            passwordPresenter.getSmsCode(phone, "2");
        }
    }

    private void toLogin() {
        String phone = etPhone.getText().toString().trim();
        String code = etSmsCode.getText().toString().trim();
        String password = MD5Util.getMD5String(etPwd.getText().toString().trim());
        String repassword = MD5Util.getMD5String(etPwd2.getText().toString().trim());
        if (StringUtils.isEmpty(phone)) {
            showMsg("手机号不能为空");
            return;
        } else if (StringUtils.isEmpty(code)) {
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
    public void modifyPwdStart() {
        tvOk.setEnabled(false);
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
        tvOk.setEnabled(true);
    }

    @Override
    public void SmsCodeStart() {
        tvGetsmscode.setEnabled(false);
    }

    @Override
    public void SmsCodeSuccess(String result) {
        DataBean mb = ParseUtils.getDataBean(context,result);
        if (mb != null) {
            if (mb.code==200) {
                MyCountTimer timeCount = new MyCountTimer(tvGetsmscode);
                timeCount.start();
                etSmsCode.setFocusable(true);
                etSmsCode.setFocusableInTouchMode(true);
                etSmsCode.requestFocus();
            }
        } else {
            showMsg("验证码发送失败");
        }
    }

    @Override
    public void SmsCodeFail() {
        showNetErrorMsg();
        if (isDestroy){
            return;
        }
    }

    @Override
    public void SmsCodeFinish() {
        tvGetsmscode.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (passwordPresenter != null)
            passwordPresenter.cancelRequest();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = etPhone.getText().toString().trim();
        String code = etSmsCode.getText().toString().trim();
        String pass = etPwd.getText().toString().trim();
        String ypass = etPwd2.getText().toString().trim();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code) || StringUtils.isEmpty(pass) || StringUtils.isEmpty(ypass)) {
            tvOk.setEnabled(false);
            tvOk.setBackgroundResource(R.drawable.corner_login);
        } else {
            tvOk.setEnabled(true);
            tvOk.setBackgroundResource(R.drawable.corner_login2);
        }
    }
}
