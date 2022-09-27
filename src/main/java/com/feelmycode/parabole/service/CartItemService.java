package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.CartItemDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public void addItem(CartItemDto cartItemDto) {
        Cart cart = cartService.getCart(cartItemDto.getUserId());
        CartItem cartItem = cartItemDto.toEntity(
            cart,
            getProduct(cartItemDto.getProductId()),
            cartItemDto.getCnt()
        );
        cartItemRepository.save(cartItem);
    }

    //TODO: userEntity로 변경
    public void updateItem(CartItemDto cartItemDto) {
        CartItem currentCartItem = getCartItem(cartItemDto.getProductId(), cartItemDto.getUserId());
        currentCartItem.setCnt(cartItemDto.getCnt());
        cartItemRepository.save(currentCartItem);
    }

    //TODO: userEntity로 변경
    public List<CartItem> cartItemList(Long userId) {
        Cart cart = cartService.getCart(userId);
        return cartItemRepository.findAllByCartId(cart.getId());
    }

    public void cartListDelete(CartItemDto cartItemDto) {
        Cart cart = cartService.getCart(cartItemDto.getUserId());
        cartItemRepository.deleteAllByProductIdInQuery(cart,
            getProductsInCart(cartItemDto.getProductsId(),
                cart.getId()));
    }

    private CartItem getCartItem(Long productId, Long userId) {
        return cartItemRepository.findByProductIdAndCartId(productId,
            cartService.getCart(userId).getId()).orElseThrow(() -> new ParaboleException(
            HttpStatus.BAD_REQUEST, "카트에 해당 상품이 존재하지 않습니다."));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
    }

    private List<Long> getProductsInCart(List<Long> productId, Long cartId) {
        List<Long> result = new ArrayList<>();
        for (Long pid : productId) {
            if (cartItemRepository.findByProductIdAndCartId(pid, cartId).isPresent()) {
                result.add(pid);
            } else {
                throw new ParaboleException(HttpStatus.BAD_REQUEST, "카트에 해당 상품이 없습니다");
            }
        }
        return result;
    }

}
