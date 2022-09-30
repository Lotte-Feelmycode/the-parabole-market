package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Long createOrder(Order order) {
        Order getOrder = orderRepository.save(order);
        return getOrder.getId();
    }

    @Transactional
    public Order updateOrder(Long userId, Long orderId, int state) {
        log.info("접근 성공");
        Order getOrder = checkAuthentication(userId, orderId);
        getOrder.setOrder(state, getOrder.getOrderInfoList());
        return getOrder;
    }

    public Order checkAuthentication(Long userId, Long orderId) {
        log.info("유효한 주문인지 확인");
        Order getOrder = null;
        try {
            log.info("값 가져오기");
            getOrder = this.getOrder(orderId);
            log.info("값 안가져와짐");
            if(getOrder == null) {
                new ParaboleException(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다");
            }
            if (getOrder.getUser().getId() != userId) {
                new ParaboleException(HttpStatus.UNAUTHORIZED, "주문에 접근할 수 없습니다.");
            }
        } catch(Exception e) {
            new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "무슨 문제일까");
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