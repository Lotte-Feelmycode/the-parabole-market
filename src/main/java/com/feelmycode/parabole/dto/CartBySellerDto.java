package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartBySellerDto {

    private Long sellerId;
    private String storeName;
    private List<CartItemDto> cartItemDtoList;

    public CartBySellerDto(Long sellerId, String storeName,
        List<CartItemDto> cartItemDtoList) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.cartItemDtoList = cartItemDtoList;
    }

}
