package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.coupons.Coupon;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllBySellerId(Long sellerId);

}
