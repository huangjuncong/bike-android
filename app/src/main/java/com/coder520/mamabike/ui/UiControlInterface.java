package com.coder520.mamabike.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

/**
 * UI控制接口
 * Created by yadong on 2017/5/23.
 */

public interface UiControlInterface {
    Context getContextInternal();
    void launchActivityForResult(int requestCode, Intent intent);
    void launchActivity(boolean loginRequire, Intent intent);
    void showAlert(Dialog dialog);
    void dismissAlert();
    void showToast(String message);
    void finish();
    void showProgress(String message);
    void dismissProgress();
}
