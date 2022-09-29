package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.OrderService;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private static final int DELIVERY_FEE = 3000;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ParaboleResponse> createOrder(@RequestParam Long userId) {
        log.info("Create Order. userId: {}", userId);
        orderService.createOrder(new Order(userService.getUser(userId), 1, DELIVERY_FEE));
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "주문 정보 생성 완료");
    }

    @PatchMapping
    public ResponseEntity<ParaboleResponse> updateOrderState(@RequestParam Long orderId,
        @RequestParam Long userId, @RequestParam int orderState) {
        log.info("Update Order. orderId: {}, userId: {}, orderState: {}", orderId, userId, orderState);
        Order order = orderService.updateOrder(userId, orderId, orderState);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 배송 상태 변경", order);
    }

    @DeleteMapping
    public ResponseEntity<ParaboleResponse> orderCancel(@RequestParam Long userId,
        @RequestParam Long orderId) {
        log.info("Delete Order. userId: {}, orderId: {}", userId, orderId);
        try {
            orderService.deleteOrder(userId, orderId);
        } catch(Exception e) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "주문을 취소할 수 없습니다.");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문을 취소했습니다.");
    }

}
