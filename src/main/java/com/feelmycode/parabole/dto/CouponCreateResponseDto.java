package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponCreateResponseDto {

    private String couponName;
    private String sellerStorename;
    private String type;
    private Integer cnt;

    public CouponCreateResponseDto(String couponName, String sellerStorename, String type,
        Integer cnt) {
        this.couponName = couponName;
        this.sellerStorename = sellerStorename;
        this.type = type;
        this.cnt = cnt;
    }
}
