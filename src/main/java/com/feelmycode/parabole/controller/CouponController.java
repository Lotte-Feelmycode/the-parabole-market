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

    Logger logger = LoggerFactory.getLogger(CouponController.class);
    private final CouponService couponService;
    private final SellerService sellerService;

    /* 1.OK */
    @PostMapping("/seller/create")
    public CouponCreateResponseDto addCoupon(@RequestBody CouponCreateRequestDto dto) {
        /** addCoupon, addUserCoupon 이 모두 발생한다. */
        return couponService.addCoupon(dto);
//        return ResponseEntity.ok(couponService.addCoupon(dto));
        // TODO: 에러 처리 해주어야함
    }

    /* 2.OK */
    /** Set User to UserCoupon :: 즉 쿠폰을 사용자에게 배부 */
    @PostMapping("/seller/giveout")
    public ResponseEntity<String> giveoutUserCoupon(@RequestParam String couponSNo,
        @RequestParam Long userId) {
        String result = couponService.giveoutUserCoupon(couponSNo, userId);
        return ResponseEntity.ok(result);
        // TODO: 에러 처리 해주어야함
    }

    /* 3.OK */
    /** Show All Seller's Coupon List :: 판매자 내 등록 쿠폰 목록 */
    @PostMapping("/seller/list")
    public ResponseEntity<List<CouponSellerResponseDto>> getSellerCouponList(
        @RequestParam Long sellerId) {
        List<CouponSellerResponseDto> sellerCouponList = couponService.findCouponsBySellerId(
            sellerId);
        return ResponseEntity.ok(sellerCouponList);
    }

    /* 4.OK */
    /** Show All User's Coupon List :: 사용자 내 소유 쿠폰 목록 */
    @PostMapping("/user/list")
    public ResponseEntity<List<CouponUserResponseDto>> getUserCouponList(
        @RequestParam Long userId) {
        List<CouponUserResponseDto> userCouponList = couponService.findUserCouponsByUserId(userId);
        return ResponseEntity.ok(userCouponList);
    }

    /* 5.OK */
    /** Show Info for Product Purchase :: 쿠폰 사용을 위해서 쿠폰 정보(유형,할인율/액수 반환 */
    @GetMapping("/applyinfo")
    public ResponseEntity<CouponAvailianceResponseDto> getCouponInfo(
        @RequestParam String couponSNo) {
        return ResponseEntity.ok(couponService.getCouponInfo(couponSNo));
    }

    /* 6.OK */
    /** Change useState (Use Coupon) :: 쿠폰 사용시 로직 (사용자 본인이 소유해야 사용 가능) */
    @PostMapping("/user/use")
    public ResponseEntity<String> useUserCoupon(@RequestParam String couponSNo,
                                                @RequestParam Long userId) {
        return ResponseEntity.ok((couponService.useUserCoupon(couponSNo, userId)));
        // TODO: 에러 처리 해주어야함
    }
}
