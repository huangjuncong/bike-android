package com.coder520.mamabike.entity.to;

import com.coder520.mamabike.utils.Base64;

/**
 * Created by zhongyanli on 17/8/20.
 */

public class LoginDataTo {
    private String mobile;
    private String code;
    private String plantform = "Android";
    private String channelId;

    public LoginDataTo(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
        this.channelId = Base64.encode(mobile.getBytes());
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlantform() {
        return plantform;
    }

    public void setPlantform(String plantform) {
        this.plantform = plantform;
    }

    public String getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "LoginTo{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                ", plantform='" + plantform + '\'' +
                '}';
    }
}

