package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.enumtype.CouponUseState;
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

    /** Seller :: Coupon Registration and Confirmation */
    public CouponCreateResponseDto addCoupon(@NotNull CouponCreateRequestDto dto) {
        Seller seller = sellerRepository.findById(dto.getSellerId())
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 판매자 Id가 존재하지 않습니다."));
        Coupon coupon = dto.toEntity(seller, CouponType.returnNameToValue(dto.getType()));
        coupon.setSeller(seller);
        couponRepository.save(coupon);

//        seller.addCoupon(coupon);
        
        for (int i = 0; i < dto.getCnt(); i++) {
            coupon.addUserCoupon(new UserCoupon());     // 연관관계의 주인은 coupon
        }
        return new CouponCreateResponseDto(coupon.getName(), new String("sellerName"),
           dto.getType(), coupon.getCnt());
    }

    /** DB 에 Seller 없으면 API testing 실패 납니다. Table 생성 후에 실행 */
    public void giveoutUserCoupon(String couponSNo, Long userId) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND,
                "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND,
                "사용자Id로 사용자를 검색한 내용이 존재하지 않습니다."));

        userCoupon.setUser(user);
    }

    @Transactional(readOnly = true)
    public Page<CouponSellerResponseDto> getSellerCouponList(Long sellerId) {

        List<Coupon> couponList = couponRepository.findAllBySellerId(sellerId);
        List<CouponSellerResponseDto> dtos = couponList.stream()
                                            .map(CouponSellerResponseDto::new)
                                            .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public Page<CouponUserResponseDto> getUserCouponList(Long userId) {

        List<UserCoupon> couponList =  userCouponRepository.findAllByUserId(userId);
        List<CouponUserResponseDto> dtos = new ArrayList<>();

        if (couponList.size() == 0) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "소유한 쿠폰이 없습니다.");
        }
        for (int i = 0; i < couponList.size(); i++) {
            UserCoupon userCoupon = couponList.get(i);
            Coupon coupon = userCoupon.getCoupon();
            Seller seller = coupon.getSeller();
            if (userCoupon == null || coupon == null || seller == null) {
                throw new ParaboleException(HttpStatus.NOT_FOUND, "소유한 쿠폰이 없습니다.");
            }
            dtos.add(new CouponUserResponseDto(coupon, userCoupon, seller.getUser().getName()));
        }
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public CouponAvailianceResponseDto getCouponInfo(String couponSNo) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다.");
        }
        Coupon coupon = userCoupon.getCoupon();

        String type = coupon.getType().getName();
        Object discountValue = null;

        if(coupon.getType().getName().equals("RATE")) {
            discountValue = coupon.getDiscountRate();
        }
        discountValue = coupon.getDiscountAmount();

        return new CouponAvailianceResponseDto(type, discountValue);
    }

    public void useUserCoupon(String couponSNo, Long userId) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다.");
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
