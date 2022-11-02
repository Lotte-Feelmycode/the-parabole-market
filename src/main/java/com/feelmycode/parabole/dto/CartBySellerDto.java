package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartBySellerDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> cartItemDtoList;
    private CouponResponseDto couponDto;

    public CartBySellerDto(Long sellerId, String storeName,
        List<CartItemDto> cartItemDtoList, CouponResponseDto couponDto) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.cartItemDtoList = cartItemDtoList;
        this.couponDto = couponDto;
    }

    public void makeNotNullResponseDto() {
        this.couponDto = new CouponResponseDto();
    }
}
