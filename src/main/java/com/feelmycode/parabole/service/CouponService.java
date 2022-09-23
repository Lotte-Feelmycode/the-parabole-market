package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.CouponType;
import com.feelmycode.parabole.domain.CouponUseState;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.coupons.Coupon;
import com.feelmycode.parabole.domain.coupons.UserCoupon;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
        Seller s = sellerRepository.findSellerBySellerId(dto.getSellerId());
        Coupon c = dto.toEntity(s, dto.getType());

        s.addCoupon(c);        // 연관관계의 주인은 seller
        for (int i = 0; i < dto.getCnt(); i++) {
            c.addUserCoupon(new UserCoupon());     // 연관관계의 주인은 coupon
        }
        // save 를 안쓰는 이유는 아마도 cascadeType을 ALL 로 해주었기 때문에 add 해도 persistence가 refresh 됭서?
        return new CouponCreateResponseDto(c.getName(), s.getName(), c.getType().ordinal(), c.getCnt());
    }

    public String giveoutUserCoupon(String couponSNo, Long userId) {

        UserCoupon uc = userCouponRepository.findUserCouponByCouponSerialNo(couponSNo);
        User u = userRepository.findUserByUserId(userId);

        if (uc.getUser() == null) {
            u.setUserCoupon(uc);
            return "UserCoupon Assigned Correctly";
        } else {
            return "UserCoupon Already Has Owner(User)";
        }
    }

    @Transactional(readOnly = true)
    public List<CouponSellerResponseDto> findCouponsBySellerId(Long sellerId) {
        List<Coupon> couponList =  couponRepository.findCouponsBySellerId(sellerId);
        return couponList.stream()
            .map(CouponSellerResponseDto::new)
            .collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    public List<CouponUserResponseDto> findUserCouponsByUserId(Long userId) {
        List<UserCoupon> couponList =  userCouponRepository.findUserCouponsByUserId(userId);
        List<CouponUserResponseDto> dtos = new ArrayList<>();

        for (int i = 0; i < couponList.size(); i++) {
            UserCoupon uc = couponList.get(i);
            Long cidOfUc = uc.getCoupon().getId();
            Coupon c = couponRepository.findCouponByCouponId(cidOfUc);

            dtos.add(new CouponUserResponseDto(c, uc, c.getSeller().getName()));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public CouponAvailianceResponseDto getCouponInfo(String couponSNo) {

        UserCoupon uc = userCouponRepository.findUserCouponByCouponSerialNo(couponSNo);
        Coupon c = uc.getCoupon();

        Integer type = null;
        Object ret = null;

        if(c.getType() == CouponType.RATE){
            type = 1;
            ret = c.getDiscountRate();
        } else if (c.getType() == CouponType.AMOUNT) {
            type = 2 ;
            ret = c.getDiscountAmount();
        }
        return new CouponAvailianceResponseDto(type, ret);
    }

    public String useUserCoupon(String couponSNo, Long userId) {
        String ret = null;

        UserCoupon uc = userCouponRepository.findUserCouponByCouponSerialNo(couponSNo);
        if (uc.getUser().getId() == userId) {
            if (uc.getUseState() == CouponUseState.NotUsed) {
                uc.useCoupon(LocalDateTime.now().toString());
                ret = "Coupon Used Correctly";
            } else if (uc.getUseState() == CouponUseState.Used) {
                ret = "Coupon Already Used";
            }
        } else {
            ret = "Coupon Unavailable (Not mine)";
        }
        return ret;
    }

}
