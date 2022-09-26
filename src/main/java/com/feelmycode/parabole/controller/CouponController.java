package com.feelmycode.parabole.controller;

//import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.global.api.ParaboleResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        
        if (response == null) {
            return ParaboleResponse.CommonResponse(HttpStatus.BAD_REQUEST,
                false, "제목이 중복되어 쿠폰을 등록하지 못했습니다.", response);
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 등록 성공", response);
    }

    @PostMapping("/seller/giveout")
    public ResponseEntity<ParaboleResponse> assignUserToUserCoupon(@RequestBody Map<String, Object> map) {

        String couponSNo = (String)map.get("couponSNo");
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));

        String response = couponService.giveoutUserCoupon(couponSNo, userId);
        String message = null;

        if (response == "SUCCESS") {
            message = "UserCoupon Assigned Successfully";
        } else if (response == "FAIL") {
            message = "UserCoupon Already Has Owner(User)";
        }
        message = "쿠폰에 사용자 배정 API : " + message;
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, message);
    }

    @PostMapping("/seller/list")
    public ResponseEntity<ParaboleResponse> getSellerCouponList(
                                    @RequestBody Map<String, Object> map) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        Long sellerId = Long.parseLong(String.valueOf(map.get("sellerId")));

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponList(sellerId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "셀러 쿠폰 목록", sellerCouponList);
    }

    @PostMapping("/user/list")
    public ResponseEntity<ParaboleResponse> getUserCouponList(
                                    @RequestBody Map<String, Object> map) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));

        Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "유저 쿠폰 목록", userCouponList);
    }

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getCouponInfo(
                                    @RequestBody Map<String, Object> map) {

        String couponSNo = (String) map.get("couponSNo");
        CouponAvailianceResponseDto response = couponService.getCouponInfo(couponSNo);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 정보 반환", response);
    }

    @PostMapping("/user/use")
    public ResponseEntity<ParaboleResponse> useUserCoupon(@RequestBody Map<String, Object> map) {

        String couponSNo = (String)map.get("couponSNo");
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));
        String response = couponService.useUserCoupon(couponSNo, userId);

        String message = null;
        if (response == "NO_USER") {
            message = "No Users are Assigned to Coupon. Assign a User first.";
        } else if (response == "SUCCESS") {
            message = "Coupon Used Correctly";
        } else if (response == "ALREADY_USED") {
            message = "Coupon Already Used";
        } else if (response == "FAILURE") {
            message = "Coupon Unavailable (Not mine)";
        }

        message = "쿠폰 사용 API : " + message;
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, message);
    }
}
