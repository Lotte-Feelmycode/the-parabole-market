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

    public OrderRequestDto(Long userId, Long orderId,
        List<OrderInfoRequestListDto> orderInfoRequestList, String userName, String userEmail,
        String userPhone, String receiverName, String receiverPhone, String addressSimple,
        String addressDetail, String deliveryComment, String orderState, String orderInfoState,
        String orderPayState) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderInfoRequestList = orderInfoRequestList;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.addressSimple = addressSimple;
        this.addressDetail = addressDetail;
        this.deliveryComment = deliveryComment;
        this.orderState = orderState;
        this.orderInfoState = orderInfoState;
        this.orderPayState = orderPayState;
    }

}
