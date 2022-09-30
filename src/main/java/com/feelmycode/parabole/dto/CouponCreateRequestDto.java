package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponCreateRequestDto {

        private String name;
        private Long userId;
        private Integer type;
        private Integer discountRate;
        private Long discountAmount;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private Long maxDiscountAmount;
        private Long minPaymentAmount;
        private String detail;
        private Integer cnt;    // 발행할 쿠폰수

        public CouponCreateRequestDto(String name, Long userId, Integer type, Integer discountRate,
            Long discountAmount, LocalDateTime validAt, LocalDateTime expiresAt,
            Long maxDiscountAmount,
            Long minPaymentAmount, String detail, Integer cnt) {
                this.name = name;
                this.userId = userId;
                this.type = type;
                this.discountRate = discountRate;
                this.discountAmount = discountAmount;
                this.validAt = validAt;
                this.expiresAt = expiresAt;
                this.maxDiscountAmount = maxDiscountAmount;
                this.minPaymentAmount = minPaymentAmount;
                this.detail = detail;
                this.cnt = cnt;
        }

}
