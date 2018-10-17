package com.coder520.mamabike.internet.net;

import com.coder520.mamabike.entity.ResponseEntityVo;
import com.coder520.mamabike.entity.to.BikeTo;
import com.coder520.mamabike.entity.to.LocationReportTo;
import com.coder520.mamabike.entity.vo.BikeLocationVo;
import com.coder520.mamabike.entity.vo.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zhongyanli on 17/9/2.
 */

public interface BikeControllerInterface {
    @POST("bike/findAroundBike")
    Call<ResponseEntityVo<List<BikeLocationVo>>> findAroundBike(@Body Point data);
    @POST("bike/reportLocation")
    Call<ResponseEntityVo<Object>> reportLocation(@Body BikeLocationVo data);
    @POST("bike/unLockBike")
    Call<ResponseEntityVo<Object>> doUnlockBike(@Body BikeTo data);
    @POST("bike/queryBikeStatus")
    Call<ResponseEntityVo<Integer>> queryBikeStatus(@Body BikeTo data);
    @POST("bike/lockBike")
    Call<ResponseEntityVo<Integer>> doLockBike(@Body BikeLocationVo data);
}
