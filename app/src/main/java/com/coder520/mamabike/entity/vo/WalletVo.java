package com.coder520.mamabike.entity.vo;

import java.math.BigDecimal;

/**
 * Created by huang on 2017/9/20.
 */

public class WalletVo {
    private Long id;
    private Long userid;
    private BigDecimal remainSum;
    private BigDecimal deposit;

    public WalletVo(Long id, Long userid, BigDecimal remainSum, BigDecimal deposit) {
        this.id = id;
        this.userid = userid;
        this.remainSum = remainSum;
        this.deposit = deposit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getRemainSum() {
        return remainSum;
    }

    public void setRemainSum(BigDecimal remainSum) {
        this.remainSum = remainSum;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "WalletVo{" +
                "id=" + id +
                ", userid=" + userid +
                ", remainSum=" + remainSum +
                ", deposit=" + deposit +
                '}';
    }
}
