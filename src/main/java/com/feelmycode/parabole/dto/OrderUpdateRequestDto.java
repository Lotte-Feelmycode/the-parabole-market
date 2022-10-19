package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequestDto {

    private Long userId;
    private String orderState;
    private String orderPayState;

    public OrderUpdateRequestDto(Long userId, String orderState, String orderPayState) {
        this.userId = userId;
        this.orderState = orderState;
        this.orderPayState = orderPayState;
    }

}
