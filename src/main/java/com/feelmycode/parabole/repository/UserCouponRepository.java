package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserId(Long userId);
    @Query(value="select uc from UserCoupon uc where uc.coupon.expiresAt <= current_date")
    List<UserCoupon> findAllValidByUserId(Long userId);
    UserCoupon findBySerialNo(String serialNo);

}
