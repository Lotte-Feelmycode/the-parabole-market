package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.coupons.Coupon;
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
        private String createdAt;
        private String validAt;
        private String expiresAt;
        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String details;
        private Integer cnt;

        /** Entity to DTO **/

        public CouponSellerResponseDto(Coupon coupon) {
                // 동일한 couponId 로 물려있는 coupon 과 userCoupon 인 경우에
                this.name = coupon.getName();
                this.sellerId = coupon.getSeller().getId();
                this.type = coupon.getType();
                this.discountRate = coupon.getDiscountRate();
                this.discountAmount = coupon.getDiscountAmount();
                this.createdAt = coupon.getCreatedAt();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();
                this.details = coupon.getDetails();

                this.cnt = coupon.getCnt();
        }
}
