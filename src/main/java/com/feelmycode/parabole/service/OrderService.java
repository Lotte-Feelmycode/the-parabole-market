package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Long createOrder(Order order) {
        Order getOrder = orderRepository.save(order);
        return getOrder.getId();
    }

    @Transactional
    public Order updateOrder(Long userId, Long orderId, int state) {
        Order getOrder = checkAuthentication(userId, orderId);
        getOrder.setOrder(state, getOrder.getOrderInfoList());
        return getOrder;
    }

    public Order checkAuthentication(Long userId, Long orderId) {
        Order getOrder = this.getOrder(orderId);
        if (getOrder.getUser().getId() != userId) {
            new ParaboleException(HttpStatus.UNAUTHORIZED, "주문에 접근할 수 없습니다.");
        }
        return getOrder;
    }
    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order getOrder = checkAuthentication(userId, orderId);
        getOrder.setDeleted();
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST, "주문된 상품이 없습니다."));
    }

    public Order getOrderByUserId(Long userId) {
        return orderRepository.findTopByUserIdOrderByIdDesc(userId);
    }

}
