package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserId(Long userId);
    UserCoupon findBySerialNoContains(String serialNo);
}
