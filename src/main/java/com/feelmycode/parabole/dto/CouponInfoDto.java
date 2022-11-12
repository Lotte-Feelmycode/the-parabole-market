package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponInfoDto {

    private String couponName;
    private String serialNo;
    private String storeName;
    private String type;
    private Integer discountValue;
    private Integer totalFee;

    public CouponInfoDto(String couponName, String serialNo, String storeName, String type, Integer discountValue, Integer totalFee) {
        this.couponName = couponName;
        this.serialNo = serialNo;
        this.storeName = storeName;
        this.type = type;
        this.discountValue = discountValue;
        this.totalFee = totalFee;
    }

}
