package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.CouponAssignRequestDto;
import com.feelmycode.parabole.dto.CouponUseAndAssignRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.CouponService;
import com.feelmycode.parabole.dto.CouponInfoResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final SellerService sellerService;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

    @PostMapping("/create")
    public ResponseEntity<ParaboleResponse> addCoupon(
                                    @RequestBody CouponCreateRequestDto dto) {

        /** addCoupon, addUserCoupon 이 모두 발생한다. */
        CouponCreateResponseDto response = couponService.addCoupon(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 등록 성공", response);
    }

//    @PostMapping("/giveout")
//    public ResponseEntity<ParaboleResponse> assignUserToUserCoupon(
//        @RequestBody CouponUseAndAssignRequestDto dto) {
//
//        couponService.giveoutUserCoupon(dto.getCouponSNo(), dto.getUserId());
//        return ParaboleResponse.CommonResponse(HttpStatus.OK,
//            true, "쿠폰에 사용자가 배정되었습니다");
//    }

    @PostMapping("/assign")
    public ResponseEntity<ParaboleResponse> assignCoupon(@AuthenticationPrincipal Long userId,
        @RequestBody CouponAssignRequestDto dto) {

        Coupon coupon = couponService.getCouponById(dto.getCouponId());
        List<Long> getUserIdList = dto.getUserIdList();
        List<User> userList = new ArrayList<>();

        for (Long id : getUserIdList) {
            userList.add(userRepository.findById(id).orElseThrow(() -> new NoDataException()));
        }

        List<UserCoupon> userCouponList = coupon.getNotAssignedUserCouponList();
        if (userCouponList.size() < getUserIdList.size()) {
            throw new ParaboleException(HttpStatus.NOT_ACCEPTABLE, "사용자에게 배정할 쿠폰의 수량이 부족합니다.");
        } else {
            for (int i = 0; i < userList.size(); i++) {
                userCouponList.get(i).setUser(userList.get(i));
            }
            userCouponRepository.saveAll(userCouponList);
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "사용자에게 쿠폰 배정 성공");
    }

    @GetMapping("/seller/list")
    public ResponseEntity<ParaboleResponse> getSellerCouponList(@RequestParam Long sellerId) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponListBySellerId(sellerId);
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

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getCouponList(@AuthenticationPrincipal Long userId) {

        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

        if(!userService.isSeller(userId)){
            Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
            return ParaboleResponse.CommonResponse(HttpStatus.OK,
                true, "유저 쿠폰 목록", userCouponList);
        }
        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "셀러 쿠폰 목록", sellerCouponList);
    }

    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getCouponInfo(@RequestParam String couponSNo) {

        CouponInfoResponseDto response = couponService.getCouponInfo(couponSNo);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "쿠폰 정보 반환", response);
    }

    @PostMapping("/user/use")
    public ResponseEntity<ParaboleResponse> useUserCoupon(@RequestBody CouponUseAndAssignRequestDto dto) {

        couponService.useUserCoupon(dto.getCouponSNo(), dto.getUserId());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "쿠폰이 정상적으로 사용되었습니다.");
    }
}
