package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.dto.OrderDeliveryUpdateRequestDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.OrderInfoService;
import com.feelmycode.parabole.service.OrderService;
import com.feelmycode.parabole.service.UpdateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final UpdateService updateService;
    private final OrderService orderService;
    private final OrderInfoService orderInfoService;

    // 결제처리
    @PostMapping
    public ResponseEntity<ParaboleResponse> updateOrder(@RequestAttribute("userId") Long userId, @RequestBody OrderRequestDto orderUpdateRequestDto) {
        updateService.updateOrderState(userId, orderUpdateRequestDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 결제 완료");
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getOrderList(@RequestAttribute("userId") Long userId) {
        List<Order> orderList = orderService.getOrderList(userId);
        List<OrderInfoResponseDto> orderInfoResponseDtoList = orderInfoService.getOrderInfoListByUserId(orderList);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "모든 주문 정보 호출", orderInfoResponseDtoList);
    }
}
