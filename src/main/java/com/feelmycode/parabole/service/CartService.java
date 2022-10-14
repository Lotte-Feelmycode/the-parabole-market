package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    public Long createCart(Long userId) {
        checkNoCart(userId);
        User user = userService.getUser(userId);
        Cart cart = new Cart(user);
        Cart save = cartRepository.save(cart);
        return save.getId();
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(()->
            new ParaboleException(HttpStatus.BAD_REQUEST, "장바구니가 없습니다."));
    }

    public void checkNoCart(Long userId) {
        cartRepository.findByUserId(userId)
            .ifPresent(u -> {throw new ParaboleException(HttpStatus.BAD_REQUEST, "장바구니가 이미 있습니다.");});
    }
}
