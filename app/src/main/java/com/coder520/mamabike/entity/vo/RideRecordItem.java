package com.coder520.mamabike.entity.vo;

/**
 * Created by zhongyanli on 17/9/2.
 */

    public class RideRecordItem {
        private Integer bikeNo;
        private String endTime;
        private Integer id;
        private String recordNo;
        private Float rideCost;
        private Integer rideTime;
        private String startTime;
        private String status;
        private Integer userid;

        public RideRecordItem(Integer bikeNo, String endTime, Integer id, String recordNo,
                              Float rideCost, Integer rideTime, String startTime,
                              String status, Integer userid) {
            this.bikeNo = bikeNo;
            this.endTime = endTime;
            this.id = id;
            this.recordNo = recordNo;
            this.rideCost = rideCost;
            this.rideTime = rideTime;
            this.startTime = startTime;
            this.status = status;
            this.userid = userid;
        }

        public Integer getBikeNo() {
            return bikeNo;
        }

        public void setBikeNo(Integer bikeNo) {
            this.bikeNo = bikeNo;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public Float getRideCost() {
            return rideCost;
        }

        public void setRideCost(Float rideCost) {
            this.rideCost = rideCost;
        }

        public Integer getRideTime() {
            return rideTime;
        }

        public void setRideTime(Integer rideTime) {
            this.rideTime = rideTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        @Override
        public String toString() {
            return "RideRecordItem{" +
                    "bikeNo=" + bikeNo +
                    ", endTime='" + endTime + '\'' +
                    ", id=" + id +
                    ", recordNo='" + recordNo + '\'' +
                    ", rideCost=" + rideCost +
                    ", rideTime=" + rideTime +
                    ", startTime='" + startTime + '\'' +
                    ", status='" + status + '\'' +
                    ", userid=" + userid +
                    '}';
        }
    }
