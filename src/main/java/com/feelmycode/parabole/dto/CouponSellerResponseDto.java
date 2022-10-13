package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Coupon;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponSellerResponseDto {

        private Long couponId;
        private String name;
        private Integer type;
        private Integer discountValue;
        private LocalDateTime createdAt;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String detail;
        private Integer cnt;

        public CouponSellerResponseDto(Coupon coupon) {
                this.couponId = coupon.getId();
                this.name = coupon.getName();
                this.type = coupon.getType().getValue();
                this.discountValue = coupon.getDiscountValue();
                this.createdAt = coupon.getCreatedAt();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();
                this.detail = coupon.getDetail();
                this.cnt = coupon.getCnt();
        }

}
