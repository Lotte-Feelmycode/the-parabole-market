package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartAddItemRequestDto {

    @NotNull
    private Long productId;

    @NotNull
    private Integer cnt;

    public CartAddItemRequestDto(Long productId, Integer cnt) {
        this.productId = productId;
        this.cnt = cnt;
    }

}
