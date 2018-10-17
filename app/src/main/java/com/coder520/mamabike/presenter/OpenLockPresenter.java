package com.coder520.mamabike.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.to.BikeTo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.manager.BikeManager;
import com.coder520.mamabike.ui.UiControlInterface;

/**
 * Created by huang on 2017/9/14.
 */

public class OpenLockPresenter extends BasePresenter {
    private static final int TIME_DELAY_TO_QUERY = 5 * 1000;
    private static final int MSG_GET_STATUS = 1;
    private int mTryGetStatusTimes = 0;

    public OpenLockPresenter(UiControlInterface uiControlInterface) {
        super(uiControlInterface);
    }

    public void openLock(String numberStr) {
        if(TextUtils.isEmpty(numberStr) || numberStr.length() != 8) {
            getUiControlInterface().showToast(getString(R.string.input_right_bike_number));
            return;
        }
        Long bikeNumber;
        try {
            bikeNumber = Long.valueOf(numberStr);
        }catch (NumberFormatException e) {
            getUiControlInterface().showToast(getString(R.string.input_right_bike_number));
            return;
        }
        getUiControlInterface().showProgress(getString(R.string.unolocking));
        BikeTo bike = new BikeTo();
        bike.setNumber(bikeNumber);
        BikeManager.getInstance().doUnLockBike(bike, new DataListener<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                sendQueryMessageDelay();
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

    private void sendQueryMessageDelay() {
        if(mTryGetStatusTimes < 10) {
            Message message = mHandler.obtainMessage(MSG_GET_STATUS);
            mTryGetStatusTimes++;
            mHandler.sendMessageDelayed(message, TIME_DELAY_TO_QUERY);
        }
        //TODO 解锁状态未知..
    }

    private void doQueryBikeStatus() {
        Long bikeNumber = BikeManager.getInstance().getCurrentActiveBikeNumber();
        if (bikeNumber == 0L) {
            return;
        }
        BikeTo bike = new BikeTo();
        bike.setNumber(bikeNumber);
        BikeManager.getInstance().queryBikeStatus(bike, new DataListener<Integer>() {
            @Override
            public void onSuccess(Integer data, String message) {
                //TODO 解锁成功
                if(data == 2) {
                    getUiControlInterface().showToast(getString(R.string.unlock_success));
                    getUiControlInterface().finish();
                } else {
                    //解锁未成功
                    sendQueryMessageDelay();
                }
            }

            @Override
            public void onFailed(Throwable error, String message) {
                //查询状态失败
                sendQueryMessageDelay();
            }

            @Override
            public void onEnd() {
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_STATUS:
                    doQueryBikeStatus();
                    break;
                default:
                    break;
            }
        }
    };
}
