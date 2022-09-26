package com.feelmycode.parabole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter // dto 에 getter 안붙여주면 406 Not Acceptable 납니다
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
