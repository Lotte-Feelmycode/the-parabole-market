package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.repository.ProductRepository;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long cartId;

    @NotNull
    private Product product;

    @NotNull
    private Integer cnt;

    public CartItemDto(Long userId,Product product,Integer cnt){
        this.userId=userId;
        this.product=product;
        this.cnt=cnt;
    }
    //TODO : user만들어지면 entity
    public CartItem toEntity(Cart cart, Product product,Integer cnt){
        return new CartItem(cart,product,cnt);
    }


}
