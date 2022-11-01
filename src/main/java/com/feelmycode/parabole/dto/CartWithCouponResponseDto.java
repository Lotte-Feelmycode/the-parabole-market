package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartWithCouponResponseDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> cartItemList;
    private CouponResponseDto couponList;

    public CartWithCouponResponseDto(Long sellerId, String storeName,
        List<CartItemDto> cartItemList,CouponResponseDto couponList) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.cartItemList = cartItemList;
        this.couponList = couponList;
    }

    public void makeNotNullResponseDto() {
        this.couponList = new CouponResponseDto();
    }
}
