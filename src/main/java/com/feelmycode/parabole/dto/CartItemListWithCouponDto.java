package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartItemListWithCouponDto {

    Long sellerId;
    List<CartItemDto> cartItem;
    List<CouponInfoResponseDto> couponInfo;

    public CartItemListWithCouponDto(Long sellerId, List<CartItemDto> cartItem, List<CouponInfoResponseDto> couponInfo) {
        this.sellerId = sellerId;
        this.cartItem = cartItem;
        this.couponInfo = couponInfo;
    }

}
