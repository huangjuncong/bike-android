package com.coder520.mamabike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.to.LoginDataTo;
import com.coder520.mamabike.presenter.LoginPresenter;
import com.coder520.mamabike.utils.TimeCountUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by zhongyanli on 2017/9/8.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.edit_phone_number)
    EditText editPhoneNumber;
    @BindView(R.id.edit_verify_code)
    EditText editVerifyCode;
    @BindView(R.id.spinner_item)
    Spinner spinnerItem;
    @BindView(R.id.btn_get_authentication)
    Button btnGetAuthentication;
    @BindView(R.id.btn_ok)
    Button btnOk;
    private TimeCountUtil mTimeCountUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mTimeCountUtil = TimeCountUtil.create(TAG, this, 60 * 1000, 1000, btnGetAuthentication);
    }

    @Override
    protected int getLeftActionDrawable() {
        return R.drawable.ic_actionbar_fork;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @OnClick(R.id.btn_ok)
    public void onLoginClicked() {
        String phoneNumber = editPhoneNumber.getText().toString();
        String verifyCode = editVerifyCode.getText().toString();
        LoginDataTo loginDataTo = new LoginDataTo(phoneNumber, verifyCode);
        mPresenter.doLogin(loginDataTo);
    }

    @OnClick(R.id.btn_get_authentication)
    public void onGetVerifyCodeClick() {
        String phoneNumber = editPhoneNumber.getText().toString();
        mPresenter.doSendAuthenticationCode(phoneNumber);
    }

    @OnTextChanged(R.id.edit_phone_number)
    public void onPhoneNumberChanged(CharSequence s, int start, int before, int count) {
        boolean enable = !TextUtils.isEmpty(s);
        btnGetAuthentication.setEnabled(enable);
    }

    @OnTextChanged(R.id.edit_verify_code)
    public void onVerifyCodeChanged(CharSequence s, int start, int before, int count) {
        boolean enable = !TextUtils.isEmpty(s);
        btnOk.setEnabled(enable);
    }

    @Override
    protected String getActionTitle() {
        return getString(R.string.verify_phone);
    }

    @Override
    protected View.OnClickListener getLeftActionClickAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                launchActivity(false, intent);
                finish();
            }
        };
    }

    /**
     * 开始计时
     */
    public void startCountDown() {
        mTimeCountUtil.startRun();
    }

}
