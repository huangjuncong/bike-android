package com.coder520.mamabike;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.coder520.mamabike.internet.ImageDownloader;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.squareup.picasso.Picasso;


public class MamaApplication extends Application {
    private static MamaApplication sInstance;

    public static MamaApplication getApplication() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        sInstance = this;

        InternetRequestWorker.getInstance().init(this);

        //初始化picasso
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new ImageDownloader())
                .build();
        Picasso.setSingletonInstance(picasso);
    }
}