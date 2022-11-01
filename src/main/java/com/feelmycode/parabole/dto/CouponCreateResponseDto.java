package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponCreateResponseDto {

    private Long couponId;
    private String couponName;
    private String sellerName;
    private String type;
    private Integer cnt;

    public CouponCreateResponseDto(Long couponId, String couponName, String sellerName, String type,
        Integer cnt) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.sellerName = sellerName;
        this.type = type;
        this.cnt = cnt;
    }
}
