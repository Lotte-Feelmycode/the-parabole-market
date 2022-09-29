package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.OrderInfoService;
import com.feelmycode.parabole.service.OrderService;
import java.util.List;
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

@RestController
@RequestMapping("/api/v1/orderinfo")
@RequiredArgsConstructor
@Slf4j
public class OrderInfoController {

    private final OrderInfoService orderInfoService;
    private final OrderService orderService;

    // TODO: userCoupon 정보 가져오기
    @PostMapping
    public ResponseEntity<ParaboleResponse> createOrderInfo(@RequestBody OrderInfoListDto orderInfoDto) {
        try {
            Order order = orderService.getOrderByUserId(orderInfoDto.getUserId());
            OrderInfo orderInfo = orderInfoDto.toEntity(order);
            orderInfoService.saveOrderInfo(orderInfo);
        } catch(Exception e) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "결제할 상품을 추가할 수 없습니다.");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "결제목록에 상품을 추가했습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getOrderInfoList(@RequestParam Long userId) {
        log.info("Get Order List. userId: {}", userId);
        List<OrderInfo> orderInfoList = orderInfoService.getOrderInfoList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 정보 목록 조회", orderInfoList);
    }

}
