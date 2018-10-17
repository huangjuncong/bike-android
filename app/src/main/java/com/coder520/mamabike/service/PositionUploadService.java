package com.coder520.mamabike.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.coder520.mamabike.entity.vo.BikeLocationVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.manager.BikeManager;
import com.coder520.mamabike.presenter.MainPresenter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huang on 2017/10/4.
 */

public class PositionUploadService extends Service
        implements LocationService.LocationChangeListener{
    private LocationService mLocationService;
    private BDLocation mLastBDLocation;
    private Timer mTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mLastBDLocation != null) {
                    uploadBikeLocation(mLastBDLocation);
                }
            }
        }, 1000, 5000);
    }

    private void uploadBikeLocation(BDLocation location) {
        BikeLocationVo bikeLocationVo = new BikeLocationVo();
        //当前已经停止骑行
        if (!BikeManager.getInstance().isRide()) {
            stopSelf();
            return;
        }
        bikeLocationVo.setBikeNumber(BikeManager.getInstance().getCurrentActiveBikeNumber());
        bikeLocationVo.setCoordinates(new Double[] {location.getLongitude(),
                location.getLatitude()});
        BikeManager.getInstance().uploadLocation(bikeLocationVo, new DataListener<Object>() {
            @Override
            public void onSuccess(Object data, String message) {
            }

            @Override
            public void onFailed(Throwable error, String message) {
            }

            @Override
            public void onEnd() {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationService = ((LocationService.MyBinder)service).getService();
            mLocationService.addLocationChangeListener(PositionUploadService.this);
            mLocationService.getCurrentLocation(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocationService = null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChange(BDLocation point) {
        mLastBDLocation = point;
    }
}
