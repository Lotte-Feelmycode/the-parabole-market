package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.feelmycode.parabole.enumtype.OrderPayState;
import com.feelmycode.parabole.enumtype.OrderState;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    private Long orderId;
    private List<OrderInfoRequestListDto> orderInfoRequestList;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String receiverName;
    private String receiverPhone;
    private String addressSimple;
    private String addressDetail;
    private String deliveryComment;
    private OrderState orderState;
    private OrderInfoState orderInfoState;
    private OrderPayState orderPayState;

    public OrderRequestDto(String orderPayState) {
        this.orderPayState = OrderPayState.returnValueByName(orderPayState);
    }

    public void setOrderState(String orderState) {
        this.orderState = OrderState.returnValueByName(orderState);
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setOrderInfoState(OrderInfoState orderInfoState) {
        this.orderInfoState = orderInfoState;
    }

    public void setUserInfo(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

}
