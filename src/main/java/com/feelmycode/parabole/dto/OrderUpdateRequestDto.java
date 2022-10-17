package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequestDto {

    private Long userId;
    private String orderState;

    public OrderUpdateRequestDto(Long userId, String orderState) {
        this.userId = userId;
        this.orderState = orderState;
    }

}
