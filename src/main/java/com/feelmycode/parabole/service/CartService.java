package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Long createCart(Long userId) {
        Cart cart = getCart(userId);
        cartRepository.save(cart);
        return cart.getId();
    }

    public Cart getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "장바구니가 없습니다.");
        } else {
            return cart;
        }
    }
}
