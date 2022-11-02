package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartBySellerDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> cartBySellerDtoList;
    private CouponResponseDto couponDto;

    public CartBySellerDto(Long sellerId, String storeName,
        List<CartItemDto> cartBySellerDtoList, CouponResponseDto couponDto) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.cartBySellerDtoList = cartBySellerDtoList;
        this.couponDto = couponDto;
    }

    public void makeNotNullResponseDto() {
        this.couponDto = new CouponResponseDto();
    }
}
