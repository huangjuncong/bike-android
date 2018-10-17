package com.coder520.mamabike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.to.BikeTo;
import com.coder520.mamabike.entity.vo.BikeLocationVo;
import com.coder520.mamabike.entity.vo.Point;
import com.coder520.mamabike.presenter.MainPresenter;
import com.coder520.mamabike.ui.dialog.ServiceDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> {
    @BindView(R.id.btn_message)
    ImageView btnMessage;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.container_click_to_scan)
    LinearLayout containerClickToScan;
    @BindView(R.id.btn_location)
    ImageView btnLocation;
    @BindView(R.id.btn_refresh)
    ImageView btnRefresh;
    @BindView(R.id.btn_service)
    ImageView btnService;
    @BindView(R.id.btn_red_pacakge)
    ImageView btnRedPacakge;
    @BindView(R.id.mapview_main)
    MapView mapviewMain;
    BaiduMap mBaiduMap;
    @BindView(R.id.container_in_rode)
    RelativeLayout containerInRode;
    @BindView(R.id.text_rode_time)
    TextView textRodeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configView();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setInRode(final boolean inRode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                containerInRode.setVisibility(inRode ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void setTextRodeTime(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textRodeTime.setText(time);
            }
        });
    }

    private void configView() {
        mapviewMain.showZoomControls(false);
        mapviewMain.showScaleControl(false);
        mBaiduMap = mapviewMain.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //地图状态改变相关接口
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
                // 手势操作地图，设置地图状态等操作导致地图状态开始改变。
                clearAllBikeMark();
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                mPresenter.loadBikeAround(null);
            }
        });
    }

    public void startDataLoadAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final RotateAnimation animation = new RotateAnimation(0f, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setRepeatCount(-1);
                btnRefresh.startAnimation(animation);
            }
        });

    }

    public LatLng getCenterInfo() {
        return mBaiduMap.getMapStatus().target;
    }

    public void cancelLoadAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRefresh.getAnimation().cancel();
            }
        });
    }

    public void clearAllBikeMark() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Marker marker : mBikeMarks) {
                    marker.remove();
                }
                mBikeMarks.clear();
            }
        });
    }

    @UiThread
    public void moveMap(final BDLocation location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                //设定中心点坐标
                LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude());
                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(cenpt)
                        .zoom(16)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.setMapStatus(mMapStatusUpdate);
            }
        });
    }

    private List<Marker> mBikeMarks = new ArrayList<>(100);

    /**
     * 在地图上添加车辆信息
     *
     * @param bikeLocationVo
     */
    @UiThread
    public void addBikeMark(final BikeLocationVo bikeLocationVo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Point position = new Point(bikeLocationVo.getCoordinates()[1],
                        bikeLocationVo.getCoordinates()[0]);
                if (mBikeMarks.size() > 100) {
                    return;
                }
                LatLng point = new LatLng(position.getLatitude(), position.getLongitude());
                int iconResId = R.drawable.ic_location_1;
                BikeTo bikeInfo = bikeLocationVo.getBikeInfo();
                if (bikeInfo != null && bikeInfo.getType() == 1) {
                    iconResId = R.drawable.ic_location_2;
                }
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(iconResId);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmapDescriptor);
                mBikeMarks.add((Marker) mBaiduMap.addOverlay(option));
            }
        });
    }

    @OnClick(R.id.btn_refresh)
    public void onLoadBikeClick() {
        mPresenter.loadBikeAround(null);
    }

    @OnClick(R.id.btn_service)
    public void onServiceClicked() {
        ServiceDialog.createDailog(this).show();
    }

    @OnClick(R.id.btn_location)
    public void onLocationClick() {
        mPresenter.startLocation();
    }

    @OnClick(R.id.btn_user_settings)
    public void onUserSettingsClick() {
        Intent intent = new Intent(this, UserSettingActivity.class);
        launchActivity(true, intent);
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @OnClick(R.id.container_click_to_scan)
    public void onScanToUnlockBikeClick() {
        Intent intent = new Intent(this, OpenLockActivity.class);
        launchActivity(true, intent);
    }

    @OnClick(R.id.btn_end_ride)
    public void onEndRideClick() {
        mPresenter.doEndRide();
    }
}
