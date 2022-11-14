package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponRequestDto {

    private Long sellerId;
    private Integer totalFee;

    public CouponRequestDto(Long sellerId, Integer totalFee) {
        this.sellerId = sellerId;
        this.totalFee = totalFee;
    }

}
