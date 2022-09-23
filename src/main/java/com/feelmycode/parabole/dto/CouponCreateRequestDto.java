package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.CouponType;
//import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.coupons.Coupon;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CouponCreateRequestDto {

        private String name;
        private Long sellerId;
        private Integer type;
        private Integer discountRate;
        private Long discountAmount;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String details;
        private Integer cnt;    // 발행할 쿠폰수

        /** DTO to Entity **/
        public Coupon toEntity(Long sellerId, Integer type){
                CouponType ct = null;
                if (type == 1) {
                        ct = CouponType.RATE;
                } else if (type == 2) {
                        ct = CouponType.AMOUNT;
                }
                return new Coupon(name, sellerId, ct, discountRate,
                    discountAmount, validAt, expiresAt,
                    maxDiscountAmount, minPaymentAmount, details,
                    cnt);
        }
}
