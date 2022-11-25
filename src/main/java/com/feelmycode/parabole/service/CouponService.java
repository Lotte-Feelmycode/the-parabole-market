package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.dto.CouponInfoDto;
import com.feelmycode.parabole.dto.CouponInfoResponseDto;
import com.feelmycode.parabole.dto.CouponRequestDto;
import com.feelmycode.parabole.dto.CouponSellerResponseDto;
import com.feelmycode.parabole.dto.CouponUserResponseDto;
import com.feelmycode.parabole.dto.CouponWithSellerStoreDto;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.enumtype.CouponUseState;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.NotSellerException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserCouponRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public CouponCreateResponseDto addCoupon(Long sellerId, @NotNull CouponCreateRequestDto dto) {

        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new NotSellerException());

        Coupon coupon = new Coupon(dto.getName(), seller, CouponType.returnNameToValue(dto.getType()), dto.getDiscountValue(), dto.getValidAt(),
            dto.getExpiresAt(), dto.getDetail(), dto.getCnt());

        couponRepository.save(coupon);

        for (int i = 0; i < dto.getCnt(); i++) {
            coupon.addUserCoupon(new UserCoupon(coupon));
        }
        return new CouponCreateResponseDto(coupon.getId(), coupon.getName(), seller.getStoreName(), coupon.getType().getName(), coupon.getCnt());
    }

//    public void giveoutUserCoupon(String couponSNo, Long userId) {
//
//        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
//        if (userCoupon == null) {
//            throw new NoDataException();
//        }
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException());
//        userCoupon.setUser(user);
//    }

    @Transactional
    public Boolean setCouponStock(Long couponId, Integer stock) {
        Coupon getCoupon = this.getCouponById(couponId);
        try {
            if (stock < 0) {
                getCoupon.setCouponForEvent(stock * -1);
            } else {
                getCoupon.cancelCouponEvent(stock);
            }
            couponRepository.save(getCoupon);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }

    public Coupon getCouponById(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new NoDataException());
    }

    public Page<CouponSellerResponseDto> getSellerCouponList(Long userId) {

        Seller seller = userRepository.findById(userId).orElseThrow(() -> new NoDataException()).getSeller();

        List<Coupon> couponList = couponRepository.findAllBySellerIdAndExpiresAtAfter(
            seller.getId(), LocalDateTime.now());

        List<CouponSellerResponseDto> dtos = couponList.stream()
            .map(CouponSellerResponseDto::new)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    public Page<CouponSellerResponseDto> getSellerCouponListBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new NoDataException());

        List<Coupon> couponList = couponRepository.findAllBySellerIdAndExpiresAtAfter(
            seller.getId(), LocalDateTime.now());

        List<CouponSellerResponseDto> dtos = couponList.stream()
            .map(CouponSellerResponseDto::new)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    public Page<CouponUserResponseDto> getUserCouponList(Long userId) {

        List<UserCoupon> couponListAll = userCouponRepository.findAllByUserId(userId);
        List<UserCoupon> validList = couponListAll.stream()
            .filter(userCoupon -> userCoupon.getUseState().equals(CouponUseState.NotUsed)
                && userCoupon.getCoupon().getValidAt().isBefore(LocalDateTime.now())
                && userCoupon.getCoupon().getExpiresAt().isAfter(LocalDateTime.now()))
            .collect(Collectors.toList());

        List<CouponUserResponseDto> dtos = new ArrayList<>();

        if (validList.isEmpty()) {
            throw new NoDataException();
        }
        for (UserCoupon userCoupon : validList) {
            Seller creator = userCoupon.getCoupon().getSeller();
            dtos.add(new CouponUserResponseDto(userCoupon.getCoupon(), userCoupon,
                creator.getStoreName(), creator.getId()));
        }
        return new PageImpl<>(dtos);
    }

    public UserCoupon getUserCouponBySerialNo(String serialNo) {
        return userCouponRepository.findBySerialNo(serialNo);
    }

    public List<UserCoupon> getUserCouponByCouponId(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NoDataException());
        return coupon.getUserCoupons();
    }

    public CouponInfoResponseDto getCouponInfo(String couponSNo) {

        UserCoupon userCoupon = userCouponRepository.findBySerialNo(couponSNo);
        if (userCoupon == null) {
            throw new NoDataException();
        }
        Coupon coupon = userCoupon.getCoupon();
        return new CouponInfoResponseDto(coupon.getType().getName(), coupon.getDiscountValue());
    }

    public List<CouponInfoDto> getCouponListByDiscountValue(Long userId, CouponRequestDto couponRequestDto) {

        Long sellerId = couponRequestDto.getSellerId();
        Integer totalFee = couponRequestDto.getTotalFee();

        List<UserCoupon> couponList = userCouponRepository.findAllByUserId(userId);

        List<UserCoupon> getCouponListBySellerId = couponList.stream()
            .filter(coupon -> coupon.getCoupon().getSeller().getId() == sellerId)
            .filter(coupon -> coupon.getUseState() == CouponUseState.NotUsed)
            .collect(Collectors.toList());

        Collections.sort(getCouponListBySellerId, new Comparator<UserCoupon>() {
            @Override
            public int compare(UserCoupon coupon1, UserCoupon coupon2) {
                Integer discountO1 = discountValue(coupon1, totalFee);
                Integer discountO2 = discountValue(coupon2, totalFee);
                return -Long.compare(discountO1, discountO2);
            }
        });

        List<CouponInfoDto> couponListDto = getCouponListBySellerId.stream()
            .map(coupon -> new CouponInfoDto(coupon.getCoupon().getName(), coupon.getSerialNo(),
                coupon.getCoupon().getSeller().getStoreName(), coupon.getCoupon().getType().getName(), coupon.getCoupon().getDiscountValue(), discountValue(coupon, totalFee)))
            .collect(Collectors.toList());

        return couponListDto;
    }

    public List<CouponWithSellerStoreDto> getCouponListByStoreName(String storeName) {
        List<Coupon>  couponList = couponRepository.findAll()
            .stream()
            .filter(coupon -> coupon.getSeller().getStoreName().contains(storeName) || storeName.contains(coupon.getSeller().getStoreName()))
            .collect(Collectors.toList());

        if(couponList == null)
            return new ArrayList<>();

        Coupon dto = couponList.get(0);
        return couponList.stream()
            .filter(coupon -> !dto.getSeller().getStoreName().equals(coupon.getSeller().getStoreName()))
            .map((Coupon coupon) -> new CouponWithSellerStoreDto(coupon.getId(), coupon.getName(), coupon.getType().getName(), coupon.getDetail(), coupon.getDiscountValue(), coupon.getExpiresAt()))
            .limit(3)
            .collect(Collectors.toList());
    }

    public Integer discountValue(UserCoupon userCoupon, Integer value) {
        Coupon coupon = userCoupon.getCoupon();
        CouponType type = coupon.getType();
        if(type == CouponType.RATE) {
            return (value *= coupon.getDiscountValue())/100;
        }
        return coupon.getDiscountValue();
    }

    @Transactional
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
        } else if (!user.getId().equals(userId)) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "사용자의 쿠폰이 아닙니다. 타인의 쿠폰입니다.");
        } else if (userCoupon.getCoupon().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "쿠폰이 만료되어 사용할 수 없습니다.");
        } else {
            if (userCoupon.getUseState() == CouponUseState.Used) {
                throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 사용완료된 쿠폰입니다.");
            }
        }
        userCoupon.useCoupon();
        userCouponRepository.save(userCoupon);
    }

}
