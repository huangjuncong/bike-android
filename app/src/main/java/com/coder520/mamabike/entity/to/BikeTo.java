package com.coder520.mamabike.entity.to;

/**
 * Created by zhongyanli on 17/9/2.
 */

public class BikeTo {
    private String enableFlag;
    private int id;
    private long number;
    private int type; /*1 light, 0 normal*/

    public BikeTo() {
    }

    public BikeTo(String enableFlag, int id, long number, int type) {
        this.enableFlag = enableFlag;
        this.id = id;
        this.number = number;
        this.type = type;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BikeTo{" +
                "enableFlag='" + enableFlag + '\'' +
                ", id=" + id +
                ", number=" + number +
                ", type='" + type + '\'' +
                '}';
    }
}
