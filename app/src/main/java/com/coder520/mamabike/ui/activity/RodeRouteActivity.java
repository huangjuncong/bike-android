package com.coder520.mamabike.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.Point;
import com.coder520.mamabike.presenter.RodeRoutePresneter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mapapi.overlayutil.DrivingRouteOverlay;
import mapapi.overlayutil.OverlayManager;

/**
 * Created by huang on 2017/9/24.
 */

public class RodeRouteActivity extends BaseActivity<RodeRoutePresneter> {
    @BindView(R.id.mapview_main)
    MapView mapviewMain;
    BaiduMap mBaiduMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_route);
        ButterKnife.bind(this);
        mBaiduMap = mapviewMain.getMap();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void starRouteSearch(List<Point> points) {
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(getRoutePlanResultListener);
        //设置途径点
        List<PlanNode> planNode_list = new ArrayList<PlanNode>();
        if (points.size() > 2) {
            for (int i = 1; i < points.size(); i++) {
                Point point = points.get(i);
                PlanNode passby = PlanNode.withLocation(pointToLatlng(point));//途径点
                planNode_list.add(passby);
            }
        }

        // 设置起终点信息
        Point start = points.get(0);
        Point end = points.get(points.size() - 1);
        PlanNode stNode = PlanNode.withLocation(pointToLatlng(start));
        PlanNode enNode = PlanNode.withLocation(pointToLatlng(end));
        mSearch.drivingSearch(
                new DrivingRoutePlanOption()
                        .from(stNode)
                        .to(enNode)
                        .passBy(planNode_list));
    }

    private LatLng pointToLatlng(Point point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    private OnGetRoutePlanResultListener
            getRoutePlanResultListener =
            new OnGetRoutePlanResultListener() {

                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        showToast("抱歉，未找到结果");
                    }
                    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        if (result.getRouteLines().size() >= 1) {
                            RouteLine route = result.getRouteLines().get(0);
                            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                            OverlayManager routeOverlay = overlay;
                            mBaiduMap.setOnMarkerClickListener(overlay);
                            overlay.setData(result.getRouteLines().get(0));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        } else {
                            Log.d("route result", "结果数<0");
                            return;
                        }
                    }
                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult result) {
                }
            };

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

    @Override
    protected RodeRoutePresneter createPresenter() {
        return new RodeRoutePresneter(this);
    }

    @Override
    protected String getActionTitle() {
        return getString(R.string.ride_route);
    }
}
