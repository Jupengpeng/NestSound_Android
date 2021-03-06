package com.xilu.wybz.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.LoginBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.LoginPresenter;
import com.xilu.wybz.presenter.RegisterPresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.ui.IView.IRegisterView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.MyCountTimer;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/8/5.
 */
public class LoginActivity extends BaseActivity implements ILoginView, IRegisterView, TextWatcher {
    @Bind(R.id.iv_flag1)
    ImageView ivFlag1;
    @Bind(R.id.iv_flag2)
    ImageView ivFlag2;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.et_reg_phone)
    EditText etRegPhone;
    @Bind(R.id.et_reg_code)
    EditText etRegCode;
    @Bind(R.id.et_reg_pwd)
    EditText etRegPwd;
    @Bind(R.id.et_reg_pwd2)
    EditText etRegPwd2;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(R.id.cb_agreement)
    CheckBox cbAgreement;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_reg)
    TextView tvReg;
    @Bind(R.id.tv_choice_login)
    TextView tvChoiceLogin;
    @Bind(R.id.tv_choice_reg)
    TextView tvChoiceReg;
    @Bind(R.id.tv_regcode)
    TextView tvRegcode;
    @Bind(R.id.ll_agreement)
    LinearLayout llAgreement;
    @Bind(R.id.ll_other_login)
    LinearLayout llOtherLogin;
    LoginPresenter loginPresenter;
    RegisterPresenter registerPresenter;
    UMShareAPI mShareAPI;
    boolean isFirstGetUserInfo;
    LoginBean userInfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(this, this);
        registerPresenter = new RegisterPresenter(this, this);
        loginPresenter.init();
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        toLoginOrReg(0);
        etNickname.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etRegCode.addTextChangedListener(this);
        etRegPhone.addTextChangedListener(this);
        etRegPwd.addTextChangedListener(this);
        etRegPwd2.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String user = etPhone.getText().toString();
        String pass = etPassword.getText().toString();
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pass)) {
            tvLogin.setEnabled(false);
            tvLogin.setBackgroundResource(R.drawable.corner_login);
        } else {
            tvLogin.setEnabled(true);
            tvLogin.setBackgroundResource(R.drawable.corner_login2);
        }
        String nickname = etNickname.getText().toString();
        String phone = etRegPhone.getText().toString();
        String regcode = etRegCode.getText().toString();
        String pwd = etRegPwd.getText().toString();
        String pwd2 = etRegPwd2.getText().toString();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(regcode)
                || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(pwd2)) {
            tvReg.setEnabled(false);
            tvReg.setBackgroundResource(R.drawable.corner_login);
        } else {
            tvReg.setEnabled(true);
            tvReg.setBackgroundResource(R.drawable.corner_login2);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_qq_login, R.id.tv_wx_login, R.id.tv_wb_login,
            R.id.tv_forget_pwd, R.id.tv_agreement, R.id.tv_login, R.id.tv_reg,
            R.id.tv_choice_login, R.id.tv_choice_reg, R.id.tv_regcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qq_login:
                otherLogin("qq");
                break;
            case R.id.tv_wx_login:
                otherLogin("wx");
                break;
            case R.id.tv_wb_login:
                otherLogin("wb");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forget_pwd:
                startActivity(PassWorddActivity.class);
                break;
            case R.id.tv_login:
                toLoin();
                break;
            case R.id.tv_reg:
                toReg();
                break;
            case R.id.tv_choice_login:
                toLoginOrReg(0);
                break;
            case R.id.tv_choice_reg:
                toLoginOrReg(1);
                break;
            case R.id.tv_regcode:
                getSmsCode();
                break;
            case R.id.tv_agreement:
                BrowserActivity.toBrowserActivity(context, MyCommon.AGREEMENT);
                break;
        }
    }

    public void otherLogin(String type) {
        SHARE_MEDIA platform = null;
        if (type.equals("qq")) {
            platform = SHARE_MEDIA.QQ;
        } else if (type.equals("wx")) {
            platform = SHARE_MEDIA.WEIXIN;
        } else if (type.equals("wb")) {
            platform = SHARE_MEDIA.SINA;
        }
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(this);
        }
        mShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareAPI == null) mShareAPI = UMShareAPI.get(this);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthListener2);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
        }
    };
    private UMAuthListener umAuthListener2 = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            userInfo = new LoginBean();
            Log.e("Authorize" + platform.toString(), new Gson().toJson(data));
            //返回的token或者id信息
            if (platform.toString().equals("WEIXIN")) {
                try {
                    String openid = data.get("openid");
                    String sex = data.get("sex");
                    String headimgurl = data.get("headimgurl");
                    String nickname = data.get("nickname");
                    userInfo.openid = openid;
                    userInfo.nickname = nickname;
                    userInfo.signature = "";
                    userInfo.headurl = headimgurl;
                    userInfo.sex = sex;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (platform.toString().equals("SINA")) {
                try {
                    String result = data.get("result");
                    JSONObject jsonObject = new JSONObject(result);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String avatar_large = jsonObject.getString("avatar_large");
                    String gender = jsonObject.getString("gender");//"m"男 "f" 女
                    userInfo.openid = id;
                    userInfo.nickname = name;
                    userInfo.headurl = avatar_large;
                    userInfo.signature = description;
                    userInfo.sex = gender.equals("m") ? "2" : (gender.equals("f") ? "1" : "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (platform.toString().equals("QQ")) {
                try {
                    String profile_image_url = data.get("profile_image_url");
                    String screen_name = data.get("screen_name");
                    String gender = data.get("gender");//"男" ”女“
                    String openid = data.get("openid");//"男" ”女“
                    userInfo.openid = openid;
                    userInfo.nickname = screen_name;
                    userInfo.headurl = profile_image_url;
                    userInfo.signature = "";
                    userInfo.sex = gender.equals("男") ? "2" : (gender.equals("女") ? "1" : "0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.e("userInfo", new Gson().toJson(userInfo));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
        }
    };

    public void toLoin() {
        String mobile = etPhone.getText().toString();
        String password = MD5Util.getMD5String(etPassword.getText().toString());
        if (mobile.trim().equals("")) {
            showMsg("请输入手机号");
        } else if (password.trim().equals("")) {
            showMsg("请输入密码");
        }
        loginPresenter.login(mobile, password);
    }

    public void toReg() {
        String phone = etRegPhone.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        String code = etRegCode.getText().toString().trim();
        String password = MD5Util.getMD5String(etRegPwd.getText().toString().trim());
        String repassword = MD5Util.getMD5String((etRegPwd2.getText().toString().trim()));
        if (!cbAgreement.isChecked()) {
            showMsg("请先同意使用协议");
            return;
        }
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
        registerPresenter.register(nickname, phone, code, password, repassword);
    }

    public void getSmsCode() {
        String phone = etRegPhone.getText().toString().trim();
        if (StringUtils.isBlank(phone)) {
            showMsg("请输入手机号码");
        } else {
            registerPresenter.getSmsCode(phone, "1");
        }
    }

    //切换注册和登录 flag 0 登录 1注册
    public void toLoginOrReg(int flag) {
        llLogin.setVisibility(flag == 0 ? View.VISIBLE : View.GONE);
//        llOtherLogin.setVisibility(flag == 0 ? View.VISIBLE : View.GONE);
        llAgreement.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
        llRegister.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
        tvLogin.setVisibility(flag == 0 ? View.VISIBLE : View.GONE);
        tvReg.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
        ivFlag1.setVisibility(flag == 0 ? View.VISIBLE : View.GONE);
        ivFlag2.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
        tvChoiceLogin.setSelected(flag == 0);
        tvChoiceReg.setSelected(flag == 1);
    }

    @Override
    public void loginStart() {
        showPd("正在登陆中...");
        if (isDestroy) {
            return;
        }
        tvLogin.setEnabled(false);
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        EventBus.getDefault().post(new Event.LoginSuccessEvent(userBean));

    }

    @Override
    public void loginFail() {
        showNetErrorMsg();
    }

    @Override
    public void loginFinish() {
        cancelPd();
        if (isDestroy) {
            return;
        }
        tvLogin.setEnabled(true);
    }

    @Override
    public void registerStart() {
        showPd("正在注册中...");
        if (isDestroy) return;
        tvReg.setEnabled(false);
    }

    @Override
    public void registerSuccess(UserBean userBean) {
        if (isDestroy) return;
        if (userBean != null) {
            showMsg("注册成功");
            EventBus.getDefault().post(new Event.LoginSuccessEvent(userBean));
            finish();
        }
    }

    @Override
    public void registerFail() {
        showNetErrorMsg();
    }

    @Override
    public void registerFinish() {
        cancelPd();
        if (isDestroy) {
            return;
        }
        tvReg.setEnabled(true);
    }

    @Override
    public void SmsCodeStart() {
        if (isDestroy) {
            return;
        }
        tvRegcode.setEnabled(false);
    }

    @Override
    public void SmsCodeSuccess(String result) {
        if (isDestroy) return;
        DataBean mb = ParseUtils.getDataBean(context, result);
        if (mb != null) {
            if (mb.code == 200) {
                MyCountTimer timeCount = new MyCountTimer(tvRegcode);
                timeCount.start();
                if (isDestroy) {
                    return;
                }
                tvRegcode.setFocusable(true);
                tvRegcode.setFocusableInTouchMode(true);
                tvRegcode.requestFocus();
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
        if (isDestroy) return;
        tvRegcode.setEnabled(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (loginPresenter != null)
            loginPresenter.cancelRequest();
        if (registerPresenter != null)
            registerPresenter.cancelRequest();
    }
}
