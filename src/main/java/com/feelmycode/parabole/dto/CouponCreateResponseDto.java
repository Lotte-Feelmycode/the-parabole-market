package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateResponseDto {

    private Long couponId;
    private String couponName;
    private String sellerStorename;
    private String type;
    private Integer cnt;

    public CouponCreateResponseDto(Long couponId, String couponName, String sellerStorename, String type,
        Integer cnt) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.sellerStorename = sellerStorename;
        this.type = type;
        this.cnt = cnt;
    }
}
