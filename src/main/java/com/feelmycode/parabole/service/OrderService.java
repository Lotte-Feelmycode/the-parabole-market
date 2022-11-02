package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Order order) {
        Order getOrder =  orderRepository.save(order);
        return getOrder;
    }

    public List<Order> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findAllByUserId(userId)
            .stream()
            .filter(order -> order.getState() != -1)
            .collect(Collectors.toList());
        return orderList;
    }
    @Transactional
    public Order getOrder(Long userId) {
        Order getOrder = null;
        getOrder = this.getOrderByUserId(userId);
        return getOrder;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void checkOrderState(Long userId) {
        Order order = this.getOrder(userId);
        if (order == null) {
            return;
        }
        if (order.getState() < 0) {
            this.deleteOrder(order.getId());
        }
    }

    public Order getOrderByUserId(Long userId) {
        return orderRepository.findTop1ByUserIdOrderByIdDesc(userId);
    }

}
