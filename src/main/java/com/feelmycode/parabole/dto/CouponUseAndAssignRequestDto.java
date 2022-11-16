package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponUseAndAssignRequestDto {

    private String couponSNo;

    public CouponUseAndAssignRequestDto(String couponSNo) {
        this.couponSNo = couponSNo;
    }

}
