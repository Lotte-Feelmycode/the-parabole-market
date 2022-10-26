package com.feelmycode.parabole.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CouponResponseDto {

    private List<CouponInfoDto> rateCoupon;
    private List<CouponInfoDto> amountCoupon;

    public CouponResponseDto() {
        rateCoupon = new ArrayList<>();
        amountCoupon = new ArrayList<>();
    }

    public CouponResponseDto setRateCoupon(List<CouponInfoDto> rateCoupon) {
        this.rateCoupon = rateCoupon;
        return this;
    }

    public CouponResponseDto setAmountCoupon(List<CouponInfoDto> amountCoupon) {
        this.amountCoupon = amountCoupon;
        return this;
    }

}
