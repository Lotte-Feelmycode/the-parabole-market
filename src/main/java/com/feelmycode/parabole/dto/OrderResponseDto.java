package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private Long orderId;
    private Long cnt;
    List<OrderBySellerDto> orderBySellerDtoList;

    public OrderResponseDto(Long orderId, Long cnt, List<OrderBySellerDto> orderBySellerDtoList) {
        this.orderId = orderId;
        this.cnt = cnt;
        this.orderBySellerDtoList = orderBySellerDtoList;
    }

}
