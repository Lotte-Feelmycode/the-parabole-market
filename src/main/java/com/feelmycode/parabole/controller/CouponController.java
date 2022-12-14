package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.CouponAssignRequestDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponDto;
import com.feelmycode.parabole.dto.CouponInfoResponseDto;
import com.feelmycode.parabole.dto.CouponRequestDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUseAndAssignRequestDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.CouponService;
import com.feelmycode.parabole.service.SellerService;
import com.feelmycode.parabole.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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

    @PostMapping("/new")
    public ResponseEntity<ParaboleResponse> addCoupon(@RequestAttribute Long sellerId,
                                    @RequestBody CouponCreateRequestDto dto) {

        /** addCoupon, addUserCoupon ??? ?????? ????????????. */
        CouponCreateResponseDto response = couponService.addCoupon(sellerId, dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "?????? ?????? ??????", response);
    }

    @PostMapping("/assign")
    public ResponseEntity<ParaboleResponse> assignCoupon(
        @RequestBody CouponAssignRequestDto dto) {

        Coupon coupon = couponService.getCouponById(dto.getCouponId());
        List<Long> getUserIdList = dto.getUserIdList();
        List<User> userList = new ArrayList<>();

        for (Long id : getUserIdList) {
            userList.add(userRepository.findById(id).orElseThrow(() -> new NoDataException()));
        }

        List<UserCoupon> userCouponList = coupon.getNotAssignedUserCouponList();
        if (userCouponList.size() < getUserIdList.size()) {
            throw new ParaboleException(HttpStatus.NOT_ACCEPTABLE, "??????????????? ????????? ????????? ????????? ???????????????.");
        } else {
            for (int i = 0; i < userList.size(); i++) {
                userCouponList.get(i).setUser(userList.get(i));
                userCouponList.get(i).setAcquiredDate(LocalDateTime.now());
            }
            userCouponRepository.saveAll(userCouponList);
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "??????????????? ?????? ?????? ??????");
    }

    @GetMapping("/seller/list")
    public ResponseEntity<ParaboleResponse> getSellerCouponList(@RequestAttribute Long sellerId) {

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponListBySellerId(sellerId);

        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "?????? ?????? ??????", sellerCouponList);
    }

    @GetMapping("/store")
    public ResponseEntity<ParaboleResponse> getSellerCouponListNoAuth(@RequestParam Long sellerId) {

        Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponListBySellerId(sellerId);

        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "?????? ?????? ??????", sellerCouponList);
    }

    @GetMapping("/user/list")
    public ResponseEntity<ParaboleResponse> getUserCouponList(@RequestAttribute Long userId) {

        Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "?????? ?????? ???????????? ????????? ?????? ????????? ?????? ?????? ??????", userCouponList);
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getCouponList(@RequestAttribute Long userId) {

        if(userService.isSeller(userId)){
            Seller seller = sellerService.getSellerByUserId(userId);
            Page<CouponSellerResponseDto> sellerCouponList = couponService.getSellerCouponList(seller.getId());
            return ParaboleResponse.CommonResponse(HttpStatus.OK,
                true, "?????? ?????? ??????", sellerCouponList);
        }
        Page<CouponUserResponseDto> userCouponList = couponService.getUserCouponList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "?????? ?????? ??????", userCouponList);
    }


    @GetMapping("/info")
    public ResponseEntity<ParaboleResponse> getCouponInfo(@RequestParam String couponSNo) {

        CouponInfoResponseDto response = couponService.getCouponInfo(couponSNo);
        return ParaboleResponse.CommonResponse(HttpStatus.OK,
            true, "?????? ?????? ??????", response);
    }

    @GetMapping("/data")
    public CouponDto getCouponData(@RequestParam Long couponId) {
        Coupon coupon = couponService.getCouponById(couponId);
        CouponDto couponDto = new CouponDto(coupon.getId(), coupon.getName(),
            CouponType.returnNameByValue(coupon.getType().getValue()), coupon.getDetail(),
            coupon.getDiscountValue(), coupon.getExpiresAt());
        return couponDto;
    }

    @PostMapping("/user/use")
    public ResponseEntity<ParaboleResponse> useUserCoupon(@RequestAttribute Long userId,
        @RequestBody CouponUseAndAssignRequestDto dto) {

        couponService.useUserCoupon(dto.getCouponSNo(), userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "????????? ??????????????? ?????????????????????.");
    }

    @GetMapping("/{couponId}/stock/{stock}")
    public Boolean setCouponRemains(@PathVariable("couponId") Long couponId,
        @PathVariable("stock") Integer stock) {
        log.info("Set Coupon Remains By Event Server : {} ", couponId);
        return couponService.setCouponStock(couponId, stock);
    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getCouponGroupBySeller(@RequestAttribute Long userId, @RequestParam Long sellerId, @RequestParam Integer totalFee) {
        CouponRequestDto couponRequestDto = new CouponRequestDto(sellerId, totalFee);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "????????? "+userId+" ?????? "+couponRequestDto.getSellerId()+" ??????????????? ????????? ??? ?????? ?????? ???????????????.", couponService.getCouponListByDiscountValue(userId, couponRequestDto));
    }

}
