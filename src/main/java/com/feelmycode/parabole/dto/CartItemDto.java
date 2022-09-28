package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDto {

    @NotNull
    private Long cartItemId;

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer cnt;

    public CartItemDto(Long cartItemId, Long productId, Long userId, Integer cnt) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.userId = userId;
        this.cnt = cnt;
    }

    public CartItem toEntity(Cart cart, Product product, Integer cnt) {
        return new CartItem(cart, product, cnt);
    }

}
