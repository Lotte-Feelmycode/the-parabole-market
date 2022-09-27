package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.CouponType;
import com.feelmycode.parabole.domain.CouponUseState;
//import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.UserCoupon;
//import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.dto.CouponAvailianceResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

//    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    /** Seller :: Coupon Registration and Confirmation */
    public CouponCreateResponseDto addCoupon(@NotNull CouponCreateRequestDto dto) {
//        TODO: SellerRepo 에 findById추가, 연관메서드addCoupon() 추가, line46 제거, 모든 주석 제거 필요
//        Seller s = sellerRepository.findById(dto.getSellerId())
//            .orElseThrow(() -> new IllegalArgumentException());
        Coupon c = dto.toEntity(dto.getSellerId(), dto.getType());

        if (!couponRepository.findAllByNameAndSellerId(dto.getName(), dto.getSellerId()).isEmpty()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "이전에 등록한 쿠폰명과 중복되어 쿠폰 등록에 실패하였습니다.");
        }
        couponRepository.save(c);

//        s.addCoupon(c);  // 연관관계의 주인은 seller (양방향이라 셀러에게 쿠폰을 추가해줘야하는데 Seller 없음)
        
        for (int i = 0; i < dto.getCnt(); i++) {
            c.addUserCoupon(new UserCoupon());     // 연관관계의 주인은 coupon
        }
//       save 를 안쓰는 이유는 아마도 cascadeType을 ALL로 해주었기 때문에 add해도 persistence가 refresh 되기 때문.
//       return new CouponCreateResponseDto(c.getName(), s.getName(), c.getType().ordinal(), c.getCnt());
        return new CouponCreateResponseDto(c.getName(), new String("sellerName"),
           dto.getType(), c.getCnt());
    }

    /** DB 에 Seller 없으면 API testing 실패 납니다. Table 생성 후에 실행 */
    public void giveoutUserCoupon(String couponSNo, Long userId) {

        UserCoupon uc = userCouponRepository.findBySerialNoLike(couponSNo)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다."));

        User u = userRepository.findById(userId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST,
                "사용자Id로 사용자를 검색한 내용이 존재하지 않습니다."));

        if (uc.getUser() == null) {
            u.setUserCoupon(uc);
        } else {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "쿠폰에 배정된 사용자가 이미 존재합니다.");
        }
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

        for (int i = 0; i < couponList.size(); i++) {
            UserCoupon uc = couponList.get(i);
            Long cidOfUc = uc.getCoupon().getId();
            Coupon c = couponRepository.findById(cidOfUc)
                .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST,
                    "쿠폰Id로 쿠폰을 검색한 내용이 존재하지 않습니다."));

            dtos.add(new CouponUserResponseDto(c, uc, c.getSellerName()));
        }
        return new PageImpl<>(dtos);
    }

    @Transactional(readOnly = true)
    public CouponAvailianceResponseDto getCouponInfo(String couponSNo) {

        UserCoupon uc = userCouponRepository.findBySerialNoLike(couponSNo)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST,
            "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다."));

        Coupon c = uc.getCoupon();

        String type = null;
        Object ret = null;

        if(c.getType() == CouponType.RATE){
            type = "RATE";
            ret = c.getDiscountRate();
        } else if (c.getType() == CouponType.AMOUNT) {
            type = "AMOUNT" ;
            ret = c.getDiscountAmount();
        }

        return new CouponAvailianceResponseDto(type, ret);
    }

    public void useUserCoupon(String couponSNo, Long userId) {

        UserCoupon uc = userCouponRepository.findBySerialNoLike(couponSNo)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰 일련번호로 사용자 쿠폰을 검색한 내용이 존재하지 않습니다."));
        User owner = uc.getUser();

        if (owner == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰에 배정된 사용자가 없습니다. 사용자를 먼저 배정하세요.");
        } else if (!owner.getId().equals(userId)){
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "사용자의 쿠폰이 아닙니다. 타인의 쿠폰입니다.");
        } else {
            if (uc.getUseState() == CouponUseState.Used) {
                throw new ParaboleException(HttpStatus.BAD_REQUEST,
                    "이미 사용완료된 쿠폰입니다.");
            }
        }
        uc.useCoupon();
    }

}
