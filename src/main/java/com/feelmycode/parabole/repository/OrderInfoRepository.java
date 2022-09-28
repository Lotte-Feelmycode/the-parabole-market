package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.OrderInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    List<OrderInfo> findAllByOrderId(Long orderId);

}