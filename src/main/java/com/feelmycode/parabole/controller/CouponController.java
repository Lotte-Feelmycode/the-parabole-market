package com.feelmycode.parabole.controller;

//import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.dto.CouponUseAndAssignRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.CouponService;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    //    private final SellerService sellerService;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

    @PostMapping("/seller/create")
    public ResponseEntity<ParaboleResponse> addCoupon(
                                    @RequestBody CouponCreateRequestDto dto) {

        /** addCoupon, addUserCoupon 이 모두 발생한다. */
        CouponCreateResponseDto response = couponService.addCoupon(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 등록 성공", response);
    }

    @PostMapping("/seller/giveout")
    public ResponseEntity<ParaboleResponse> assignUserToUserCoupon(
        @RequestBody CouponUseAndAssignRequestDto dto) {

        couponService.giveoutUserCoupon(dto.getCouponSNo(), dto.getUserId());
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰에 사용자가 배정되었습니다");
    }

    @GetMapping("/seller/list")
    public ResponseEntity<ParaboleResponse> getSellerCouponList(@RequestParam Long sellerId) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponList(sellerId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "셀러 쿠폰 목록", sellerCouponList);
    }

    @GetMapping("/user/list")
    public ResponseEntity<ParaboleResponse> getUserCouponList(@RequestParam Long userId) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

        Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "유저 쿠폰 목록", userCouponList);
    }

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getCouponInfo(@RequestParam String couponSNo) {

        CouponAvailianceResponseDto response = couponService.getCouponInfo(couponSNo);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 정보 반환", response);
    }

    @PostMapping("/user/use")
    public ResponseEntity<ParaboleResponse> useUserCoupon(@RequestBody CouponUseAndAssignRequestDto dto) {

        couponService.useUserCoupon(dto.getCouponSNo(), dto.getUserId());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "쿠폰이 정상적으로 사용되었습니다.");
    }
}
