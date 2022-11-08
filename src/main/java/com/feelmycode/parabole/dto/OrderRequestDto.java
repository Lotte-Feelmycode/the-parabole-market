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

    public OrderRequestDto(Long userId, String orderPayState) {
        this.userId = userId;
        this.orderPayState = orderPayState;
    }

    public void setOrderInfoState(String orderInfoState) {
        this.orderInfoState = orderInfoState;
    }

    public void setUserInfo(String userName, String userEmail, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

}
