package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.coupons.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.serialNo LIKE :CouponSNo")
    UserCoupon findUserCouponByCouponSerialNo(@Param("CouponSNo") String couponSerialNo);

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.user.id =:userId")
    List<UserCoupon> findUserCouponsByUserId(@Param("userId") Long userId);

}
