package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    private Long userId;
    private Long orderId;
    private List<OrderInfoRequestListDto> orderInfoRequestList;
    private String orderPayState;

    public OrderRequestDto(Long userId, String orderPayState) {
        this.userId = userId;
        this.orderPayState = orderPayState;
    }

    public OrderRequestDto(Long userId, Long orderId,
        List<OrderInfoRequestListDto> orderInfoRequestList, String orderPayState) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderInfoRequestList = orderInfoRequestList;
        this.orderPayState = orderPayState;
    }
}
