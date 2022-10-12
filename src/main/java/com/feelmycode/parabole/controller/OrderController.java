package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.OrderInfoService;
import com.feelmycode.parabole.service.OrderService;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private static final Long DELIVERY_FEE = 3000L;
    private final OrderService orderService;
    private final OrderInfoService orderInfoService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ParaboleResponse> createOrder(@RequestParam Long userId) {
        log.info("Create Order. userId: {}", userId);
        orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "주문 정보 생성 완료");
    }

    @DeleteMapping
    public ResponseEntity<ParaboleResponse> orderCancel(@RequestParam Long orderId) {
        log.info("Delete Order. userId: {}, orderId: {}", orderId);
        try {
            orderService.deleteOrder(orderId);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "주문을 취소할 수 없습니다.");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문을 취소했습니다.");
    }

}
