package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Coupon;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllBySellerId(Long sellerId);

    List<Coupon> findAllBySellerIdAndExpiresAtAfter(Long sellerId, LocalDateTime end);

    List<Coupon> findAllBySellerIdAndValidAtBeforeAndExpiresAtAfter(Long sellerId,
        LocalDateTime start, LocalDateTime end);

    List<Coupon> findAllByNameAndSellerId(String couponName, Long sellerId);
}
