package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.UserCoupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserId(Long userId);

    /** 쿼리문 안 붙였었는데 동작 안되길래 이 함수만 작성했어요 */
//    @Query("SELECT uc FROM UserCoupon uc WHERE uc.serialNo LIKE %:serialNo%")
    UserCoupon findBySerialNo(String serialNo);

}
