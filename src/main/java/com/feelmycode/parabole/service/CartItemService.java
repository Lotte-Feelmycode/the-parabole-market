package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.CartItemDto;
import com.feelmycode.parabole.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    public String addItem(CartItemDto cartItemDto) {
        Cart cart = cartService.getCart(cartItemDto.getUserId());
        CartItem cartItem = cartItemDto.toEntity(
            cart,
            cartItemDto.getProduct(),
            cartItemDto.getCnt()
        );
        cartItemRepository.save(cartItem);
        return "장바구니 성공";
    }

    //TODO: userEntity로 변경
    public void updateItem(Product product, Long userId, Integer cnt) {
        CartItem currentCartItem = getCartItem(product.getId(), userId);
        currentCartItem.setCnt(cnt);
        cartItemRepository.save(currentCartItem);
    }

    //TODO: userEntity로 변경
    public List<CartItem> cartItemList(Long userId) {
        Cart cart = cartService.getCart(userId);
        return cartItemRepository.findAllByCartId(cart.getId());
    }

    public void cartDelete(Long cartId, Long productId) {
        deleteCartItem(cartId, productId);
    }

    public void cartListDelete(Long cartId, List<Long> productId) {
        cartItemRepository.deleteAllByProductIdInQuery(cartId, productId);
    }

    private CartItem getCartItem(Long productId, Long userId) {
        return cartItemRepository.findByProductIdAndCartId(productId,
            cartService.getCart(userId).getId()).orElseThrow(() -> new IllegalArgumentException());
    }

    private CartItem deleteCartItem(Long cartId, Long productId) {
        return cartItemRepository.deleteByProductId(cartId, productId)
            .orElseThrow(() -> new IllegalArgumentException());
    }


}
