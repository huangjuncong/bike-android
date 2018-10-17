package com.coder520.mamabike.entity.vo;

import java.util.List;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class RideRecordRodeVo {

    private String bikeNo;
    private List<Point> contrail;
    private String rideRecordNo;

    public RideRecordRodeVo(String bikeNo, List<Point> contrail, String rideRecordNo) {
        this.bikeNo = bikeNo;
        this.contrail = contrail;
        this.rideRecordNo = rideRecordNo;
    }

    public String getBikeNo() {
        return bikeNo;
    }

    public void setBikeNo(String bikeNo) {
        this.bikeNo = bikeNo;
    }

    public List<Point> getContrail() {
        return contrail;
    }

    public void setContrail(List<Point> contrail) {
        this.contrail = contrail;
    }

    public String getRideRecordNo() {
        return rideRecordNo;
    }

    public void setRideRecordNo(String rideRecordNo) {
        this.rideRecordNo = rideRecordNo;
    }

    @Override
    public String toString() {
        return "RideRecordRodeVo{" +
                "bikeNo='" + bikeNo + '\'' +
                ", contrail=" + contrail +
                ", rideRecordNo='" + rideRecordNo + '\'' +
                '}';
    }
}
