package com.coder520.mamabike.service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.coder520.mamabike.entity.vo.Point;
import com.coder520.mamabike.manager.BikeManager;
import com.coder520.mamabike.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/8/9.
 */

public class LocationService extends Service {
    //标识多远的距离改变进行位置更新
    private static final int DISTANCE_TO_CHANGE = 2;

    public LocationClient mLocationClient = null;
    public BDLocationListener mDBLocationListener = new MyDBLocationListener();
    private BDLocation mLastLocation = null;
    private MyBinder mBinder;
    private List<LocationChangeListener> mListener = new ArrayList<>();
    private Object mLock = new Object();

    public interface LocationChangeListener {
        void onLocationChange(BDLocation point);
    }

    @Override
    public void onCreate() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(mDBLocationListener);
        mBinder = new MyBinder();
        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    public void addLocationChangeListener(LocationChangeListener listener) {
        synchronized (mLock) {
            mListener.add(listener);
        }
    }

    public Point getCurrentLocation(boolean forceLoad) {
        if (forceLoad) {
            startLocation();
        }
        if (mLastLocation == null) {
            return null;
        }
        notifyListener(mLastLocation);
        return new Point(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }

    public boolean isLocationClientStarted() {
        return mLocationClient.isStarted();
    }

    public void stopLocation(){
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public void startLocation() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        //option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        //option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        //option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private class MyDBLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeServerError) {
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                return;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //当前网络不通
                return;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                return;
            }
            Point out = null;
            if (mLastLocation != null) {
                double distance = DistanceUtil.getDistance(
                        new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                        new LatLng(location.getLatitude(), location.getLongitude()));
                if (distance < DISTANCE_TO_CHANGE) {
                     return;
                }
            }
            mLastLocation = location;
            notifyListener(location);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    private void notifyListener(BDLocation point) {
        synchronized (mLock) {
            for (LocationChangeListener listener : mListener) {
                listener.onLocationChange(point);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

}
