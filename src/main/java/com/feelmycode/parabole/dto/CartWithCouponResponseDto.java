package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartWithCouponResponseDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> cartItemList;
    private CouponResponseDto couponDto;

    public CartWithCouponResponseDto(Long sellerId, String storeName,
        List<CartItemDto> cartItemList,CouponResponseDto couponDto) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.cartItemList = cartItemList;
        this.couponDto = couponDto;
    }

    public void makeNotNullResponseDto() {
        this.couponDto = new CouponResponseDto();
    }
}
