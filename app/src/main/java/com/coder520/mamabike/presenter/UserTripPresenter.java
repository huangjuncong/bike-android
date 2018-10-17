package com.coder520.mamabike.presenter;

import android.content.Intent;

import com.coder520.mamabike.entity.vo.RideRecordItem;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.manager.RideRecordManager;
import com.coder520.mamabike.ui.UiControlInterface;
import com.coder520.mamabike.ui.activity.UserTripActivity;

import java.util.List;

/**
 * Created by huang on 2017/9/17.
 */

public class UserTripPresenter extends BasePresenter {
    private UserTripActivity mActivity;
    public UserTripPresenter(UserTripActivity uiControlInterface) {
        super(uiControlInterface);
        mActivity = uiControlInterface;
    }

    @Override
    public void start(Intent intent) {
        RideRecordManager.getInstance().loadRecordList(0L, new DataListener<List<RideRecordItem>>() {
            @Override
            public void onSuccess(List<RideRecordItem> data, String message) {
                mActivity.setTripDatas(data);
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
}
