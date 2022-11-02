package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private Long cartId;
    private Long cnt;
    private List<CartBySellerDto> cartBySellerDtoList;

    public CartResponseDto(Long cartId, Long cnt, List<CartBySellerDto> cartBySellerDtoList) {
        this.cartId = cartId;
        this.cnt = cnt;
        this.cartBySellerDtoList = cartBySellerDtoList;
    }
}
