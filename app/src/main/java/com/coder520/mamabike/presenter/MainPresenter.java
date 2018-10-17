package com.coder520.mamabike.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.BikeLocationVo;
import com.coder520.mamabike.entity.vo.Point;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.manager.BikeManager;
import com.coder520.mamabike.service.LocationService;
import com.coder520.mamabike.service.PositionUploadService;
import com.coder520.mamabike.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huang on 2017/9/10.
 */

public class MainPresenter extends BasePresenter
        implements LocationService.LocationChangeListener {
    private MainActivity mActivity;
    private Timer mTimer;
    private LocationService mLocationService;

    public MainPresenter(MainActivity uiControlInterface) {
        super(uiControlInterface);
        mActivity = uiControlInterface;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(mContext, LocationService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationService = ((LocationService.MyBinder)service).getService();
            mLocationService.addLocationChangeListener(MainPresenter.this);
            mLocationService.getCurrentLocation(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocationService = null;
        }
    };

    @Override
    public void onDestroy() {
        mContext.unbindService(mServiceConnection);
    }

    @Override
    public void start(Intent intent) {
        super.start(intent);
        loadRideState();
    }

    @Override
    public void stop() {
        super.stop();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void doEndRide() {
        getUiControlInterface().showProgress(getString(R.string.end_ride));
        BikeLocationVo bikeLocationVo = new BikeLocationVo();
        if (mLocationService != null) {
            Point lastPoint = mLocationService.getCurrentLocation(false);
            if (lastPoint != null) {
                bikeLocationVo.setCoordinates(new Double[]{lastPoint.getLongitude(),
                        lastPoint.getLatitude()});
            }
        }
        bikeLocationVo.setBikeNumber(BikeManager.getInstance().getCurrentActiveBikeNumber());
        BikeManager.getInstance().doLockBike(bikeLocationVo, new DataListener<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
                getUiControlInterface().showToast(getString(R.string.lock_success));
                loadRideState();
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

    private void startPostionUploadService() {
        Intent intent = new Intent(mContext, PositionUploadService.class);
        mContext.startService(intent);
    }

    private void loadRideState() {
        boolean inRode = BikeManager.getInstance().isRide();
        mActivity.setInRode(inRode);
        startPostionUploadService();
        if (inRode) {
            final Long rodeStart = BikeManager.getInstance().getRideStartTime();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Long diff = System.currentTimeMillis() - rodeStart;
                    diff = diff / 1000;
                    long min = diff / 60;
                    long second = diff % 60;
                    StringBuilder timeShowing = new StringBuilder();
                    if (min != 0) {
                        timeShowing.append(min);
                        timeShowing.append(getString(R.string.min));
                    }
                    timeShowing.append(second);
                    timeShowing.append(getString(R.string.second));
                    mActivity.setTextRodeTime(timeShowing.toString());
                }
            }, 1 * 1000,  1 * 1000);
        } else {
            if (mTimer != null) {
                mTimer.cancel();
            }
        }
    }

    /**
     * 载入当前位置信息周围的车并显示在地图上
     */
    public void loadBikeAround(BDLocation location) {
        mActivity.startDataLoadAnimation();
        Point point;
        if (location == null) {
            //获取中心点信息并查询中心点周围的单车信息
            LatLng target = mActivity.getCenterInfo();
            point = new Point(target.latitude, target.longitude);
        } else {
            point = new Point(location.getLatitude(), location.getLongitude());
        }
        BikeManager.getInstance().getAoundBikeInfo(point, new DataListener<List<BikeLocationVo>>() {
                    @Override
                    public void onSuccess(List<BikeLocationVo> datas, String message) {
                        if (datas == null) {
                            return;
                        }
                        mActivity.clearAllBikeMark();
                        for (BikeLocationVo bikeLocation : datas) {
                            mActivity.addBikeMark(bikeLocation);
                        }
                    }

                    @Override
                    public void onFailed(Throwable error, String message) {
                        getUiControlInterface().showToast(message);
                    }

                    @Override
                    public void onEnd() {
                        mActivity.cancelLoadAnimation();
                    }
                });
    }

    public void startLocation() {
        if (mLocationService != null) {
            mLocationService.getCurrentLocation(true);
        }
    }

    //从BikeManager中返回的骑行结束通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRideEnd(BikeManager.RideEndMessage messageEvent) {
        getUiControlInterface().showToast(getString(R.string.ride_end));
        //查询骑行状态更新页面
        loadRideState();
        loadBikeAround(mLastLocation);
    }
    private BDLocation mLastLocation;

    @Override
    public void onLocationChange(BDLocation location) {
        mActivity.moveMap(location);
        //开始载入周围的自行车信息
        loadBikeAround(location);
        mLastLocation = location;
    }
}
