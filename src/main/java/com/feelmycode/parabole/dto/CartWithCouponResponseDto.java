package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartWithCouponResponseDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> getItemList;
    private CouponResponseDto getCouponList;

    public CartWithCouponResponseDto(Long sellerId, String storeName,
        List<CartItemDto> getItemList,CouponResponseDto getCouponList) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.getItemList = getItemList;
        this.getCouponList = getCouponList;
    }

    public void makeNotNullResponseDto() {
        this.getCouponList = new CouponResponseDto();
    }
}
