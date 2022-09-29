package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponCreateResponseDto {

    private String couponName;
    private String sellerName;
    private Integer type;
    private Integer cnt;

    public CouponCreateResponseDto(String couponName, String sellerName, Integer type,
        Integer cnt) {
        this.couponName = couponName;
        this.sellerName = sellerName;
        this.type = type;
        this.cnt = cnt;
    }
}
