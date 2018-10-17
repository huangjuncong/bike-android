package com.coder520.mamabike.manager;

import android.text.TextUtils;

import com.coder520.mamabike.datastore.PreferenceDataSaver;
import com.coder520.mamabike.entity.ResponseEntityVo;
import com.coder520.mamabike.entity.to.LoginDataTo;
import com.coder520.mamabike.entity.to.LoginTo;
import com.coder520.mamabike.entity.to.UserTo;
import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.entity.vo.WalletVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.DefaultResponseDispatcher;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.internet.net.UserInterface;
import com.coder520.mamabike.utils.AESUtil;
import com.coder520.mamabike.utils.Base64Util;
import com.coder520.mamabike.utils.RSAUtil;
import com.google.gson.Gson;

import java.util.Random;
import java.util.UUID;

/**
 * Created by huang on 2017/9/10.
 */

public class UserManager {
    public static UserManager sInstance;
    private String mUserToken;
    private UserVo mUserInfo;

    private UserManager() {
    }

    public void loginOut() {
        mUserToken = null;
        mUserInfo = null;
        new PreferenceDataSaver().remove(PreferenceDataSaver.PREFERENCE_KEY_USER_SESSION);
    }

    public UserVo getUserInfo() {
        return mUserInfo;
    }

    public boolean isLogin() {
        if (TextUtils.isEmpty(mUserToken)) {
            mUserToken = new PreferenceDataSaver().get(
                    PreferenceDataSaver.PREFERENCE_KEY_USER_SESSION);
        }
        return !TextUtils.isEmpty(mUserToken);
    }

    public String getUserToken() {
        return mUserToken;
    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    public void loadUserInfo(DataListener<UserVo> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(
                InternetRequestWorker.getInstance().create(UserInterface.class).getUserInfo(),
                new DefaultResponseDispatcher(dataListener) {
            @Override
            protected void handleSuccess(ResponseEntityVo data) {
                if (data.success()) {
                    mUserInfo = (UserVo) data.getData();
                }
            }
        });
    }

    public void loadWalletInfo(DataListener<WalletVo> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(
                InternetRequestWorker.getInstance().create(UserInterface.class).getWalletInfo(),
                new DefaultResponseDispatcher(dataListener)
        );
    }

    private String getOrderIdByUUId() {
        int first = new Random(10).nextInt(8) + 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return first + String.format("%015d", hashCodeV);
    }

    public void sendMessageVerifyCode(String number, DataListener<Object> dataListener) {
        UserTo userTo = new UserTo();
        userTo.setMobile(number);
        InternetRequestWorker.getInstance().asyncNetwork(InternetRequestWorker.getInstance()
                .create(UserInterface.class).sendVerCode(userTo),
                new DefaultResponseDispatcher(dataListener));
    }

    public void doLogin(final LoginDataTo loginDataTo, DataListener<String> dataListener) {
        String key = getOrderIdByUUId();
        String data = new Gson().toJson(loginDataTo);
        try {
            byte[] keyrsa = RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"),
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuVRY8B3+Af5euC9WbgNkJKAiBzqOvrYi9mSST78jd4clpn7vkYHDfHzJiqFz9wjNRLzg9MUREF53bw9yhSljZ7F8JPMryfe8RR2Ed6CJq5nCy/2hvTTw4L6ypDemwe9f9yjIg52oPRPwU8lm8Uj3wKhjedDmZrkO1TAmt3sbQtwIDAQAB");
            String base = Base64Util.encode(keyrsa);
            InternetRequestWorker worker = InternetRequestWorker.getInstance();
            worker.asyncNetwork(worker.create(UserInterface.class)
                    .doLogin(new LoginTo(AESUtil.encrypt(data, key), base)), new DefaultResponseDispatcher(dataListener) {
                @Override
                protected void handleSuccess(ResponseEntityVo data) {
                    if (data.success()) {
                        mUserToken = (String) data.getData();
                        new PreferenceDataSaver().putString(
                                PreferenceDataSaver.PREFERENCE_KEY_USER_SESSION, mUserToken);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
