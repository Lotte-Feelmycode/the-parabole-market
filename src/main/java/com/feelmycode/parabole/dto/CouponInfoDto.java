package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponInfoDto {

    private String couponName;
    private String storeName;
    private String type;
    private Integer discountValue;

    public CouponInfoDto(String couponName, String storeName, String type, Integer discountValue) {
        this.couponName = couponName;
        this.storeName = storeName;
        this.type = type;
        this.discountValue = discountValue;
    }

}
