package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequestDto {

    private Long orderId;
    private Long orderInfoId;
    private String orderState;

    public OrderUpdateRequestDto(Long orderId, Long orderInfoId, String orderState) {
        this.orderId = orderId;
        this.orderInfoId = orderInfoId;
        this.orderState = orderState;
    }

}
