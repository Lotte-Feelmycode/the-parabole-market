package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer cnt;

    @NotNull
    private List<Long> productsId;

    public CartItemDto(Long userId,Long productId,Integer cnt){
        this.userId=userId;
        this.productId=productId;
        this.cnt=cnt;
    }

    public CartItemDto(Long userId, List<Long> productsId){
        this.userId=userId;
        this.productsId=productsId;
    }

    public CartItem toEntity(Cart cart, Product product,Integer cnt){
        return new CartItem(cart,product,cnt);
    }

}
