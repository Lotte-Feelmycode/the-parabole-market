package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findTop1ByUserIdOrderByIdDesc(Long userId);
    List<Order> findAllByUserId(Long userId);
}
