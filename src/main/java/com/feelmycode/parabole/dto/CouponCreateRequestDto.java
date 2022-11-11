package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponCreateRequestDto {

        private String name;
        private Integer type;
        private Integer discountValue;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private String detail;
        private Integer cnt;

}
