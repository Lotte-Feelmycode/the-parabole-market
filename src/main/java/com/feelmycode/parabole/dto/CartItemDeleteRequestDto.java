package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDeleteRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long cartItemId;

    public CartItemDeleteRequestDto(Long userId, Long cartItemId) {
        this.userId = userId;
        this.cartItemId = cartItemId;
    }
}
