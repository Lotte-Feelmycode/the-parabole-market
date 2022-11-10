package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.enumtype.OrderInfoState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderInfoRequestDto {

    private Long orderInfoId;
    private OrderInfoState orderInfoState;

    public OrderInfoRequestDto(Long orderInfoId, String orderState) {
        this.orderInfoId = orderInfoId;
        this.orderInfoState = OrderInfoState.returnValueByName(orderState);
    }

}
