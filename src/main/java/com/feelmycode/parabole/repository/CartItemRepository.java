package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartId(Long cartId);
    Optional<CartItem> findById(Long cartItemId);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    Long countByCartId(Long cartId);

    @Modifying
    void deleteAllByIdIn(List<Long> cartItemId);

}
