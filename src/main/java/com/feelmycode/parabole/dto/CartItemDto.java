package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import lombok.Getter;

@Getter
public class CartItemDto {
    private final Long cartItemId;
    private final ProductDto product;
    private final Integer count;

    public CartItemDto(Long cartItemId, Product product, Integer count) {
        this.cartItemId = cartItemId;
        this.product = new ProductDto(product);
        this.count = count;
    }

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.product = new ProductDto(cartItem.getProduct());
        this.count = cartItem.getCnt();
    }
}
