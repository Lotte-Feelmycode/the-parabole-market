package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.CouponInfoResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.enumtype.CouponUseState;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponCreateResponseDto addCoupon(@NotNull CouponCreateRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NoDataException());

        Coupon coupon = new Coupon(dto.getName(), user.getSeller(), CouponType.returnNameToValue(dto.getType()), dto.getDiscountValue(), dto.getValidAt(),
            dto.getExpiresAt(), dto.getMaxDiscountAmount(), dto.getMinPaymentAmount(), dto.getDetail(), dto.getCnt());

        couponRepository.save(coupon);

        for (int i = 0; i < dto.getCnt(); i++) {
            coupon.addUserCoupon(new UserCoupon(coupon));
        }
        return new CouponCreateResponseDto(coupon.getName(), user.getName(), coupon.getType().getName(), coupon.getCnt());
    }

    public void giveoutUserCoupon(String couponSNo, Long userId) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new NoDataException();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException());
        userCoupon.setUser(user);
    }

    @Transactional(readOnly = true)
    public Page<CouponSellerResponseDto> getSellerCouponList(Long userId) {

        Seller seller = userRepository.findById(userId).orElseThrow(() -> new NoDataException()).getSeller();
        List<Coupon> couponList = couponRepository.findAllBySellerId(seller.getId());

        List<CouponSellerResponseDto> dtos = couponList.stream()
                                            .map(CouponSellerResponseDto::new)
                                            .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public Page<CouponSellerResponseDto> getSellerCouponListBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new NoDataException());
        List<Coupon> couponList = couponRepository.findAllBySellerId(seller.getId());

        List<CouponSellerResponseDto> dtos = couponList.stream()
            .map(CouponSellerResponseDto::new)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public Page<CouponUserResponseDto> getUserCouponList(Long userId) {

        List<UserCoupon> couponList =  userCouponRepository.findAllByUserId(userId);
        List<CouponUserResponseDto> dtos = new ArrayList<>();

        if (couponList.isEmpty()) {
            throw new NoDataException();
        }
        for (UserCoupon i : couponList) {
            Coupon nowCoupon = i.getCoupon();
            Seller nowSeller = nowCoupon.getSeller();
            dtos.add(new CouponUserResponseDto(nowCoupon, i,
                nowSeller.getStoreName()));
        }
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public CouponInfoResponseDto getCouponInfo(String couponSNo) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new NoDataException();
        }
        Coupon coupon = userCoupon.getCoupon();
        return new CouponInfoResponseDto(coupon.getType().getName(), coupon.getDiscountValue());
    }

    public void useUserCoupon(String couponSNo, Long userId) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND,
                "해당 일련번호를 가지는 쿠폰이 존재하지 않습니다.");
        }
        User user = userCoupon.getUser();

        if (user == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰에 배정된 사용자가 없습니다. 사용자를 먼저 배정하세요.");
        } else if (!user.getId().equals(userId)){
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "사용자의 쿠폰이 아닙니다. 타인의 쿠폰입니다.");
        } else if(userCoupon.getCoupon().getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰이 만료되어 사용할 수 없습니다.");
        } else {
            if (userCoupon.getUseState() == CouponUseState.Used) {
                throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 사용완료된 쿠폰입니다.");
            }
        }
        userCoupon.useCoupon();
    }

}
