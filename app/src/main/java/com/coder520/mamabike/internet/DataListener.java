package com.coder520.mamabike.internet;

/**
 * Created by zhongyanli on 17/8/20.
 */

public interface DataListener<T> {
    void onSuccess(T data, String message);
    void onFailed(Throwable error, String message);
    void onEnd();
}
