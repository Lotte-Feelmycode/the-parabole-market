package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.OrderInfoService;
import com.feelmycode.parabole.service.OrderService;
import com.feelmycode.parabole.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private static final Long DELIVERY_FEE = 0L;

    private final OrderService orderService;
    private final UserService userService;
    private final OrderInfoService orderInfoService;

    @GetMapping
    public ResponseEntity<ParaboleResponse> createOrder(@RequestParam Long userId) {
        log.info("Create Order. userId: {}", userId);
        orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "주문 정보 생성 완료");
    }

    @PostMapping("/update")
    public ResponseEntity<ParaboleResponse> updateOrder(@RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {
        orderService.updateOrderState(orderUpdateRequestDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 수정 완료");
    }

}
