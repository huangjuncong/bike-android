package com.coder520.mamabike.entity.to;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class LoginTo {
    private String data;
    private String key;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LoginTo(String data, String key) {
        this.data = data;
        this.key = key;
    }

    @Override
    public String toString() {
        return "LoginTo{" +
                "data='" + data + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
