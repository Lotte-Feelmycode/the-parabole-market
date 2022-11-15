package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.enumtype.CouponType;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CouponRepositoryTest {

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Test
    @DisplayName("CouponRepositoryTest: test_jpa_functions")
    public void test_jpa_functions() {

        Seller seller = new Seller("newSeller's Store Name", "1010101010-1010101010");
        Seller newSeller = sellerRepository.save(seller);

        Coupon coup1 = new Coupon("test coupon", newSeller, CouponType.AMOUNT, 1000,
            LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3), "test coupon detail", 3);
        Coupon coup2 = new Coupon("test coupon", newSeller, CouponType.AMOUNT, 1000,
            LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3), "test coupon detail", 3);
        Coupon coup3 = new Coupon("test coupon", newSeller, CouponType.AMOUNT, 1000,
            LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), "test coupon detail", 3);
        Coupon coup4 = new Coupon("test coupon", newSeller, CouponType.AMOUNT, 1000,
            LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5), "test coupon detail", 3);

        couponRepository.save(coup1);
        couponRepository.save(coup2);
        couponRepository.save(coup3);
        couponRepository.save(coup4);

        Assertions.assertEquals(couponRepository.findAllBySellerId(newSeller.getId()).size(), 4);

        Assertions.assertEquals(
            couponRepository.findAllBySellerIdAndValidAtBeforeAndExpiresAtAfter(newSeller.getId(), LocalDateTime.now(),
                LocalDateTime.now()).size(), 2);
    }

}
