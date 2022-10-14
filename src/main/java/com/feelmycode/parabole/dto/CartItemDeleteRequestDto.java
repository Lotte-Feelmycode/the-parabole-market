package com.feelmycode.parabole.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDeleteRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private List<Long> cartItemId;

    public CartItemDeleteRequestDto(Long userId, List<Long> cartItemId) {
        this.userId = userId;
        this.cartItemId = cartItemId;
    }
}
