package com.coder520.mamabike.internet;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.Headers;

public class JsonConverterFactory extends Converter.Factory {

    public static JsonConverterFactory create() {
        return create(new Gson());
    }

    public static JsonConverterFactory create(Gson gson) {
        return new JsonConverterFactory(gson);

    }

    private final Gson gson;

    private JsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {

        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new JsonResponseBodyConverter<>(gson, adapter, isThiredPartRequest(annotations)); //响应
    }

    private boolean isThiredPartRequest(Annotation [] methodAnnotations) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof Headers) {
                    for (String str : ((Headers) annotation).value()) {
                        if (str.contains(InternetRequestWorker.MARK_AS_THIRED_REQUEST)) {
                            try {
                                if ("true".equals(str.substring(str.lastIndexOf(":") + 1).trim())) {
                                    return true;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }
        return false;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new JsonRequestBodyConverter<>(gson, adapter, isThiredPartRequest(methodAnnotations)); //请求
    }
}
