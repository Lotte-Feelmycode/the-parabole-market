package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.UserCoupon;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponUserResponseDto {

        /** ResponseDto for POST /api/v1/coupon/user/list **/
        private String name;
        private String serialNo;
        private String storeName;
        private String type;
        private Integer discountValue;
        private String useState;
        private LocalDateTime useDate;
        private LocalDateTime expiresAt;

        public CouponUserResponseDto(Coupon coupon, UserCoupon userCoupon, String storeName) {

                this.name = coupon.getName();
                this.serialNo = userCoupon.getSerialNo();
                this.storeName = storeName;
                this.type = coupon.getType().getName();
                this.discountValue = coupon.getDiscountValue();
                this.useState = userCoupon.getUseState().getState();
                this.useDate = userCoupon.getUseDate();
                this.expiresAt = coupon.getExpiresAt();
        }

}
