package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Coupon;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponSellerResponseDto {
        /** ResponseDto for POST /api/v1/coupon/seller/list **/
        private String name;
        private Long sellerId;
        private Integer type;
        private Integer discountRate;
        private Long discountAmount;
        private LocalDateTime createdAt;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String detail;
        private Integer cnt;

        /** Entity to DTO **/
        public CouponSellerResponseDto(Coupon coupon) {
                // 동일한 couponId 로 물려있는 coupon 과 userCoupon 인 경우에
                this.name = coupon.getName();
                this.sellerId = coupon.getSellerId();
                this.type = coupon.getType().ordinal();
                this.discountRate = coupon.getDiscountRate();
                this.discountAmount = coupon.getDiscountAmount();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();
                this.detail = coupon.getDetail();
                this.cnt = coupon.getCnt();
        }
}
