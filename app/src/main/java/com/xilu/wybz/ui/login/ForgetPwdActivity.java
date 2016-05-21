package com.xilu.wybz.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.presenter.ForgetPwdPresenter;
import com.xilu.wybz.ui.IView.IForgetPwdView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.MyCountTimer;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.StringUtil;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/3/25.
 */
public class ForgetPwdActivity extends ToolbarActivity implements IForgetPwdView {
    @Bind(R.id.tv_ok)
    TextView tv_ok;
    @Bind(R.id.tv_getsmscode)
    TextView tv_getsmscode;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_newpwd)
    EditText et_newpwd;
    @Bind(R.id.et_sms_code)
    EditText et_smscode;
    MyCountTimer timeCount;
    public boolean isPhoneNum;//电话号码
    public ForgetPwdPresenter forgetPwdPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPwdPresenter = new ForgetPwdPresenter(this, this);
        forgetPwdPresenter.init();
        initEvent();
    }

    public void initView() {
        setTitle("忘记密码");
    }

    @OnClick({R.id.tv_getsmscode, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getsmscode:
                getCode();
                break;
            case R.id.tv_ok:
                tv_ok.setEnabled(false);
                toLogin();
                break;
        }
    }

    protected void initEvent() {
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ChangeOkBtnState();
            }
        });
        et_newpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ChangeOkBtnState();
            }
        });
        et_smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ChangeOkBtnState();
            }
        });

    }

    //验证码发送
    public void getCode() {
        String phone = et_phone.getText().toString().trim();
        if (!StringUtil.checkPhone(phone)) {
            showMsg("请输入正确手机号码");
        } else {
            forgetPwdPresenter.getSmsCode(phone);
            tv_getsmscode.setClickable(false);
        }
    }

    //改变确定按钮点击状态
    public void ChangeOkBtnState() {
        String phonenum = et_phone.getText().toString().trim();
        isPhoneNum = StringUtil.checkPhone(phonenum);
        if (timeCount == null || !timeCount.isStart) {
            if (isPhoneNum) {
                tv_getsmscode.setBackgroundResource(R.drawable.corner_yellow3);
                tv_getsmscode.setEnabled(true);
            } else {
                tv_getsmscode.setEnabled(false);
                tv_getsmscode.setBackgroundResource(R.drawable.corner_e5e5e52);
            }
        }
        if (phonenum.startsWith("1") && phonenum.length() == 11 &&
                et_smscode.getText().toString().trim().length() > 0 &&
                et_newpwd.getText().toString().trim().length() > 0) {
            tv_ok.setBackgroundResource(R.drawable.ok_btn2);
            tv_ok.setEnabled(true);
        } else {
            tv_ok.setBackgroundResource(R.drawable.ok_btn);
            tv_ok.setEnabled(false);
        }
    }

    public void toLogin() {
        String phone = et_phone.getText().toString().trim();
        String code = et_smscode.getText().toString().trim();
        String password = MD5Util.getMD5String(et_newpwd.getText().toString().trim());
        if (password.length() < 6) showMsg("密码不能小于6位");
        forgetPwdPresenter.modifyPwd(phone, password, code);
    }

    @Override
    public void getSmsCodeSuccess(String result) {
        if (ParseUtils.checkCode(result)) {
            showMsg("验证码发送成功");
            timeCount = new MyCountTimer(tv_getsmscode);
            timeCount.start();
            tv_getsmscode.setBackgroundResource(R.drawable.corner_e5e5e52);
            et_smscode.setFocusable(true);
            et_smscode.setFocusableInTouchMode(true);
            et_smscode.requestFocus();
        }
    }

    @Override
    public void getSmsCodeFail() {
        tv_getsmscode.setClickable(true);
        showMsg("验证码发送失败");
    }


    @Override
    public void modifyPwdSuccess(String result) {
        tv_ok.setEnabled(true);
        if (ParseUtils.checkCode(result)) {
            showMsg("密码修改成功");
            finish();
        } else {
            showMsg("密码修改失败");
        }
    }

    @Override
    public void modifyPwdFail() {
        tv_ok.setEnabled(true);
        showMsg("密码修改失败");
    }
}
