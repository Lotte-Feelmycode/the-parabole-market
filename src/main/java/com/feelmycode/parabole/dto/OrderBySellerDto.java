package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderBySellerDto {

        private Long sellerId;
        private String storeName;
        private List<OrderInfoResponseDto> orderInfoResponseDtos;
        private CouponResponseDto couponDto;

        public OrderBySellerDto(Long sellerId, String storeName,
            List<OrderInfoResponseDto> orderInfoResponseDtos, CouponResponseDto couponDto) {
            this.sellerId = sellerId;
            this.storeName = storeName;
            this.orderInfoResponseDtos = orderInfoResponseDtos;
            this.couponDto = couponDto;
        }
    }
