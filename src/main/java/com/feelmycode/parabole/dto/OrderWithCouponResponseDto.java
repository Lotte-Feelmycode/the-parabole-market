package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderWithCouponResponseDto {

        private Long sellerId;
        private String storeName;
        private List<OrderInfoResponseDto> orderInfoList;
        private CouponResponseDto couponList;

        public OrderWithCouponResponseDto(Long sellerId, String storeName,
            List<OrderInfoResponseDto> orderInfoList, CouponResponseDto couponList) {
            this.sellerId = sellerId;
            this.storeName = storeName;
            this.orderInfoList = orderInfoList;
            this.couponList = couponList;
        }
    }
