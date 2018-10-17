package com.coder520.mamabike.entity.to;

import java.util.List;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class LocationReportTo {
    private int bikeNumber;
    private List<Float> coordinates;
    private float distance;
    private String id;
    private int status;

    public LocationReportTo(int bikeNumber, List<Float> coordinates, float distance, String id,
                            int status) {
        this.bikeNumber = bikeNumber;
        this.coordinates = coordinates;
        this.distance = distance;
        this.id = id;
        this.status = status;
    }

    public int getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(int bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LocationReportTo{" +
                "bikeNumber=" + bikeNumber +
                ", coordinates=" + coordinates +
                ", distance=" + distance +
                ", id='" + id + '\'' +
                ", status=" + status +
                '}';
    }
}
