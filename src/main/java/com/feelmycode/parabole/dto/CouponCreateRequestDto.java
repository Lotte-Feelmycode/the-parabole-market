package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.CouponType;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.coupons.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {

        private String name;
        private Long sellerId;
        private Integer type;

        private Integer discountRate;
        private Long discountAmount;
        private String createdAt;
        private String validAt;
        private String expiresAt;

        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String details;
        private Integer cnt;    // 발행할 쿠폰수

        /** DTO to Entity **/
        public Coupon toEntity(Seller seller, Integer type){
                CouponType ct = null;
                if (type == 1) {
                        ct = CouponType.RATE;
                } else if (type == 2) {
                        ct = CouponType.AMOUNT;
                }
                return new Coupon(name, seller, ct, discountRate,
                    discountAmount, createdAt, validAt, expiresAt,
                    maxDiscountAmount, minPaymentAmount, details,
                    cnt);
        }
}
