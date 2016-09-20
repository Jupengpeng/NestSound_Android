package com.xilu.wybz.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.presenter.ModifyPwdPresenter;
import com.xilu.wybz.ui.IView.IModifyPwdView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 2016/5/4.
 */
public class ModifyPwdActivity extends ToolbarActivity implements IModifyPwdView,TextWatcher {
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd1)
    EditText etPwd1;
    @Bind(R.id.et_pwd2)
    EditText etPwd2;
    @Bind(R.id.tv_ok)
    TextView tvOk;
    ModifyPwdPresenter modifyPwdPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modifyPwdPresenter = new ModifyPwdPresenter(this,this);
        modifyPwdPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("修改密码");
        etPwd.addTextChangedListener(this);
        etPwd1.addTextChangedListener(this);
        etPwd2.addTextChangedListener(this);
    }
    public void toModify() {
        String oPwd = MD5Util.getMD5String(etPwd.getText().toString().trim());
        String nPws = MD5Util.getMD5String(etPwd1.getText().toString()).trim();
        String rPws = MD5Util.getMD5String(etPwd2.getText().toString()).trim();
        if (!nPws.equals(rPws)) {
            showMsg("两次输入的密码不一致！");
            return;
        }
        modifyPwdPresenter.modifyPwd(oPwd,nPws,rPws);
    }
    @OnClick(R.id.tv_ok)
    public void onClick() {
        toModify();
    }

    @Override
    public void modifyPwdStart() {
        showPd("正在修改中,请稍候...");
        tvOk.setEnabled(false);
    }

    @Override
    public void modifyPwdSuccess() {
        ToastUtils.toast(context,"修改成功！");
        finish();
    }

    @Override
    public void modifyPwdFail() {
    }
    @Override
    public void modifyPwdFinish() {
        cancelPd();
        tvOk.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String oPwd = etPwd.getText().toString().toString().trim();
        String nPwd = etPwd1.getText().toString().toString().trim();
        String rPwd = etPwd2.getText().toString().toString().trim();
        if (StringUtils.isBlank(oPwd) || StringUtils.isBlank(nPwd)|| StringUtils.isBlank(rPwd)) {
            tvOk.setEnabled(false);
            tvOk.setBackgroundResource(R.drawable.corner_login);
        } else {
            tvOk.setEnabled(true);
            tvOk.setBackgroundResource(R.drawable.corner_login2);
        }
    }
}
