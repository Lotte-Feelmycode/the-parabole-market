package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponDto {

    private Long couponId;
    private String couponDetail;
    private Integer couponDiscountValue;
    private LocalDateTime expiresAt;

    public CouponDto(Long couponId, String couponDetail, Integer couponDiscountValue,
        LocalDateTime expiresAt) {
        this.couponId = couponId;
        this.couponDetail = couponDetail;
        this.couponDiscountValue = couponDiscountValue;
        this.expiresAt = expiresAt;
    }
}
