package com.coder520.mamabike.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.RideRecordRodeVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.manager.RideRecordManager;
import com.coder520.mamabike.ui.UiControlInterface;
import com.coder520.mamabike.ui.activity.RodeRouteActivity;

import java.util.List;

/**
 * Created by huang on 2017/9/24.
 */

public class RodeRoutePresneter extends BasePresenter {
    public static final String EXTRA_ROUTE_ID = "route_id";
    private RodeRouteActivity mActivity;
    public RodeRoutePresneter(UiControlInterface uiControlInterface) {
        super(uiControlInterface);
        mActivity = (RodeRouteActivity) uiControlInterface;
    }

    @Override
    public void start(Intent intent) {
        String recordNum = intent.getStringExtra(EXTRA_ROUTE_ID);
        if (TextUtils.isEmpty(recordNum)) {
            getUiControlInterface().showToast(getString(R.string.error_route));
            return;
        }
        getUiControlInterface().showProgress(getString(R.string.loading));
        RideRecordManager.getInstance().loadRouteDetail(recordNum,
                new DataListener<RideRecordRodeVo>() {
            @Override
            public void onSuccess(RideRecordRodeVo data, String message) {
                mActivity.starRouteSearch(data.getContrail());
            }

            @Override
            public void onFailed(Throwable error, String message) {
                getUiControlInterface().showToast(message);
            }

            @Override
            public void onEnd() {
                getUiControlInterface().dismissProgress();
            }
        });
    }
}
