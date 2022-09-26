package com.feelmycode.parabole.controller;

//import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.service.CouponService;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<CouponCreateResponseDto> addCoupon(
                                    @RequestBody CouponCreateRequestDto dto) {

        /** addCoupon, addUserCoupon 이 모두 발생한다. */
        return ResponseEntity.ok(couponService.addCoupon(dto));
        // TODO: 에러 처리 해주어야함
    }

    @PostMapping("/seller/giveout")
    public ResponseEntity<String> setUserToUserCoupon(@RequestBody Map<String, Object> map) {

        String couponSNo = (String)map.get("couponSNo");
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));

        String result = couponService.giveoutUserCoupon(couponSNo, userId);
        return ResponseEntity.ok(result);
        // TODO: 에러 처리 해주어야함
    }

    @PostMapping("/seller/list")
    public ResponseEntity<Page<CouponSellerResponseDto>> getSellerCouponList(
                                    @RequestBody Map<String, Object> map) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        Long sellerId = Long.parseLong(String.valueOf(map.get("sellerId")));

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponList(sellerId);
        return ResponseEntity.ok(sellerCouponList);
    }

    @PostMapping("/user/list")
    public ResponseEntity<Page<CouponUserResponseDto> > getUserCouponList(
                                    @RequestBody Map<String, Object> map) {

        Long userId = Long.parseLong(String.valueOf(map.get("userId")));

        Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
        return ResponseEntity.ok(userCouponList);
    }

    @GetMapping("/applyinfo")
    public ResponseEntity<CouponAvailianceResponseDto> getCouponInfo(
                                    @RequestBody Map<String, Object> map) {

        String couponSNo = (String) map.get("couponSNo");
        return ResponseEntity.ok(couponService.getCouponInfo(couponSNo));
    }

    @PostMapping("/user/use")
    public ResponseEntity<String> useUserCoupon(@RequestBody Map<String, Object> map) {

        String couponSNo = (String)map.get("couponSNo");
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));

        return ResponseEntity.ok((couponService.useUserCoupon(couponSNo, userId)));
        // TODO: 에러 처리 해주어야함
    }

}
