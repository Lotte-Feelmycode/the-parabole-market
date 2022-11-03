package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponInfoDto {

    private String couponName;
    private String serialNo;
    private String storeName;
    private String type;
    private Integer discountValue;

    public CouponInfoDto(String couponName, String serialNo, String storeName, String type, Integer discountValue) {
        this.couponName = couponName;
        this.serialNo = serialNo;
        this.storeName = storeName;
        this.type = type;
        this.discountValue = discountValue;
    }

}
