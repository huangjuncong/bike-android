package com.coder520.mamabike.entity.vo;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class Point {
    private double latitude;
    private double longitude;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "RecordPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}