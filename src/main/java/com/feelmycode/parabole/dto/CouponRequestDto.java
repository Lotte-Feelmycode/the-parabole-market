package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponRequestDto {

    private Long sellerId;
    private Long totalFee;

    public CouponRequestDto(Long sellerId, Long totalFee) {
        this.sellerId = sellerId;
        this.totalFee = totalFee;
    }

}
