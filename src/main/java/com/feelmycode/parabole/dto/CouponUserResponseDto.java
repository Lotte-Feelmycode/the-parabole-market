package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.enumtype.CouponType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CouponUserResponseDto {

        /** ResponseDto for POST /api/v1/coupon/user/list **/
        private String name;
        private String serialNo;
        private String sellerName;
        private String type;
        private Integer discountValue;
        private String useState;
        private LocalDateTime useDate;
        private LocalDateTime acquiredDate;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;

        private Long maxDiscountAmount;
        private Long minPaymentAmount;

        public CouponUserResponseDto(Coupon coupon, UserCoupon userCoupon, String sellerName) {

                this.name = coupon.getName();
                this.serialNo = userCoupon.getSerialNo();
                this.sellerName = sellerName;
                this.type = coupon.getType().getName();
                this.discountValue = coupon.getDiscountValue();
                this.useState = userCoupon.getUseState().getState();
                this.acquiredDate = userCoupon.getAcquiredDate();
                this.useDate = userCoupon.getUseDate();
                this.validAt = coupon.getValidAt();
                this.expiresAt = coupon.getExpiresAt();
                this.maxDiscountAmount = coupon.getMaxDiscountAmount();
                this.minPaymentAmount = coupon.getMinPaymentAmount();
        }

}
