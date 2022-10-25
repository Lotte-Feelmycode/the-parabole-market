package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CouponAssignRequestDto {

    private Long couponId;
    private List<Long> userIdList;
}
