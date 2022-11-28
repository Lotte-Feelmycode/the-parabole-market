package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponWithSellerStoreDto {

    private Long couponId;
    private String couponName;
    private String couponType;
    private String couponDetail;
    private String sellerStoreName;
    private Integer couponDiscountValue;
    private LocalDateTime expiresAt;

    public CouponWithSellerStoreDto(Long couponId, String couponName, String couponType, String couponDetail, Integer couponDiscountValue,
        LocalDateTime expiresAt) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.couponDetail = couponDetail;
        this.couponDiscountValue = couponDiscountValue;
        this.expiresAt = expiresAt;
    }
}
