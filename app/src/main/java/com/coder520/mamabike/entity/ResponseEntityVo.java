package com.coder520.mamabike.entity;

/**
 * Created by zhongyanli on 17/8/20.
 */

public class ResponseEntityVo<T> {
    public static final int SUCCESS = 200;

    private T data;
    private String message;
    private int code;

    public boolean success() {
        return code == SUCCESS;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
