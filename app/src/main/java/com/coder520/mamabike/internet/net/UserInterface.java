package com.coder520.mamabike.internet.net;

import com.coder520.mamabike.entity.ResponseEntityVo;
import com.coder520.mamabike.entity.to.LoginTo;
import com.coder520.mamabike.entity.to.UserTo;
import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.entity.vo.WalletVo;
import com.coder520.mamabike.internet.InternetRequestWorker;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zhongyanli on 17/8/20.
 */

public interface UserInterface {
    @Headers(InternetRequestWorker.MARK_AS_NO_LOGIN_REQUEST)
    @POST("user/login")
    Call<ResponseEntityVo<String>> doLogin(@Body LoginTo data);
    @POST("user/modifyNickName")
    Call<ResponseEntityVo<Object>> changeUserNickName(@Body UserTo data);
    @POST("/user/sendVercode")
    Call<ResponseEntityVo<Object>> sendVerCode(@Body UserTo data);
    @Multipart
    @POST("/user/uploadHeadImg")
    Call<ResponseEntityVo<Object>> uploadHeadImg(@Part MultipartBody.Part file);
    @GET("/user/getUserInfo")
    Call<ResponseEntityVo<UserVo>> getUserInfo();
    @GET("/wallet/getUserWallet")
    Call<ResponseEntityVo<WalletVo>> getWalletInfo();
}