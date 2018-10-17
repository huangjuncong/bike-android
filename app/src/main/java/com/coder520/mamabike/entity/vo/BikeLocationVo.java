package com.coder520.mamabike.entity.vo;

import com.coder520.mamabike.entity.to.BikeTo;

import java.util.Arrays;

/**
 * Created by huang on 2017/9/10.
 */

public class BikeLocationVo {
    private String id;
    private Long bikeNumber;
    private int status;
    private Double[] coordinates;
    private Double distance;
    private BikeTo bikeInfo;

    public BikeLocationVo() {
    }

    public BikeLocationVo(String id, Long bikeNumber, int status, Double[] coordinates,
                          Double distance, BikeTo bikeInfo) {
        this.id = id;
        this.bikeNumber = bikeNumber;
        this.status = status;
        this.coordinates = coordinates;
        this.distance = distance;
        this.bikeInfo  = bikeInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(Long bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public BikeTo getBikeInfo() {
        return bikeInfo;
    }

    public void setBikeInfo(BikeTo bikeInfo) {
        this.bikeInfo = bikeInfo;
    }

    @Override
    public String toString() {
        return "BikeLocationVo{" +
                "id='" + id + '\'' +
                ", bikeNumber=" + bikeNumber +
                ", status=" + status +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", distance=" + distance +
                '}';
    }
}
