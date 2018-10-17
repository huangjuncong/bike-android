package com.coder520.mamabike.presenter;

import android.content.Intent;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.entity.vo.WalletVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.manager.UserManager;
import com.coder520.mamabike.ui.UiControlInterface;
import com.coder520.mamabike.ui.activity.UserSettingActivity;

/**
 * Created by huang on 2017/9/17.
 */

public class UserSettingPresenter extends BasePresenter {
    private UserSettingActivity mActivity;

    public UserSettingPresenter(UserSettingActivity uiControlInterface) {
        super(uiControlInterface);
        this.mActivity = uiControlInterface;
    }

    @Override
    public void start(Intent intent) {
        loadUserInfo();
    }

    private void loadUserInfo() {
        getUiControlInterface().showProgress(getString(R.string.loading));
        UserManager.getInstance().loadUserInfo(new DataListener<UserVo>() {
            @Override
            public void onSuccess(UserVo data, String message) {
                mActivity.setUserInfo(data);
                loadWalletInfo();
            }

            @Override
            public void onFailed(Throwable error, String message) {
                getUiControlInterface().showToast(message);
                getUiControlInterface().dismissProgress();
            }

            @Override
            public void onEnd() {
            }
        });
    }

    private void loadWalletInfo() {
        UserManager.getInstance().loadWalletInfo(new DataListener<WalletVo>() {
            @Override
            public void onSuccess(WalletVo data, String message) {
                if (data == null) {
                    return;
                }
                mActivity.setWalletInfo(data);
            }

            @Override
            public void onFailed(Throwable error, String message) {
                getUiControlInterface().showProgress(message);
            }

            @Override
            public void onEnd() {
                getUiControlInterface().dismissProgress();
            }
        });
    }

    public void loginOut() {
        UserManager.getInstance().loginOut();
        getUiControlInterface().showProgress(getString(R.string.login_out_success));
        getUiControlInterface().finish();
    }
}
