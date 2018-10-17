package com.coder520.mamabike.entity.to;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class UserTo {
    private String enableFlag;
    private String headImg;
    private int id;
    private String mobile;
    private String nickname;
    private String verifyFlag;

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(String verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public UserTo() {
    }

    public UserTo(String enableFlag, String headImg, int id,
                  String mobile, String nickname, String verifyFlag) {
        this.enableFlag = enableFlag;
        this.headImg = headImg;
        this.id = id;
        this.mobile = mobile;
        this.nickname = nickname;
        this.verifyFlag = verifyFlag;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "enableFlag='" + enableFlag + '\'' +
                ", headImg='" + headImg + '\'' +
                ", id=" + id +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", verifyFlag='" + verifyFlag + '\'' +
                '}';
    }
}
