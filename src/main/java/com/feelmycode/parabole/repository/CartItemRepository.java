package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.CartItem;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);


    Optional<CartItem> findByProductId(Long productId);

    List<CartItem> findAllByCartId(Long cartId);

    @Query("delete from CartItem c where c.cart = :cart_id and c.product in: pid")
    void deleteAllByProductIdInQuery(@Param("cart_id") Long cartId,
        @Param("pid") List<Long> productId);

    @Query("delete from CartItem c where c.cart=:cart_id and c.product=:productId")
    Optional<CartItem> deleteByProductId(Long cartId, Long productId);
}
