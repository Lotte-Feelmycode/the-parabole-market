package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.coupons.Coupon;
import com.feelmycode.parabole.domain.coupons.UserCoupon;
import java.time.LocalDateTime;
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
        private LocalDateTime useDate;
        private LocalDateTime acquiredDate;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;

        private Long maxDiscountAmount;
        private Long minPaymentAmount;

        public CouponUserResponseDto(Coupon coupon, UserCoupon userCoupon, String sellerName) {

                // 동일한 couponId 로 물려있는 coupon 과 userCoupon 인 경우에
                this.name = coupon.getName();
                this.serialNo = userCoupon.getSerialNo();
                this.sellerName = sellerName;
                this.type = coupon.getType().ordinal();
                if (type == 1) {
                        this.RateOrAmount = coupon.getDiscountRate();
                } else if (type == 2) {
                        this.RateOrAmount = coupon.getDiscountAmount();
                }
                this.useState = userCoupon.getUseState().ordinal();
                this.acquiredDate = userCoupon.getAcquiredDate();
                this.useDate = userCoupon.getUseDate();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();
        }

}
