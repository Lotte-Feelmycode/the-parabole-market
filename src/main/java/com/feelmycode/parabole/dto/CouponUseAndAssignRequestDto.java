package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponUseAndAssignRequestDto {

    private String couponSNo;
    private Long userId;

    public CouponUseAndAssignRequestDto(String couponSNo, Long userId) {
        this.couponSNo = couponSNo;
        this.userId = userId;
    }

}