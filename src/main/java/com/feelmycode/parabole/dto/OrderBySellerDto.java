package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderBySellerDto {

        private Long sellerId;
        private String storeName;
        private List<OrderInfoResponseDto> orderBySellerDtoList;
        private CouponResponseDto couponDto;

        public OrderBySellerDto(Long sellerId, String storeName,
            List<OrderInfoResponseDto> orderBySellerDtoList, CouponResponseDto couponDto) {
            this.sellerId = sellerId;
            this.storeName = storeName;
            this.orderBySellerDtoList = orderBySellerDtoList;
            this.couponDto = couponDto;
        }
    }
