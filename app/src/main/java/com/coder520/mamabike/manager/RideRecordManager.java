package com.coder520.mamabike.manager;

import com.coder520.mamabike.entity.vo.RideRecordItem;
import com.coder520.mamabike.entity.vo.RideRecordRodeVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.DefaultResponseDispatcher;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.internet.net.RideRecordInterface;

import java.net.IDN;
import java.util.List;

/**
 * Created by huang on 2017/9/17.
 */

public class RideRecordManager {
    private static RideRecordManager sInstance;
    private RideRecordInterface mRideRecordInterface;
    private RideRecordManager() {
        mRideRecordInterface = InternetRequestWorker.getInstance()
                .create(RideRecordInterface.class);
    }
    public static RideRecordManager getInstance() {
        if (sInstance == null) {
            sInstance = new RideRecordManager();
        }
        return sInstance;
    }
    public void loadRecordList(Long id, DataListener<List<RideRecordItem>> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(
                mRideRecordInterface.getRideRecordList(0L),
                new DefaultResponseDispatcher(dataListener));
    }
    public void loadRouteDetail(String recordNum, DataListener<RideRecordRodeVo> dataListener) {
        InternetRequestWorker.getInstance().asyncNetwork(
                mRideRecordInterface.getRideRecord(recordNum + ""),
                new DefaultResponseDispatcher(dataListener));
    }
}
