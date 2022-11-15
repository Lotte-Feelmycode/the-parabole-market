package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.dto.OrderInfoRequestDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.dto.OrderResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.OrderInfoService;
import com.feelmycode.parabole.service.OrderService;
import com.feelmycode.parabole.service.UpdateService;
import com.feelmycode.parabole.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orderinfo")
@RequiredArgsConstructor
public class OrderInfoController {

    private static final Long DELIVERY_FEE = 0L;

    private final OrderService orderService;
    private final OrderInfoService orderInfoService;
    private final UpdateService updateService;
    private final UserService userService;

    // TODO: order paging
    // TODO: userCoupon 정보 가져오기
    @PostMapping
    public ResponseEntity<ParaboleResponse> createOrderInfo(@RequestAttribute("userId") Long userId, @RequestBody OrderInfoListDto orderInfoListDto) {
        try {
            orderService.checkOrderState(userId);
            orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));

            for (OrderInfoSimpleDto orderInfo : orderInfoListDto.getOrderInfoDto()) {
                orderInfoService.saveOrderInfo(userId, orderInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "결제할 상품을 추가할 수 없습니다.");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "결제목록에 상품을 추가했습니다.");
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getOrderInfoList(@RequestAttribute("userId") Long userId) {
        OrderResponseDto orderResponseDto = orderInfoService.getOrderInfoGroupBySellerIdOrderByIdDesc(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "주문 정보 목록 조회", orderResponseDto);
    }

    @GetMapping("/seller")
    public ResponseEntity<ParaboleResponse> getOrderInfoBySeller(@RequestAttribute("userId") Long userId) {
        log.info("Seller Id: {} ", userId);
        List<OrderInfoResponseDto> orderInfoList = orderInfoService.getOrderInfoListBySeller(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자의 상품 주문 정보 목록 조회",orderInfoList);
    }

    @PatchMapping
    public ResponseEntity<ParaboleResponse> updateOrderInfo(@RequestAttribute("userId") Long userId, @RequestBody OrderInfoRequestDto orderInfoRequestDto) {
        updateService.updateOrderInfoState(userId, orderInfoRequestDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "사용자의 상세 주문 배송 정보 수정");
    }

}
