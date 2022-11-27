package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SellerService sellerService;
    private final ProductService productService;
    private final CouponService couponService;
    private final OrderInfoService orderInfoService;

    @GetMapping
    public ResponseEntity<ParaboleResponse> getStoreNameList() {
        List<String> sellerInfoList = sellerService.getSellerList()
            .stream()
            .map((seller) -> seller.getStoreName())
            .limit(3)
            .collect(Collectors.toList());

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자 정보 목록", sellerInfoList);
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getProductList(@RequestParam String storeName) {
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, storeName+"의 상품목록", productService.getProductListByStoreName(storeName));
    }

    @GetMapping("/coupon")
    public ResponseEntity<ParaboleResponse> getCouponList(@RequestParam String storeName) {
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, storeName+"의 쿠폰 항목", couponService.getCouponListByStoreName(storeName));
    }

    @GetMapping("/order")
    public ResponseEntity<ParaboleResponse> getOrderList(@RequestAttribute Long userId) {
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "총 주문 금액", orderInfoService.getOrderTotalByUserId(userId));
    }

    @GetMapping("/product")
    public ResponseEntity<ParaboleResponse> getProductPrice(@RequestParam String productName) {
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, productName+"의 가격", productService.getProductPriceByProductName(productName));
    }
}
