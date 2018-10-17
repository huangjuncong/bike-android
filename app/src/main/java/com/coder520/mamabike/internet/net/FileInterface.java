package com.coder520.mamabike.internet.net;

import com.coder520.mamabike.internet.InternetRequestWorker;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by yadong on 2017/6/6.
 */

public interface FileInterface {
    @Headers({"Cache-Control: public, max-age=864000, no-cache",
            InternetRequestWorker.MARK_AS_THIRED_REQUEST})
    @GET
    Call<ResponseBody> getImage(@Url String url);

}
