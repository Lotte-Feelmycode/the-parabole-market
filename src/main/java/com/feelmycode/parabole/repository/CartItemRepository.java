package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartId(Long cartId);

    CartItem findAllById(Long cartItemId);

    @Modifying
    void deleteAllByIdIn(List<Long> cartItemId);

}
