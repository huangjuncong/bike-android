package com.coder520.mamabike.ui.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.to.LoginDataTo;
import com.coder520.mamabike.entity.to.LoginTo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.DefaultResponseDispatcher;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.internet.net.UserInterface;
import com.coder520.mamabike.presenter.BasePresenter;
import com.coder520.mamabike.presenter.OpenLockPresenter;
import com.coder520.mamabike.utils.AESUtil;
import com.coder520.mamabike.utils.Base64Util;
import com.coder520.mamabike.utils.RSAUtil;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yadong on 2017/8/19.
 */

public class OpenLockActivity extends BaseActivity<OpenLockPresenter>
        implements QRCodeReaderView.OnQRCodeReadListener {

    @BindView(R.id.qrdecoderview)
    com.dlazaro66.qrcodereaderview.QRCodeReaderView mQqrdecoderview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lock);
        ButterKnife.bind(this);
        mQqrdecoderview.setOnQRCodeReadListener(this);
        // Use this function LoginTo enable/disable decoding
        mQqrdecoderview.setQRDecodingEnabled(true);
        // Use this function LoginTo change the autofocus interval (default is 5 secs)
        mQqrdecoderview.setAutofocusInterval(2000L);
        // Use this function LoginTo enable/disable Torch
        mQqrdecoderview.setTorchEnabled(true);
        // Use this function LoginTo set back camera preview
        mQqrdecoderview.setBackCamera();
    }

    @Override
    protected OpenLockPresenter createPresenter() {
        return new OpenLockPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQqrdecoderview.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQqrdecoderview.stopCamera();
    }

    @Override
    protected String getActionTitle() {
        return getString(R.string.scan_open_lock);
    }

    @Override
    protected String getRightActionText() {
        return getString(R.string.use_help);
    }

    @Override
    protected View.OnClickListener getRightActionClickAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mQqrdecoderview.stopCamera();
        mPresenter.openLock(text);
    }
}
