package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderBySellerDto {

    private Long sellerId;
    private String storeName;
    private List<OrderInfoResponseDto> orderInfoResponseDtos;

    public OrderBySellerDto(Long sellerId, String storeName,
        List<OrderInfoResponseDto> orderInfoResponseDtos) {
        this.sellerId = sellerId;
        this.storeName = storeName;
        this.orderInfoResponseDtos = orderInfoResponseDtos;
    }

}
