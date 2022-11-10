package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.enumtype.OrderInfoState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderInfoRequestDto {

    private Long userId;
    private Long orderInfoId;
    private OrderInfoState orderInfoState;

    public OrderInfoRequestDto(Long userId, Long orderInfoId, String orderState) {
        this.userId = userId;
        this.orderInfoId = orderInfoId;
        this.orderInfoState = OrderInfoState.returnValueByName(orderState);
    }

}
