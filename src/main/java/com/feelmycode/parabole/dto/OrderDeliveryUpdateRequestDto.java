package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDeliveryUpdateRequestDto {

    private Long userId;
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
    private String payState;

    public OrderDeliveryUpdateRequestDto(Long userId, String userName, String userEmail,
        String userPhone, String receiverName, String receiverPhone, String addressSimple,
        String addressDetail, String deliveryComment, String orderState, String orderInfoState,
        String payState) {
        this.userId = userId;
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
        this.payState = payState;
    }

}
