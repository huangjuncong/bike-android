package com.coder520.mamabike.presenter;

import android.content.Context;
import android.content.Intent;

import com.coder520.mamabike.ui.UiControlInterface;


public abstract class BasePresenter {
    private UiControlInterface mUiControlInterface;
    protected Context mContext;

    public BasePresenter(UiControlInterface uiControlInterface) {
        this.mUiControlInterface = uiControlInterface;
        this.mContext = mUiControlInterface.getContextInternal();
    }

    protected UiControlInterface getUiControlInterface() {
        return mUiControlInterface;
    }

    public void onCreate() {
    }

    public void start(Intent intent) {
    }

    public void stop() {
    }

    public void onDestroy() {
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    protected String getString(int resId) {
        return mContext.getString(resId);
    }


}
