package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CouponAssignRequestDto {

    private Long couponId;
    private List<Long> userIdList;

    public CouponAssignRequestDto(Long couponId, List<Long> userIdList) {
        this.couponId = couponId;
        this.userIdList = userIdList;
    }
}
