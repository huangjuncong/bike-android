package com.coder520.mamabike.entity.vo;

/**
 * Created by huang on 2017/9/20.
 */

public class UserVo {
    private Long id;
    private String nickname;
    private String mobile;
    private String headImg;
    private Byte verifyFlag;
    private Byte enableFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Byte getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(Byte verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public Byte getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Byte enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", headImg='" + headImg + '\'' +
                ", verifyFlag=" + verifyFlag +
                ", enableFlag=" + enableFlag +
                '}';
    }
}
