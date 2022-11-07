package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderInfoRequestDto {

    private Long userId;
    private Long orderInfoId;
    private String orderState;

    public OrderInfoRequestDto(Long userId, String orderState) {
        this.userId = userId;
        this.orderState = orderState;
    }

}
