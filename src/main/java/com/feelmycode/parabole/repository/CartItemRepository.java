package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);

    List<CartItem> findAllByCartId(Long cartId);

    @Transactional
    @Modifying
    @Query(value = "delete from Cart_items c where c.cart_id = :cart and c.product_id in :productsId", nativeQuery = true)
    void deleteAllByProductIdInQuery(Cart cart, List<Long> productsId);

}
