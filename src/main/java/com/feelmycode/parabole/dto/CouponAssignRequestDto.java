package com.feelmycode.parabole.dto;

import lombok.Getter;

@Getter
public class CouponAssignRequestDto {

    private Long couponId;
    private String couponSerialNo;
    private Long sellerId;
    private Long userId;
}
