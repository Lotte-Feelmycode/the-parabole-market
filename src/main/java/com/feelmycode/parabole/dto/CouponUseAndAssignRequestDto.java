package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponUseAndAssignRequestDto {

    private String couponSNo;

    public CouponUseAndAssignRequestDto(String couponSNo) {
        this.couponSNo = couponSNo;
    }

}
