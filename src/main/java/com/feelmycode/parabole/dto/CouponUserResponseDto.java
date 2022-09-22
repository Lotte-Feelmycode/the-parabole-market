package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.coupons.Coupon;
import com.feelmycode.parabole.domain.coupons.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponUserResponseDto {
        /** ResponseDto for POST /api/v1/coupon/user/list **/

        private String name;
        private String serialNo;
        private String sellerName;
        private Integer type;
        private Object RateOrAmount;
        private Integer useState;
        private String useDate;
        private String acquiredDate;
        private String validAt;
        private String expiresAt;

        private Long maxDiscountAmount;
        private Long minPaymentAmount;

        public CouponUserResponseDto(Coupon coupon, UserCoupon userCoupon, String sellerName) {
                // 동일한 couponId 로 물려있는 coupon 과 userCoupon 인 경우에
                this.name = coupon.getName();
                this.serialNo = userCoupon.getSerialNo();
                this.sellerName = sellerName;
                this.type = coupon.getType();
                if (type == 1) {
                        this.RateOrAmount = coupon.getDiscountRate();
                } else if (type == 2) {
                        this.RateOrAmount = coupon.getDiscountAmount();
                }
                this.useState = userCoupon.getUseState();
                this.acquiredDate = userCoupon.getAcquiredDate();
                this.useDate = userCoupon.getUseDate();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();

        }

}
