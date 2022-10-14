package com.feelmycode.parabole.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CouponInfoResponseDto {

    private String couponType;            // 쿠폰유형 ( 할인율1  할인금액2 )
    private Integer discountValue;          // 유형1이면 할인율, 유형2면 할인액수

    public CouponInfoResponseDto(String couponType, Integer discountValue) {
        this.couponType = couponType;
        this.discountValue = discountValue;
    }

}
