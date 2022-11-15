package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.enumtype.OrderInfoState;
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
    private String orderState;
    private String orderInfoState;
    private String orderPayState;

    public OrderRequestDto(String orderPayState) {
        this.orderPayState = orderPayState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState.getState();
    }

    public void setOrderInfoState(OrderInfoState orderInfoState) {
        this.orderInfoState = orderInfoState.getState();
    }

    public void setUserInfo(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

}
