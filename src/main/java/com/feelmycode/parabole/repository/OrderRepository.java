package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserIdOrderByOrderIdDesc(Long userId);

}
