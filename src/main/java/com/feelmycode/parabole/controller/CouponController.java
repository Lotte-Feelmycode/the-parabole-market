package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.service.CouponService;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {

    // TODO: 로거 추후 변경 예정
    Logger logger = LoggerFactory.getLogger(CouponController.class);
    private final CouponService couponService;
    private final SellerService sellerService;

//    @PostMapping("/seller/create")
//    public CouponCreateResponseDto addCoupon(@RequestBody CouponCreateRequestDto dto) {
//        /** addCoupon, addUserCoupon 이 모두 발생한다. */
//        return couponService.addCoupon(dto);
////        return ResponseEntity.ok(couponService.addCoupon(dto));
//        // TODO: 에러 처리 해주어야함
//    }

    @PostMapping("/seller/giveout")
    public ResponseEntity<String> setUserToUserCoupon(@RequestParam String couponSNo,
        @RequestParam Long userId) {
        String result = couponService.giveoutUserCoupon(couponSNo, userId);
        return ResponseEntity.ok(result);
        // TODO: 에러 처리 해주어야함
    }

//    @PostMapping("/seller/list")
//    public ResponseEntity<List<CouponSellerResponseDto>> getSellerCouponList(
//        @RequestParam Long sellerId) {
//        List<CouponSellerResponseDto> sellerCouponList = couponService.findCouponsBySellerId(
//            sellerId);
//        return ResponseEntity.ok(sellerCouponList);
//    }

//    @PostMapping("/user/list")
//    public ResponseEntity<List<CouponUserResponseDto>> getUserCouponList(
//        @RequestParam Long userId) {
//        List<CouponUserResponseDto> userCouponList = couponService.findUserCouponsByUserId(userId);
//        return ResponseEntity.ok(userCouponList);
//    }

    @GetMapping("/applyinfo")
    public ResponseEntity<CouponAvailianceResponseDto> getCouponInfo(
        @RequestParam String couponSNo) {
        return ResponseEntity.ok(couponService.getCouponInfo(couponSNo));
    }

    @PostMapping("/user/use")
    public ResponseEntity<String> useUserCoupon(@RequestParam String couponSNo,
                                                @RequestParam Long userId) {
        return ResponseEntity.ok((couponService.useUserCoupon(couponSNo, userId)));
        // TODO: 에러 처리 해주어야함
    }
}
