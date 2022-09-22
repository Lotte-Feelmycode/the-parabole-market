package com.feelmycode.parabole.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter // dto 에 getter 안붙여주면 406 Not Acceptable 납니다
@AllArgsConstructor
public class CouponCreateResponseDto {
    private String couponName;
    private String sellerName;
    private Integer type;
    private Integer cnt;
}
