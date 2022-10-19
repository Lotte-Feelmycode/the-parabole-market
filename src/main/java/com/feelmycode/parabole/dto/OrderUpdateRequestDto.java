package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequestDto {

    private Long userId;
    private String orderPayState;

    public OrderUpdateRequestDto(Long userId, String orderPayState) {
        this.userId = userId;
        this.orderPayState = orderPayState;
    }

}
