package com.coder520.mamabike.internet;

import android.net.Uri;

import com.coder520.mamabike.AppConfig;
import com.coder520.mamabike.MamaApplication;
import com.coder520.mamabike.internet.net.FileInterface;
import com.squareup.picasso.Downloader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 基于okhttp3 的picasso图片下载器
 * Created by yadong on 2017/6/6.
 */

public class ImageDownloader implements Downloader {
    private static final int INTERNET_CACHE_SIZE = 10 * 1024 * 1024; //10M
    private Retrofit mRetrofit = null;

    public ImageDownloader() {
        init();
    }

    public void init() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Cache cache = new Cache(MamaApplication.getApplication().getExternalCacheDir(),
                INTERNET_CACHE_SIZE);
        httpClient.cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.API_BASE_URL)
                .client(httpClient.build())
                .build();
    }

    @Override
    public Response load(Uri uri, int networkPolicy) throws IOException {
        FileInterface fileDownload = mRetrofit.create(FileInterface.class);
        Call<ResponseBody> requestCall = fileDownload.getImage(uri.toString());
        Call<ResponseBody> call;
        if (requestCall.isExecuted()) {
            call = requestCall.clone();
        } else {
            call = requestCall;
        }
        retrofit2.Response<ResponseBody> response = call.execute();
        return new Response(response.body().byteStream(), false, response.body().contentLength());
    }

    @Override
    public void shutdown() {
    }
}
