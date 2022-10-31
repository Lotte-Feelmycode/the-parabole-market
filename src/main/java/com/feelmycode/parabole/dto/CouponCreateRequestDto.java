package com.feelmycode.parabole.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CouponCreateRequestDto {

        private String name;
        private Long userId;
        private Integer type;
        private Integer discountValue;
        private LocalDateTime validAt;
        private LocalDateTime expiresAt;
        private String detail;
        private Integer cnt;

        public CouponCreateRequestDto(String name, Long userId, Integer type, Integer discountValue,
            LocalDateTime validAt, LocalDateTime expiresAt, String detail, Integer cnt) {
                this.name = name;
                this.userId = userId;
                this.type = type;
                this.discountValue = discountValue;
                this.validAt = validAt;
                this.expiresAt = expiresAt;
                this.detail = detail;
                this.cnt = cnt;
        }

}
