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
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.RegisterPresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IRegisterView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.MyCountTimer;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by June on 2016/5/4.
 */
public class RegisterActivity extends ToolbarActivity implements IRegisterView,TextWatcher {
    RegisterPresenter registerPresenter;
    @Bind(R.id.mreg_user)
    EditText mregUser;
    @Bind(R.id.mreg_phone)
    EditText mregPhone;
    @Bind(R.id.mreg_phonepass)
    EditText mregPhonepass;
    @Bind(R.id.mreg_phonebut)
    TextView mregPhonebut;
    @Bind(R.id.mreg_pass)
    EditText mregPass;
    @Bind(R.id.mreg_ypass)
    EditText mregYpass;
    @Bind(R.id.mreg_reg)
    TextView mregReg;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPresenter = new RegisterPresenter(context, this);
        registerPresenter.init();
    }

    @Override
    public void registerStart() {
        if(isDestroy)return;
        mregReg.setEnabled(false);
    }

    @Override
    public void registerSuccess(UserBean ub) {
        if(isDestroy)return;
        if (ub != null) {
            showMsg("注册成功");
            EventBus.getDefault().post(new Event.LoginSuccessEvent(ub));
            finish();
        }
    }
    @Override
    public void registerFail() {
        showNetErrorMsg();
    }

    @Override
    public void registerFinish() {
        if (isDestroy){
            return;
        }
        mregReg.setEnabled(true);
    }

    @Override
    public void SmsCodeStart() {
        if (isDestroy){
            return;
        }
        mregPhonebut.setEnabled(false);
    }

    @Override
    public void SmsCodeSuccess(String result) {
        if(isDestroy)return;
        DataBean mb = ParseUtils.getDataBean(context,result);
        if (mb != null) {
            if (mb.code==200) {
                MyCountTimer timeCount = new MyCountTimer(mregPhonebut);
                timeCount.start();
                if (isDestroy){
                    return;
                }
                mregPhonepass.setFocusable(true);
                mregPhonepass.setFocusableInTouchMode(true);
                mregPhonepass.requestFocus();
            }
        } else {
            showMsg("验证码发送失败");
        }

    }

    @Override
    public void SmsCodeFail() {
        showNetErrorMsg();
    }

    @Override
    public void SmsCodeFinish() {
        if(isDestroy)return;
        mregPhonebut.setEnabled(true);
    }

    @Override
    public void initView() {
        mregUser.addTextChangedListener(this);
        mregPhone.addTextChangedListener(this);
        mregPass.addTextChangedListener(this);
        mregPhonepass.addTextChangedListener(this);
        mregYpass.addTextChangedListener(this);
    }

    @OnClick({R.id.mreg_phonebut, R.id.mreg_pass, R.id.tv_agreement, R.id.mreg_reg, R.id.mreg_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mreg_phonebut:
                getSmsCode();
                break;
            case R.id.tv_agreement:
                BrowserActivity.toBrowserActivity(context, MyCommon.AGREEMENT);
                break;
            case R.id.mreg_reg:
                toReg();
                break;
            case R.id.mreg_login:
                finish();
                break;
        }
    }
    public void toReg() {
        String phone = mregPhone.getText().toString().trim();
        String nickname = mregUser.getText().toString().trim();
        String code = mregPhonepass.getText().toString().trim();
        String password = MD5Util.getMD5String(mregPass.getText().toString().trim());
        String repassword = MD5Util.getMD5String((mregYpass.getText().toString().trim()));
        if (StringUtils.isEmpty(nickname)) {
            showMsg("用户名不能为空");
            return;
        } else if (StringUtils.isEmpty(phone)) {
            showMsg("手机号不能为空");
            return;
        } else if (!StringUtils.checkPhone(phone)) {
            showMsg("手机号填写不正确");
            return;
        } else if (StringUtils.isEmpty(code)) {
            showMsg("验证码不能为空");
            return;
        } else if (StringUtils.isEmpty(repassword)) {
            showMsg("确认密码不能为空");
            return;
        } else if (password.length() < 6) {
            showMsg("密码不能少于6个字符");
            return;
        } else if (!repassword.equals(password)) {
            showMsg("两次密码要输入一致");
            return;
        }
        registerPresenter.register(nickname,phone,code,password,repassword);
    }

    public void getSmsCode() {
        String phone = mregPhone.getText().toString().trim();
        if (!StringUtils.checkPhone(phone)) {
            showMsg("请输入正确手机号码");
        } else {
            registerPresenter.getSmsCode(phone, "1");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String user = mregUser.getText().toString();
        String phone = mregPhone.getText().toString();
        String phonepass = mregPhonepass.getText().toString();
        String pass = mregPass.getText().toString();
        String ypass = mregYpass.getText().toString();
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(phonepass) || StringUtils.isEmpty(pass) || StringUtils.isEmpty(ypass)) {
            mregReg.setEnabled(false);
            mregReg.setBackgroundResource(R.drawable.corner_login);
        } else {
            mregReg.setEnabled(true);
            mregReg.setBackgroundResource(R.drawable.corner_login2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(registerPresenter!=null) {
            registerPresenter.cancelRequest();
        }
    }
}
