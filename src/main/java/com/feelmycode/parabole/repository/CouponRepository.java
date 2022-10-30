package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Coupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllBySellerId(Long sellerId);
    @Query(value="select c from Coupon c where c.expiresAt > current_date and c.seller.id = :sellerId")
    List<Coupon> findAllValidCoupons(@Param("sellerId") Long sellerId);
    List<Coupon> findAllByNameAndSellerId(String couponName, Long sellerId);
}
