package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemUpdateRequestDto {

    @NotNull
    private Long cartItemId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer cnt;

    public CartItemUpdateRequestDto(Long cartItemId, Long productId, Integer cnt) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.cnt = cnt;
    }

}
