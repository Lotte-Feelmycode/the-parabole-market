package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.dto.OrderDeliveryUpdateRequestDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
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
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final UpdateService updateService;
    private final OrderService orderService;
    private final OrderInfoService orderInfoService;

    @PostMapping
    public ResponseEntity<ParaboleResponse> updateOrder(@RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {
        updateService.updateOrderState(orderUpdateRequestDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 수정 완료");
    }

    @PatchMapping
    public ResponseEntity<ParaboleResponse> updateDelivery(@RequestBody OrderDeliveryUpdateRequestDto updateDto) {
        updateService.updateDeliveryInfo(updateDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 정보 저장 완료");
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getOrderList(@RequestParam Long userId) {
        List<Order> orderList = orderService.getOrderList(userId);
        List<OrderInfoResponseDto> orderInfoResponseDtoList = orderInfoService.getOrderInfoListByUserId(orderList);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "모든 주문 정보 호출", orderInfoResponseDtoList);
    }
}
