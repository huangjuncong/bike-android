package com.coder520.mamabike.internet;

import android.util.Log;

import com.coder520.mamabike.AppConfig;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yadong on 2017/8/4.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = JsonResponseBodyConverter.class.getSimpleName();
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;
    private boolean thiredPartRequest = false;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean thiredPartRequest) {
        this.mGson = gson;
        this.adapter = adapter;
        this.thiredPartRequest = thiredPartRequest;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        if (thiredPartRequest) {
            JsonReader jsonReader = this.mGson.newJsonReader(responseBody.charStream());
            Object var3;
            try {
                var3 = this.adapter.read(jsonReader);
            } finally {
                responseBody.close();
            }
            return (T) var3;
        }
        //response string
        String response = responseBody.string();
        String result = null;
        try {
//            result = Aes.decText(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object var3;
        try {
            var3 = this.adapter.fromJson(result);
        } finally {
            responseBody.close();
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "convert: decoded data from server is:" + response);
        }
        return (T) var3;
    }

}
