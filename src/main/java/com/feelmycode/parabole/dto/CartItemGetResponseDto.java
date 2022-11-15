package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartItemGetResponseDto {
    private final Long cartId;
    private final List<CartItemDto> cartItemDto;
    private final Integer itemCount;

    public CartItemGetResponseDto(Long cartId,
        List<CartItemDto> cartItemDto, Integer itemCount) {
        this.cartId = cartId;
        this.cartItemDto = cartItemDto;
        this.itemCount = itemCount;
    }
}
