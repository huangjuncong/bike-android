package com.coder520.mamabike.manager;

import com.coder520.mamabike.datastore.PreferenceDataSaver;
import com.coder520.mamabike.entity.ResponseEntityVo;
import com.coder520.mamabike.entity.to.BikeTo;
import com.coder520.mamabike.entity.vo.BikeLocationVo;
import com.coder520.mamabike.entity.vo.Point;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.DefaultResponseDispatcher;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.internet.net.BikeControllerInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by huang on 2017/9/9.
 */

public class BikeManager {
    private static final long INVALID_BIKE_NUMBER = -1L;
    private static BikeManager sInstance;
    private BikeControllerInterface mBikeInterface;
    //当前用户正在使用的单车编号
    private Long mCurrentActiveBikeNumber = INVALID_BIKE_NUMBER;

    public Long getCurrentActiveBikeNumber() {
        return mCurrentActiveBikeNumber;
    }

    private void saveRideStartTime(long startTime) {
        new PreferenceDataSaver().putLong(PreferenceDataSaver.PREFERENCE_KEY_RODE_STARTED,
                startTime);
    }

    private void clearStartTime() {
        new PreferenceDataSaver().remove(PreferenceDataSaver.PREFERENCE_KEY_RODE_STARTED);
    }

    public Long getRideStartTime() {
        Long timeStart = new PreferenceDataSaver().getLong(
                PreferenceDataSaver.PREFERENCE_KEY_RODE_STARTED);
        if (timeStart == 0) {
            timeStart = System.currentTimeMillis();
            saveRideStartTime(timeStart);
        }
        return timeStart;
    }

    public boolean isRide() {
        return mCurrentActiveBikeNumber != INVALID_BIKE_NUMBER;
    }

    private BikeManager() {
        mBikeInterface = InternetRequestWorker.getInstance()
                .create(BikeControllerInterface.class);
    }

    public static BikeManager getInstance() {
        if (sInstance == null) {
            sInstance = new BikeManager();
        }
        return sInstance;
    }

    public void getAoundBikeInfo(Point point, DataListener<List<BikeLocationVo>> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(mBikeInterface.findAroundBike(point),
                new DefaultResponseDispatcher(dataListener));
    }

    public void doUnLockBike(final BikeTo bikeInfo, DataListener<Object> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(mBikeInterface.doUnlockBike(bikeInfo),
                new DefaultResponseDispatcher(dataListener) {
                    @Override
                    protected void handleSuccess(ResponseEntityVo data) {
                        if(data.success()) {
                            mCurrentActiveBikeNumber = bikeInfo.getNumber();
                        }
                    }
                });
    }

    public void doLockBike(final BikeLocationVo bikeLocationVo,
                           DataListener<Object> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(mBikeInterface.doLockBike(bikeLocationVo),
                new DefaultResponseDispatcher(dataListener) {
                    @Override
                    protected void handleSuccess(ResponseEntityVo data) {
                        if (data.success()) {
                            mCurrentActiveBikeNumber = INVALID_BIKE_NUMBER;
                            clearStartTime();
                        }
                    }
                });

    }

    public void queryBikeStatus(final BikeTo bikeInfo, DataListener<Integer> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(mBikeInterface.queryBikeStatus(bikeInfo),
                new DefaultResponseDispatcher(dataListener) {
                    @Override
                    protected void handleSuccess(ResponseEntityVo data) {
                        if (data.success()) {
                            if (2 == (Integer)data.getData()) {
                                saveRideStartTime(System.currentTimeMillis());
                            }
                        }
                    }
                });
    }

    public class RideEndMessage {
    }
    public void uploadLocation(BikeLocationVo bikeLocationVo, DataListener<Object> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(
                mBikeInterface.reportLocation(bikeLocationVo),
                new DefaultResponseDispatcher(dataListener) {
                    @Override
                    protected void handleSuccess(ResponseEntityVo data) {
                        //上报坐标服务器返回骑行已经结束
                        if (data.getCode() == 201) {
                            mCurrentActiveBikeNumber = INVALID_BIKE_NUMBER;
                            EventBus.getDefault().post(new RideEndMessage());
                        }
                    }
                });
    }


}
