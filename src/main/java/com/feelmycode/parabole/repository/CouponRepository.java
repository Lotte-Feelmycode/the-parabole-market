package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.coupons.Coupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


    @Query("SELECT c FROM Coupon c WHERE c.id =:couponId")
    Coupon findCouponByCouponId(@Param("couponId") Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.seller.id =:sellerId")
    List<Coupon> findCouponsBySellerId(@Param("sellerId") Long sellerId);

}
