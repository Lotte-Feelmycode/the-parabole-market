package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    //TODO entity로 바꿔줘야함
    public Long createCart(Long userId){
        Cart cart=getCart(userId);
        cartRepository.save(cart);
        return cart.getId();
    }
    public Cart getCart(Long userId){
        return cartRepository.findByUserId(userId)
            .orElseThrow(()->new IllegalArgumentException());
    }
}
