package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private Long cartId;
    private Long cnt;
    private List<CartWithCouponResponseDto> cartWithCoupon;

    public CartResponseDto(Long cartId, Long cnt, List<CartWithCouponResponseDto> cartWithCoupon) {
        this.cartId = cartId;
        this.cnt = cnt;
        this.cartWithCoupon = cartWithCoupon;
    }
}
