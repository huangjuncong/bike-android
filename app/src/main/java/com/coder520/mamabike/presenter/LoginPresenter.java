package com.coder520.mamabike.presenter;

import android.content.Intent;
import android.widget.TextView;

import com.coder520.mamabike.entity.to.LoginDataTo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.manager.UserManager;
import com.coder520.mamabike.ui.UiControlInterface;
import com.coder520.mamabike.ui.activity.LoginActivity;
import com.coder520.mamabike.ui.activity.MainActivity;



public class LoginPresenter extends BasePresenter {
    public LoginPresenter(UiControlInterface uiControlInterface) {
        super(uiControlInterface);
    }

    public void doLogin(LoginDataTo loginDataTo) {
        UserManager.getInstance().doLogin(loginDataTo, new DataListener<String>() {
            @Override
            public void onSuccess(String data, String message) {
                Intent loginIntent = new Intent(getUiControlInterface().getContextInternal(),
                        MainActivity.class);
                getUiControlInterface().launchActivity(true, loginIntent);
                getUiControlInterface().finish();
            }

            @Override
            public void onFailed(Throwable error, String message) {
                getUiControlInterface().showToast(message);
            }

            @Override
            public void onEnd() {
            }
        });
    }

    public void doSendAuthenticationCode(String phoneNumber) {
        UserManager.getInstance().sendMessageVerifyCode(phoneNumber, new DataListener<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
            }

            @Override
            public void onFailed(Throwable error, String message) {
            }

            @Override
            public void onEnd() {
                ((LoginActivity)getUiControlInterface()).startCountDown();
            }
        });
    }
}
