package com.coder520.mamabike.internet;

import android.util.Log;

import com.coder520.mamabike.AppConfig;
import com.coder520.mamabike.utils.Aes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by yadong on 2017/8/4.
 */
public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final String TAG = JsonRequestBodyConverter.class.getSimpleName();
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private boolean thiredPartRequest = false;

    /**
     * 构造器
     */
    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean encode) {
        this.gson = gson;
        this.adapter = adapter;
        this.thiredPartRequest = encode;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        OutputStreamWriter writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = this.gson.newJsonWriter(writer);
        this.adapter.write(jsonWriter, value);
        jsonWriter.close();
        if (thiredPartRequest) {
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
        String content = buffer.readByteString().string(Charset.forName("UTF-8"));
        String encryptData = null;
        if (AppConfig.DEBUG) {
            Log.d(TAG, "convert: send data:" + content);
        }
        try {
            encryptData = Aes.encText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppConfig.DEBUG) {
            Log.d(TAG, "convert: encoded data:" + encryptData);
        }
        return RequestBody.create(MEDIA_TYPE, encryptData);
    }
}
