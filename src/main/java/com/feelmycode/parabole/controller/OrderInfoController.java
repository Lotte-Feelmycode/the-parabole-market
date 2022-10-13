package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.OrderInfoService;
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
@RequestMapping("/api/v1/orderinfo")
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    // TODO: userCoupon 정보 가져오기
    @PostMapping
    public ResponseEntity<ParaboleResponse> createOrderInfo(@RequestBody List<OrderInfoListDto> orderInfoListDto) {
        try {
            for (OrderInfoListDto orderInfo : orderInfoListDto) {
                orderInfoService.saveOrderInfo(orderInfo);
            }
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "결제할 상품을 추가할 수 없습니다.");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "결제목록에 상품을 추가했습니다.");
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getOrderInfoList(@RequestParam Long userId) {
        log.info("Get Order List. userId: {}", userId);
        List<OrderInfoResponseDto> orderInfoList = orderInfoService.getOrderInfoList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 정보 목록 조회", orderInfoList);
    }

    // sellerId를 인자로 받는다고 되어있지만 판매자가 로그인했을 때 userId를 가져옴.  seller 였을 때만 불러올 수 있도록
    @GetMapping("/seller")
    public ResponseEntity<ParaboleResponse> getOrderInfoBySeller(@RequestParam Long sellerId) {
        log.info("Seller Id: {} ", sellerId);
        List<OrderInfoResponseDto> orderInfoList = orderInfoService.getOrderInfoListBySeller(
            sellerId);
        for (OrderInfoResponseDto dto : orderInfoList) {
            log.info(dto.toString());
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자의 상품 주문 정보 목록 조회",
            orderInfoList);
    }

    @PatchMapping
    public ResponseEntity<ParaboleResponse> updateOrderState(@RequestBody OrderUpdateRequestDto orderUpdate) {
        log.info("Update Order. orderInfoId: {}", orderUpdate.getOrderId());
        orderInfoService.updateOrderState(orderUpdate.getOrderInfoId(),
            orderUpdate.getOrderState());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 배송 상태 변경");
    }

}
